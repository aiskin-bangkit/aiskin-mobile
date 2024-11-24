package com.capstone.aiskin.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.aiskin.core.data.dummy.DiseaseItem
import com.capstone.aiskin.databinding.ItemDiseaseBinding



class DiseaseAdapter(private val diseaseList: List<DiseaseItem>) :
    RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {

    inner class DiseaseViewHolder(private val binding: ItemDiseaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: DiseaseItem) {
            with(binding) {
                tvItemName.text = disease.name
                tvItemDescription.text = disease.description
                Glide.with(imgItemPhoto.context)
                    .load("https://www.wowkeren.com/display/images/photo/2023/04/10/00476771.jpg")
                    .into(imgItemPhoto)
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
