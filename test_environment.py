import tensorflow as tf
import cv2
import numpy as np

print("TensorFlow:", tf.__version__)
print("OpenCV:", cv2.__version__)
print("NumPy:", np.__version__)

print("GPU Available:", tf.config.list_physical_devices("GPU"))