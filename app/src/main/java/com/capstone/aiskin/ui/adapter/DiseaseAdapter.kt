package com.capstone.aiskin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.aiskin.core.data.network.response.DiseaseResponseItem
import com.capstone.aiskin.databinding.ItemDiseaseBinding

class DiseaseAdapter(
    private val diseaseList: List<DiseaseResponseItem?>,
    private val onItemClick: (String) -> Unit
    ) :
    RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {

    inner class DiseaseViewHolder(private val binding: ItemDiseaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: DiseaseResponseItem?) {
            with(binding) {
                tvItemName.text = disease?.name ?: "Unknown"
                tvItemDescription.text = disease?.description ?: "No description available"

                Glide.with(imgItemPhoto.context)
                    .load(disease?.image ?: "https://via.placeholder.com/150")
                    .into(imgItemPhoto)

                root.setOnClickListener {
                    if (disease != null) {
                        disease.id?.let { id -> onItemClick(id) }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val binding =
            ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiseaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        holder.bind(diseaseList[position])
    }

    override fun getItemCount(): Int = diseaseList.size
}
