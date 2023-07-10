package com.example.workingcamera

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.util.Size
import android.view.Surface.ROTATION_90
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.camera.camera2.internal.compat.workaround.TargetAspectRatio.RATIO_4_3
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.workingcamera.Constants.TAG
import com.example.workingcamera.databinding.ActivityMainBinding
import com.example.workingcamera.ml.CatVsDogs3mb
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.lang.Exception
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    var running: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        outputDirectory = getOutputDirectory()

        if (allPermisionGranted()) {
            Toast.makeText(this, "We have permission", Toast.LENGTH_LONG).show()
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                Constants.REQUIRED_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISION

            )
        }

        object : CountDownTimer(10000000, 500) {
            override fun onTick(millisUntilFinished: Long) {
                if (running) {
                    takePhoto()
                }
            }
            override fun onFinish() {}
        }.start()


        binding.btnTakePhoto.setOnClickListener {
            running = !running
            if(!running) {
                val button: Button = findViewById(R.id.btnTakePhoto) as Button
                button.setText("START")

                val textView: TextView = findViewById(R.id.tvresult) as TextView
                textView.setText("Press Start")
            }
            else {
                val textView: Button = findViewById(R.id.btnTakePhoto) as Button
                textView.setText("STOP")
            }
        }
    }


    fun rotateBitmap(source: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix, true
        )
    }


    private fun getOutputDirectory(): File {
    val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
        File(mFile, resources.getString(R.string.app_name)).apply {
            mkdirs()
        }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else filesDir
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?:return

        /*
        val photoFile = File(outputDirectory,
            SimpleDateFormat(Constants.FILE_NAME_FORMAT,
                Locale.getDefault()).
                format(System.
                    currentTimeMillis())+".jpg")

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()


        imageCapture.takePicture(outputOption, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)


                val msg = "photo Saved"
                Toast.makeText(this@MainActivity,msg,Toast.LENGTH_LONG).show()
                finish()
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e(TAG,"onError${exception.message}",exception)
            }

        })

         */
        imageCapture.takePicture(ContextCompat.getMainExecutor(this), object :
            ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                //get bitmap from image
                val bitmap = imageProxyToBitmap(image)
                runObjectDetection(bitmap)
                //val textView: TextView = findViewById(R.id.tvresult) as TextView
                //textView.setText("${LocalDateTime.now()}")

                //val imageView: ImageView = findViewById(R.id.imageView) as ImageView
                //imageView.setImageBitmap(bitmap)

                super.onCaptureSuccess(image)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
            }
        })

    }

    private fun runObjectDetection(bitmap: Bitmap) {
        val model = CatVsDogs3mb.newInstance(this)
        //val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val imagesize = 150

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, imagesize, imagesize, true)

        val rotatedresizedBitmap = rotateBitmap(resizedBitmap, 90f)

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(rotatedresizedBitmap)
        var byteBuffer = tensorImage.buffer


        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imagesize, imagesize, 3), DataType.FLOAT32)

        /*
        Log.d("shape", byteBuffer.toString())
        Log.d("?","?")
        Log.d("shape", inputFeature0.buffer.toString())
         */

        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val text = "cat ${(outputFeature0.floatArray[0]*100).roundToInt()}% \ndog ${(outputFeature0.floatArray[1]*100).roundToInt()}%"
        val textView: TextView = findViewById(R.id.tvresult) as TextView
        textView.setText(text)

        val imageView: ImageView = findViewById(R.id.imageView) as ImageView
        imageView.setImageBitmap(rotatedresizedBitmap)

        model.close()
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }



    private fun startCamera(){

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            /*
            val preview = Preview.Builder().build().also {
                mPreview->
                mPreview.setSurfaceProvider(
                    binding.vievFinder.surfaceProvider
                )
            }
             */

            imageCapture = ImageCapture.Builder().setJpegQuality(75).setTargetResolution(Size(150, 150)).build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,cameraSelector, imageCapture
                )

            }catch (e:Exception){
                Log.d(Constants.TAG,"FAILED to startcamera: ", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if(requestCode == Constants.REQUEST_CODE_PERMISION){
            if(allPermisionGranted()){
                //our code
                startCamera()
            }
            else{
                Toast.makeText(this,"permissions not granted",Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun allPermisionGranted() =
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }

}