import os

TRAIN_DIR = "dataset/raw/PlantVillage/train"

print("Train Exists:", os.path.exists(TRAIN_DIR))

classes = sorted(os.listdir(TRAIN_DIR))

print("Total Classes:", len(classes))

first_class = classes[0]

print("\nFirst Class:", first_class)

class_path = os.path.join(TRAIN_DIR, first_class)

print("\nFiles inside:")

for file in os.listdir(class_path)[:20]:
    print(file)