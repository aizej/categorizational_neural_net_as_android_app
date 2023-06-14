from keras.models import load_model
import tensorflow as tf

model_name = r"cat_vs_dogs_bigger_1_1649228742.2319064_14epochs"
model = load_model(f"{model_name}.h5")


converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

print("model converted")

# Save the model.
with open(f'{model_name}.tflite', 'wb') as f:
  f.write(tflite_model)