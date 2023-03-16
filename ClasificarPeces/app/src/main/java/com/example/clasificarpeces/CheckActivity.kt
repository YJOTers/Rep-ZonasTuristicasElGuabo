package com.example.clasificarpeces

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clasificarpeces.databinding.ActivityCheckBinding
import org.tensorflow.lite.task.vision.classifier.Classifications

class CheckActivity : AppCompatActivity(), ImageClassifier.ClassifierListener {
    private lateinit var imageClassifierHelper: ImageClassifier
    private lateinit var imageUri: Uri
    private lateinit var viewBinding: ActivityCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCheckBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.include.myToolbar)
        setTitle(R.string.activity2_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Mostrar imagen obtenida
        imageUri = Uri.parse(intent?.extras?.getString("imagen"))
        viewBinding.imageView.setImageURI(imageUri)

        //Inicializar clasificador de imagenes
        imageClassifierHelper =
            ImageClassifier(context = this, imageClassifierListener = this)
        with(viewBinding.listData){
            layoutManager = LinearLayoutManager(this.context)
            adapter = classificationResultsAdapter
        }

        //Analizar la imagen
        viewBinding.btnCheck.setOnClickListener {
            //Clasificar imagen
            val imageBitmap = viewBinding.imageView.drawable.toBitmapOrNull()
            imageBitmap?.let {
                imageClassifierHelper.classify(imageBitmap, getScreenOrientation())
            } ?: Toast.makeText(this, "Imagen no obtenida", Toast.LENGTH_SHORT).show()
        }
    }

    private val classificationResultsAdapter by lazy {
        AdapterView().apply {
            updateAdapterSize(imageClassifierHelper.maxResults)
        }
    }

    private fun getScreenOrientation() : Int {
        val outMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        val display: Display?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            display = this.display
            display?.getRealMetrics(outMetrics)
        } else {
            display = this.windowManager.defaultDisplay
            display?.getMetrics(outMetrics)
        }
        return display?.rotation ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onError(error: String) {
        this.runOnUiThread {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            classificationResultsAdapter.updateResults(null)
            classificationResultsAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResults(
        results: List<Classifications>?,
        inferenceTime: Long
    ) {
        this.runOnUiThread {
            // Show result on bottom sheet
            classificationResultsAdapter.updateResults(results)
            classificationResultsAdapter.notifyDataSetChanged()
        }
    }
}