from preprocessing.dataset_loader import load_datasets

train_ds, val_ds, class_names = load_datasets()

print("=" * 50)

print("Number of Classes")

print(len(class_names))

print("=" * 50)

print(class_names)

for images, labels in train_ds.take(1):

    print()

    print("Batch Shape")

    print(images.shape)

    print()

    print("Label Shape")

    print(labels.shape)