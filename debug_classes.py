import tensorflow as tf
from config import TRAIN_DATASET, VALIDATION_DATASET, IMAGE_SIZE, BATCH_SIZE

train_ds = tf.keras.utils.image_dataset_from_directory(
    TRAIN_DATASET,
    image_size=IMAGE_SIZE,
    batch_size=BATCH_SIZE,
    shuffle=False
)

val_ds = tf.keras.utils.image_dataset_from_directory(
    VALIDATION_DATASET,
    image_size=IMAGE_SIZE,
    batch_size=BATCH_SIZE,
    shuffle=False
)

print("Train Classes:")
print(train_ds.class_names)

print("\nValidation Classes:")
print(val_ds.class_names)

print("\nSame Order?")
print(train_ds.class_names == val_ds.class_names)