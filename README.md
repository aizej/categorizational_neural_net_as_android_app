# categorizational_neural_net_as_android_app
In this project, I used TensorFlow cats/dogs dataset to train my categorization neural network
Then I took this model and built a simple Kotlin app that categorizes live from the camera


First, we import the image augmentation library "albumentations" which will allow us to randomly rotate and change the exposure of our image dataset. This will improve our model's performance in extreme conditions.

![import](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/8f1a2c52-c549-4133-9285-bbbc74afe1d3)


then we will create some rotation and exposure functions:
![brightnes_rand](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/03272501-2df3-4eff-9e37-65749989f2f9)


after that, we finally take the pictures from our folder and randomly change their exposure, rotate them, and then connect them with their label and add them to a list.

![rotace_velikost_obrazku](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/0cbd6017-a9e4-45e3-aca9-0b08e76b0f56)

Then we shuffle the data, extract the labels, convert it to a numpy array, and save them

![data_hotova](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/1a1038e6-d8c4-4486-998b-cd6e95b32c52)
To finish data preparation we load our saved dataset and normalize it:

![data_hotova](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/59f1c2f6-a242-494a-a1f5-c55f9eb770d6)


That's all for the data preparations now for the learning:


First, we can design some light CNN and let it learn on the dataset:
![CNN](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/63b44e52-5b97-4166-a958-285bb9be760c)


Now I decided to build a simple interface in Android Studio with Kotlin. That takes data from the front camera and gives live categorization

Here is a test on my cat:
![Screenshot_20230919_174528_detection demo](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/bc62be2a-8af4-4fe5-af6c-3c174f2b44d5)
![Screenshot_20230919_174438_detection demo](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/dfd10293-79a5-434a-8ce0-788272c1d909)


Sadly I don't have a dog so here is a test on a picture of a dog from a monitor:
![Screenshot_20230919_174646_detection demo](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/ac2aa034-bf5f-4ee5-92b6-0c42693f3649)

![Screenshot_20230919_174609_detection demo](https://github.com/aizej/categorizational_neural_net_as_android_app/assets/61479273/de2f06e8-c4d7-4add-bd3e-caa635bfbc99)
