package com.example.hotpepperapi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TopViewModel : ViewModel() {

    private val _etStore = MutableLiveData<String>()
    val etStore: LiveData<String>
        get() = _etStore

    init {
        _etStore.value = ""
    }

    fun checkParam(storeName: String) {
        _etStore.value = storeName
    }
}