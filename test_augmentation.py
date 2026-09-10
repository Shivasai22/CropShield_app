import matplotlib.pyplot as plt
import tensorflow as tf

from preprocessing.dataset_loader import load_datasets

train_ds, _, class_names = load_datasets()

images, labels = next(iter(train_ds))

plt.figure(figsize=(12, 12))

for i in range(9):
    plt.subplot(3, 3, i + 1)

    image = tf.clip_by_value(images[i], 0.0, 1.0)

    plt.imshow(image.numpy())

    plt.title(class_names[labels[i].numpy()], fontsize=8)

    plt.axis("off")

plt.tight_layout()
plt.show()