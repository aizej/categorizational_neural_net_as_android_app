# categorizational_neural_net_as_android_app


In this project i used tensorflow cats/dogs dataset to train my own categorization neural-network
Then i took this model and build a simple kotlin app that categorizes live from camera


First we import image augmentation library "albumentations" that will alow us to randomly rotate and change exposure of our image dataset. This will improve our models preformance in extreme condisions.

![Screenshot_20230618_132416_Firefox](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/330e3f2c-9d98-4d3b-96b1-d1f10c74c720)

then we will create some rotation and exposure functions:![Screenshot_20230618_132409_Firefox](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/670dcd2d-a021-48f7-a69a-5f4e1e1352a0)

after that we finaly take the pictures from our folder and randomly change their exposure ,rotate them and then connect them with their label and add them in to a list.
![Screenshot_20230618_133222_Firefox](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/fafd414f-efb2-4ae3-972b-2844308d0ae7)
