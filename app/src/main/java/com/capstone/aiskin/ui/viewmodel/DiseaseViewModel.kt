package com.capstone.aiskin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.aiskin.core.data.network.response.DiseaseResponseItem
import com.capstone.aiskin.core.data.network.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DiseaseViewModel : ViewModel() {
    private val _diseaseList = MutableLiveData<List<DiseaseResponseItem>>()
    val diseaseList: LiveData<List<DiseaseResponseItem>> = _diseaseList

    private val _isDiseaseListLoading = MutableLiveData<Boolean>()
    val isDiseaseListLoading: LiveData<Boolean> = _isDiseaseListLoading

    private val _diseaseListError = MutableLiveData<String>()
    val diseaseListError: LiveData<String> = _diseaseListError

    private val _disease = MutableLiveData<DiseaseResponseItem>()
    val disease: LiveData<DiseaseResponseItem> = _disease

    private val _isDiseaseLoading = MutableLiveData<Boolean>()
    val isDiseaseLoading: LiveData<Boolean> = _isDiseaseLoading

    private val _diseaseError = MutableLiveData<String>()
    val diseaseError: LiveData<String> = _diseaseError

    fun fetchDiseases() {
        _isDiseaseListLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getAllDisease()
                _diseaseList.value = response
            } catch (e: Exception) {
                _diseaseListError.value = e.message
                Log.e("DiseaseViewModel", "Error fetching diseases list: ${e.message}", e)
            } finally {
                _isDiseaseListLoading.value = false
            }
        }
    }

    fun fetchDiseaseById(id : String) {
        _isDiseaseLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getDiseaseById(id)
                _disease.value = response
            } catch (e: Exception) {
                _diseaseError.value = e.message
                Log.e("DiseaseViewModel", "Error fetching diseases : ${e.message}", e)
            } finally {
                _isDiseaseLoading.value = false
            }
        }
    }
}
