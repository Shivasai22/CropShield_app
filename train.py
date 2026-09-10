from training.trainer import train
from evaluation.plots import plot_training_history


if __name__ == "__main__":

    print("=" * 60)
    print("CropShield - Model Training")
    print("=" * 60)

    history = train()

    print("\nTraining completed.")

    plot_training_history(history)

    print("\nTraining plots generated.")