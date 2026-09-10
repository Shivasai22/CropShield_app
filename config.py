import os

# ==========================
# Project Paths
# ==========================

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

RAW_DATASET = os.path.join(BASE_DIR, "dataset", "raw")

PROCESSED_DATASET = os.path.join(BASE_DIR, "dataset", "processed")

TRAIN_DIR = os.path.join(BASE_DIR, "dataset", "train")

VALIDATION_DIR = os.path.join(BASE_DIR, "dataset", "validation")

TEST_DIR = os.path.join(BASE_DIR, "dataset", "test")

MODEL_DIR = os.path.join(BASE_DIR, "saved_models")

TFLITE_DIR = os.path.join(BASE_DIR, "tflite")

OUTPUT_DIR = os.path.join(BASE_DIR, "outputs")

LOG_DIR = os.path.join(BASE_DIR, "logs")

# ==========================
# Dataset Settings
# ==========================

IMAGE_SIZE = (224, 224)

BATCH_SIZE = 32

SEED = 42

NUM_CLASSES = 38

TRAIN_DATASET = os.path.join(BASE_DIR, "dataset", "raw", "PlantVillage", "train")

VALIDATION_DATASET = os.path.join(BASE_DIR, "dataset", "raw", "PlantVillage", "val")


# ==========================
# Model Settings
# ==========================

INPUT_SHAPE = (224, 224, 3)

LEARNING_RATE = 0.0001

EPOCHS = 20

DROPOUT_RATE = 0.30



# ==========================
# Training Configuration
# ==========================

MODEL_NAME = "mobilenetv2"

MODEL_SAVE_PATH = os.path.join(
    BASE_DIR,
    "saved_models",
    "mobilenetv2_best.keras"
)

CSV_LOG_PATH = os.path.join(
    BASE_DIR,
    "logs",
    "training_log.csv"
)

TENSORBOARD_LOG_DIR = os.path.join(
    BASE_DIR,
    "logs",
    "tensorboard"
)

PATIENCE = 5


