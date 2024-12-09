package com.capstone.aiskin.ui.detail.disease

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.aiskin.core.data.network.disease.response.DetailDiseaseResponse
import com.capstone.aiskin.core.data.network.disease.response.DiseaseResponse
import com.capstone.aiskin.core.data.network.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DiseaseViewModel : ViewModel() {
    private val _diseaseList = MutableLiveData<List<DiseaseResponse>>()
    val diseaseList: LiveData<List<DiseaseResponse>> = _diseaseList

    private val _isDiseaseListLoading = MutableLiveData<Boolean>()
    val isDiseaseListLoading: LiveData<Boolean> = _isDiseaseListLoading

    private val _diseaseListError = MutableLiveData<String>()
    val diseaseListError: LiveData<String> = _diseaseListError

    private val _disease = MutableLiveData<DetailDiseaseResponse>()
    val disease: LiveData<DetailDiseaseResponse> get() = _disease

    private val _isDiseaseLoading = MutableLiveData<Boolean>()

    private val _diseaseError = MutableLiveData<String>()

    fun fetchDiseases() {
        _isDiseaseListLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getAllDisease()
                _diseaseList.value = response
            } catch (e: Exception) {
                _diseaseListError.value = e.message
            } finally {
                _isDiseaseListLoading.value = false
            }
        }
    }

    fun fetchDiseaseById(id: String) {
        _isDiseaseLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getDiseaseById(id)
                _disease.postValue(response)
            } catch (e: Exception) {
                _diseaseError.postValue(e.message)
            } finally {
                _isDiseaseLoading.value = false
            }
        }
    }

}
