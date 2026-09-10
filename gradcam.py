import os
import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt

import config


# ============================================================
# CropShield Grad-CAM
# ============================================================

print("=" * 60)
print("CropShield Grad-CAM Explainability")
print("=" * 60)


# ============================================================
# Load trained model
# ============================================================

print("\nLoading trained model...")

model = tf.keras.models.load_model(
    config.MODEL_SAVE_PATH
)

print("Model loaded successfully.")


# ============================================================
# Get MobileNetV2 base model
# ============================================================

def get_base_model(model):

    for layer in model.layers:

        if isinstance(layer, tf.keras.Model):

            print("\nBase Model:")
            print(layer.name)

            return layer

    raise ValueError(
        "MobileNetV2 base model was not found."
    )


# ============================================================
# Get class names
# ============================================================

def get_class_names():

    class_names = sorted(
        [
            folder
            for folder in os.listdir(
                config.VALIDATION_DATASET
            )
            if os.path.isdir(
                os.path.join(
                    config.VALIDATION_DATASET,
                    folder
                )
            )
        ]
    )

    return class_names


# ============================================================
# Load image
# ============================================================

def load_image(image_path):

    image = tf.keras.utils.load_img(
        image_path,
        target_size=config.IMAGE_SIZE
    )

    image_array = tf.keras.utils.img_to_array(
        image
    )

    # Original image for display
    original_image = image_array.astype(
        np.uint8
    )

    # Same scaling used by your dataset loader
    image_array = image_array.astype(
        np.float32
    ) / 255.0

    image_array = np.expand_dims(
        image_array,
        axis=0
    )

    return image_array, original_image


# ============================================================
# Prediction
# ============================================================

def predict_image(
    image,
    model
):

    prediction = model.predict(
        image,
        verbose=0
    )[0]

    predicted_class = int(
        np.argmax(prediction)
    )

    confidence = float(
        prediction[predicted_class] * 100
    )

    return (
        predicted_class,
        confidence,
        prediction
    )


# ============================================================
# Find a good correctly classified image
# ============================================================

def find_correct_image(
    model,
    class_names
):

    validation_dir = config.VALIDATION_DATASET

    print(
        "\nSearching validation dataset "
        "for a correctly classified image..."
    )

    # We don't need to scan all 10,861 images.
    # Search a limited number from each class.
    MAX_IMAGES_PER_CLASS = 20

    candidates = []

    for actual_index, class_name in enumerate(
        class_names
    ):

        class_directory = os.path.join(
            validation_dir,
            class_name
        )

        if not os.path.isdir(
            class_directory
        ):
            continue

        image_files = [
            file
            for file in os.listdir(
                class_directory
            )
            if file.lower().endswith(
                (
                    ".jpg",
                    ".jpeg",
                    ".png"
                )
            )
        ]

        for image_file in image_files[
            :MAX_IMAGES_PER_CLASS
        ]:

            candidates.append(
                (
                    os.path.join(
                        class_directory,
                        image_file
                    ),
                    actual_index
                )
            )

    print(
        f"Images selected: {len(candidates)}"
    )

    # --------------------------------------------------------
    # Batch prediction
    # --------------------------------------------------------

    BATCH_SIZE = 32

    best_candidate = None
    best_confidence = 0.0

    for start in range(
        0,
        len(candidates),
        BATCH_SIZE
    ):

        batch_candidates = candidates[
            start:start + BATCH_SIZE
        ]

        batch_images = []
        batch_originals = []

        for image_path, actual_index in batch_candidates:

            image, original_image = load_image(
                image_path
            )

            batch_images.append(
                image[0]
            )

            batch_originals.append(
                original_image
            )

        batch_images = np.asarray(
            batch_images,
            dtype=np.float32
        )

        predictions = model.predict(
            batch_images,
            verbose=0
        )

        predicted_classes = np.argmax(
            predictions,
            axis=1
        )

        confidences = (
            np.max(
                predictions,
                axis=1
            ) * 100
        )

        # ----------------------------------------------------
        # Find correct prediction
        # ----------------------------------------------------

        for i in range(
            len(batch_candidates)
        ):

            image_path, actual_index = (
                batch_candidates[i]
            )

            predicted_class = int(
                predicted_classes[i]
            )

            confidence = float(
                confidences[i]
            )

            # Correct prediction
            if predicted_class == actual_index:

                # Keep strongest correct example
                if confidence > best_confidence:

                    best_confidence = confidence

                    best_candidate = (
                        image_path,
                        batch_images[i:i + 1],
                        batch_originals[i],
                        actual_index,
                        predicted_class,
                        confidence
                    )

        processed = min(
            start + BATCH_SIZE,
            len(candidates)
        )

        print(
            f"Checked "
            f"{processed}/{len(candidates)}"
        )

    if best_candidate is None:

        raise RuntimeError(
            "No correctly classified image "
            "was found."
        )

    print(
        "\nCorrect image found."
    )

    return best_candidate


# ============================================================
# Correct Grad-CAM implementation
# ============================================================

def make_gradcam_heatmap(
    image,
    base_model,
    global_average_pooling,
    dropout,
    dense_layer
):

    # --------------------------------------------------------
    # GradientTape
    # --------------------------------------------------------

    with tf.GradientTape() as tape:

        # MobileNetV2 preprocessing
        processed_image = (
            tf.keras.applications.mobilenet_v2.preprocess_input(
                image
            )
        )

        # Get convolutional feature maps
        feature_maps = base_model(
            processed_image,
            training=False
        )

        # Watch feature maps
        tape.watch(
            feature_maps
        )

        # Classification head
        x = global_average_pooling(
            feature_maps
        )

        x = dropout(
            x,
            training=False
        )

        predictions = dense_layer(
            x
        )

        # Predicted class
        predicted_class = tf.argmax(
            predictions[0]
        )

        class_score = predictions[
            0,
            predicted_class
        ]

    # --------------------------------------------------------
    # Gradients
    # --------------------------------------------------------

    gradients = tape.gradient(
        class_score,
        feature_maps
    )

    if gradients is None:

        raise RuntimeError(
            "Gradients are None. "
            "Grad-CAM could not calculate gradients."
        )

    # --------------------------------------------------------
    # Global average pooling of gradients
    # --------------------------------------------------------

    weights = tf.reduce_mean(
        gradients,
        axis=(1, 2)
    )

    # --------------------------------------------------------
    # Weighted feature maps
    # --------------------------------------------------------

    cam = tf.reduce_sum(
        feature_maps * weights[
            :, tf.newaxis, tf.newaxis, :
        ],
        axis=-1
    )

    # First image
    cam = cam[0]

    # ReLU
    cam = tf.maximum(
        cam,
        0
    )

    # Normalize
    cam_max = tf.reduce_max(
        cam
    )

    if float(cam_max.numpy()) > 0:

        cam = cam / cam_max

    return cam.numpy()


# ============================================================
# Generate Grad-CAM
# ============================================================

def generate_gradcam():

    class_names = get_class_names()

    # --------------------------------------------------------
    # Models / layers
    # --------------------------------------------------------

    base_model = get_base_model(
        model
    )

    target_layer = base_model.get_layer(
        "Conv_1"
    )

    print("\nTarget Layer:")
    print(
        target_layer.name
    )

    global_average_pooling = model.get_layer(
        "global_average_pooling2d"
    )

    dropout = model.get_layer(
        "dropout"
    )

    dense_layer = model.get_layer(
        "dense"
    )

    # --------------------------------------------------------
    # Find correct validation image
    # --------------------------------------------------------

    (
        image_path,
        image,
        original_image,
        actual_index,
        predicted_index,
        confidence
    ) = find_correct_image(
        model,
        class_names
    )

    actual_label = class_names[
        actual_index
    ]

    predicted_label = class_names[
        predicted_index
    ]

    print("\nImage:")
    print(
        image_path
    )

    print("\nActual Class:")
    print(
        actual_label
    )

    print("\nPredicted Class:")
    print(
        predicted_label
    )

    print(
        f"\nConfidence: "
        f"{confidence:.2f}%"
    )

    # --------------------------------------------------------
    # Grad-CAM
    # --------------------------------------------------------

    heatmap = make_gradcam_heatmap(
        image,
        base_model,
        global_average_pooling,
        dropout,
        dense_layer
    )

    # --------------------------------------------------------
    # Save
    # --------------------------------------------------------

    output_dir = os.path.join(
        config.OUTPUT_DIR,
        "gradcam"
    )

    os.makedirs(
        output_dir,
        exist_ok=True
    )

    output_path = os.path.join(
        output_dir,
        "gradcam_final.png"
    )

    # --------------------------------------------------------
    # Visualization
    # --------------------------------------------------------

    plt.figure(
        figsize=(12, 5)
    )

    # Original
    plt.subplot(
        1,
        2,
        1
    )

    plt.imshow(
        original_image
    )

    plt.title(
        "Original Image\n"
        + actual_label
    )

    plt.axis(
        "off"
    )

    # Grad-CAM
    plt.subplot(
        1,
        2,
        2
    )

    plt.imshow(
        original_image
    )

    plt.imshow(
        heatmap,
        cmap="jet",
        alpha=0.45
    )

    plt.title(
        "Grad-CAM\n"
        + predicted_label
        + f"\nConfidence: {confidence:.2f}%"
    )

    plt.axis(
        "off"
    )

    plt.tight_layout()

    plt.savefig(
        output_path,
        dpi=300,
        bbox_inches="tight"
    )

    plt.show()

    # --------------------------------------------------------
    # Final output
    # --------------------------------------------------------

    print("\n" + "=" * 60)
    print("Grad-CAM Completed Successfully")
    print("=" * 60)

    print(
        "\nActual Class:"
    )

    print(
        actual_label
    )

    print(
        "\nPredicted Class:"
    )

    print(
        predicted_label
    )

    print(
        f"\nConfidence:"
        f" {confidence:.2f}%"
    )

    print(
        "\nSaved:"
    )

    print(
        output_path
    )


# ============================================================
# Main
# ============================================================

if __name__ == "__main__":

    generate_gradcam()