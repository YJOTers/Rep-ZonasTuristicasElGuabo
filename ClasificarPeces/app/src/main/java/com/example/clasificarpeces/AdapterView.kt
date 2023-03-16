package com.example.clasificarpeces

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications
import kotlin.math.min

class AdapterView:
    RecyclerView.Adapter<AdapterView.ViewHolder>() {

    private var categories: MutableList<Category?> = mutableListOf()
    private var adapterSize: Int = 0

    fun updateAdapterSize(size: Int) {
        adapterSize = size
    }

    fun updateResults(listClassifications: List<Classifications>?) {
        categories = MutableList(adapterSize) { null }
        listClassifications?.let { it ->
            if (it.isNotEmpty()) {
                val sortedCategories = it[0].categories.sortedBy { it?.index }
                val min = min(sortedCategories.size, categories.size)
                for (i in 0 until min) {
                    categories[i] = sortedCategories[i]
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.design_rows, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        categories[position].let {
            holder.bind(it?.label, it?.score)
        }
    }
    override fun getItemCount(): Int = categories.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val txtFila = view.findViewById<TextView>(R.id.txtFila)
        @SuppressLint("SetTextI18n")
        fun bind(label: String?, score: Float?){
            if(label != null && score != null){
                val probability = (String.format("%.2f", score).toFloat() * 100).toInt()
                txtFila.text = "$label ---> $probability %"
            }else{
                txtFila.text = "? ---> ?"
            }
        }
    }
}