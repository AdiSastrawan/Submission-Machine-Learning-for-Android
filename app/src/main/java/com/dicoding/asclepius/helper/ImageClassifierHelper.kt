package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Message
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.Surface
import com.dicoding.asclepius.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


class ImageClassifierHelper(
    private val thresholds : Float = 0.5f,
    private val context: Context,
    private val classifierListener : ClassifierListener? = null
) {
    private var imageClassifier : ImageClassifier? = null
    init {
        setupImageClassifier()
    }
    private fun setupImageClassifier() {
        val baseOptions = BaseOptions.builder().setNumThreads(4)
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(1)
            .setScoreThreshold(thresholds)
            .setBaseOptions(baseOptions.build())
        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(context,MODEL_NAME,options.build())

        }catch (e:IllegalStateException){
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG,e.message.toString())
        }

    }

    fun classifyStaticImage(imageUri: Uri) {
        if(imageClassifier == null){
            setupImageClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.UINT8))
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
            var inferenceTime = SystemClock.uptimeMillis()
            val results = imageClassifier?.classify(tensorImage)
            inferenceTime = SystemClock.uptimeMillis() - inferenceTime
            classifierListener?.onResult(results,inferenceTime)
        }

    }



    interface ClassifierListener {
        fun onError(e:String)
        fun onResult(result:List<Classifications>?,inferenceTime : Long)
    }
    companion object{
        private const val MODEL_NAME = "cancer_classification.tflite"
        private const val TAG = "ImageClassifierHelper"
    }
}
