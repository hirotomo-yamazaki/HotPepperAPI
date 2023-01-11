package com.example.hotpepperapi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TopViewModel : ViewModel() {

    private val _keyword = MutableLiveData<String>()
    val keyword: LiveData<String>
        get() = _keyword

    private val _genreCode = MutableLiveData<String>()
    val genreCode: LiveData<String>
        get() = _genreCode

    private val _coupon = MutableLiveData<String>()
    val coupon: LiveData<String>
        get() = _coupon

    private val _freeDrink = MutableLiveData<String>()
    val freeDrink: LiveData<String>
        get() = _freeDrink

    private val _freeFood = MutableLiveData<String>()
    val freeFood: LiveData<String>
        get() = _freeFood

    init {
        _keyword.value = ""
        _genreCode.value = ""
        _coupon.value = "0"
        _freeDrink.value = "0"
        _freeFood.value = "0"
    }

    fun setKeyword(keyword: String){
        _keyword.value = keyword
    }

    fun setGenreCode(genreCode: String){
        _genreCode.value = genreCode
    }

    fun couponCheck(isChecked: Boolean) {
        if (isChecked) {
            _coupon.value = "1"
        } else {
            _coupon.value = "0"
        }
    }

    fun freeDrinkCheck(isChecked: Boolean) {
        if (isChecked) {
            _freeDrink.value = "1"
        } else {
            _freeDrink.value = "0"
        }
    }

    fun freeFoodCheck(isChecked: Boolean) {
        if (isChecked) {
            _freeFood.value = "1"
        } else {
            _freeFood.value = "0"
        }
    }
}