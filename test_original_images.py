import matplotlib.pyplot as plt
import tensorflow as tf
import config

dataset = tf.keras.utils.image_dataset_from_directory(
    config.TRAIN_DATASET,
    image_size=(224, 224),
    batch_size=9,
    shuffle=True
)

images, labels = next(iter(dataset))

plt.figure(figsize=(12, 12))

for i in range(9):
    plt.subplot(3, 3, i + 1)
    plt.imshow(images[i].numpy().astype("uint8"))
    plt.axis("off")

plt.tight_layout()
plt.show()