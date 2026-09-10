import os

TRAIN_DIR = "dataset/raw/PlantVillage/train"

classes = sorted(os.listdir(TRAIN_DIR))

print("\nAvailable Classes:\n")

for i, cls in enumerate(classes):
    print(f"{i+1}. {cls}")