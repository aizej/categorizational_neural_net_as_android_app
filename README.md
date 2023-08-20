# categorizational_neural_net_as_android_app
In this project, i used TensorFlow cats/dogs dataset to train my categorization neural-network
Then I took this model and built a simple Kotlin app that categorizes live from the camera


First, we import the image augmentation library "argumentation" that will allow us to randomly rotate and change the exposure of our image dataset. This will improve our model's preformance in extreme conditions.

![Screenshot_20230618_132416_Firefox](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/330e3f2c-9d98-4d3b-96b1-d1f10c74c720)

then we will create some rotation and exposure functions:![Screenshot_20230618_132409_Firefox](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/670dcd2d-a021-48f7-a69a-5f4e1e1352a0)

after that, we finaly take the pictures from our folder and randomly change their exposure,rotate them, and then connect them with their label and add them to a list.
![Screenshot_20230618_133222_Firefox](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/fafd414f-efb2-4ae3-972b-2844308d0ae7)

Then we shuffle the data, extract the labels, convert it to an numpy array, and save them
![Screenshot_20230618_133307_Firefox](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/e4d8e91f-20a7-4f37-a551-c23bd989a368)

That's all for the data preparations now for the learning:

First, we load our saved dataset and normalize it:
![Screenshot_20230618_133524_Firefox](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/2fae0daf-9e66-48ad-b3cb-674d20b53e23)

after that, we can design some light cnn and let it learn on the dataset:
![Screenshot_20230618_133658_Firefox](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/60d49d4c-9d40-41c3-8100-ce67ab551d5f)

Now I decided to build a simple interface in android studio with kotlin. That takes data from the front camera and gives live categorization

Here is a test on my cat:
![Screenshot_20230617_210113_detection demo](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/06053837-f004-43d2-a3f0-59cfb5ab96ae)

Sadly I don't have a dog so here is a test on a picture of a dog:
![Screenshot_20230617_210322_detection demo](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/99ccc58f-c812-486b-abe8-5aee730f2f40)
![Screenshot_20230617_210235_detection demo](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/afce6207-49ca-44dc-9def-b8a40a2056d5)
![Screenshot_20230617_210435_detection demo](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/f8547301-131e-4a56-9c84-d21f10936108)
