import os

TRAIN_DIR = "dataset/raw/PlantVillage/train"
VAL_DIR = "dataset/raw/PlantVillage/val"

print("=" * 80)
print("CropShield - PlantVillage Dataset Information")
print("=" * 80)

classes = sorted([
    c for c in os.listdir(TRAIN_DIR)
    if os.path.isdir(os.path.join(TRAIN_DIR, c))
])

print(f"\nTotal Classes: {len(classes)}\n")

total_train = 0
total_val = 0

VALID_EXTENSIONS = (".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG")

for cls in classes:

    train_path = os.path.join(TRAIN_DIR, cls)
    val_path = os.path.join(VAL_DIR, cls)

    train_images = [
    f for f in os.listdir(train_path)
    if f.lower().endswith((".jpg", ".jpeg", ".png"))
    ]

    val_images = [
        f for f in os.listdir(val_path)
        if f.endswith(VALID_EXTENSIONS)
    ]

    train_count = len(train_images)
    val_count = len(val_images)

    total_train += train_count
    total_val += val_count

    print(
        f"{cls:<50}"
        f"Train: {train_count:<5}"
        f"Val: {val_count:<5}"
    )

print("\n" + "=" * 80)

print(f"Total Train Images : {total_train}")
print(f"Total Validation Images : {total_val}")
print(f"Grand Total : {total_train + total_val}")

print("=" * 80)