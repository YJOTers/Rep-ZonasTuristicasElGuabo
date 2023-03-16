package com.example.clasificarpeces

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.view.Surface
import android.util.Log
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifier(var threshold: Float = 0.1f,
  var numThreads: Int = 2,
  var maxResults: Int = 3,
  var currentDelegate: Int = 0,
  var currentModel: Int = 0,
  val context: Context,
  val imageClassifierListener: ClassifierListener?
  ){

  interface ClassifierListener {
    fun onError(error: String)
    fun onResults(
      results: List<Classifications>?,
      inferenceTime: Long
    )
  }

  companion object {
    private const val TAG = "ImageClassifier"
  }

  private var imageClassifier: ImageClassifier? = null

  init {
    setupImageClassifier()
  }

  private fun setupImageClassifier() {
    val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
      .setScoreThreshold(threshold)
      .setMaxResults(maxResults)

    val baseOptionsBuilder = BaseOptions.builder().setNumThreads(numThreads)

    optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

    val modelName = "modelo.tflite"

    try {
      imageClassifier =
        ImageClassifier.createFromFileAndOptions(context, modelName, optionsBuilder.build())
    } catch (e: IllegalStateException) {
      imageClassifierListener?.onError(
        "El clasificador de imagenes fallo. Mire el LOG para mas detalles"
      )
      Log.e(TAG, "TFLite fallo al cargar un modelo dando el error: " + e.message)
    }
  }

  fun classify(image: Bitmap, rotation: Int) {
    if (imageClassifier == null) {
      setupImageClassifier()
    }

    var inferenceTime = SystemClock.uptimeMillis()

    val imageProcessor =
      ImageProcessor.Builder()
        .build()

    val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image))

    val imageProcessingOptions = ImageProcessingOptions.builder()
      .setOrientation(getOrientationFromRotation(rotation))
      .build()

    val results = imageClassifier?.classify(tensorImage, imageProcessingOptions)
    inferenceTime = SystemClock.uptimeMillis() - inferenceTime
    imageClassifierListener?.onResults(
      results,
      inferenceTime
    )
  }

  private fun getOrientationFromRotation(rotation: Int) : ImageProcessingOptions.Orientation {
    return when (rotation) {
      Surface.ROTATION_270 ->
        ImageProcessingOptions.Orientation.BOTTOM_RIGHT
      Surface.ROTATION_180 ->
        ImageProcessingOptions.Orientation.RIGHT_BOTTOM
      Surface.ROTATION_90 ->
        ImageProcessingOptions.Orientation.TOP_LEFT
      else ->
        ImageProcessingOptions.Orientation.RIGHT_TOP
    }
  }
}