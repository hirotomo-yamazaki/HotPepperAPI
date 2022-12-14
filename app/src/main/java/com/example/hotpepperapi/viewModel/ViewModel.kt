package com.example.hotpepperapi.viewModel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModel
import com.example.hotpepperapi.Constants
import com.example.hotpepperapi.model.ForMap
import com.example.hotpepperapi.model.Shop
import com.example.hotpepperapi.model.StoreDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel : ViewModel() {

    private val _progressBarFlag = MutableLiveData<Boolean>()
    val progressBarFlag: LiveData<Boolean>
        get() = _progressBarFlag

    private val _apiFlag = MutableLiveData<Boolean>()
    val apiFlag: LiveData<Boolean>
        get() = _apiFlag

    private val _multipleFlag = MutableLiveData<Boolean>()
    val multipleFlag: LiveData<Boolean>
        get() = _multipleFlag

    private val _genreCode = MutableLiveData<String>()
    val genreCode: LiveData<String>
        get() = _genreCode

    private val _etKeyword = MutableLiveData<String>()
    val etKeyword: LiveData<String>
        get() = _etKeyword

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int>
        get() = _position

    private val _lat = MutableLiveData<Double?>()
    val lat: LiveData<Double?>
        get() = _lat

    private val _lng = MutableLiveData<Double?>()
    val lng: LiveData<Double?>
        get() = _lng

    private val _coupon = MutableLiveData<String>()
    val coupon: LiveData<String>
        get() = _coupon

    private val _freeDrink = MutableLiveData<String>()
    val freeDrink: LiveData<String>
        get() = _freeDrink

    private val _freeFood = MutableLiveData<String>()
    val freeFood: LiveData<String>
        get() = _freeFood

    private val _list = MutableLiveData<MutableList<Shop>>()
    val list: LiveData<MutableList<Shop>>
        get() = _list

    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    private val _forMap = MutableLiveData<ForMap?>()
    val forMap: LiveData<ForMap?>
        get() = _forMap

    init {
        _progressBarFlag.value = false
        _progressBarFlag.value = false
        _multipleFlag.value = false
        _genreCode.value = ""
        _etKeyword.value = ""
        _position.value = 0
        _lat.value = null
        _lng.value = null
        _coupon.value = "0"
        _freeDrink.value = "0"
        _freeFood.value = "0"
        _list.value = mutableListOf()
        _url.value = ""
        _forMap.value = null
    }

    fun setKeyword(keyword: String) {
        _etKeyword.value = keyword
    }

    fun setGenreCode(genreCode: String) {
        _genreCode.value = genreCode
    }

    fun setPosition(position: Int) {
        _position.value = position
    }

    fun setLatLng(latitude: Double?, longitude: Double?) {
        _lat.value = latitude
        _lng.value = longitude
    }

    fun setUrl(url: String) {
        _url.value = url
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

    fun setMultiple(flag: Boolean) {
        _multipleFlag.value = flag
    }

    fun setForMap(list: ForMap) {
        _forMap.value = list
    }

    /** ?????????????????????????????????????????? */
    fun getByLocation() {
        viewModelScope.launch {
            byLocation()
        }
    }

    private suspend fun byLocation() {
        val service = Constants.retrofit()

        _progressBarFlag.value = true
        _apiFlag.value = false
        _multipleFlag.value = true

        withContext(Dispatchers.IO) {
            val listCall = lat.value?.let { lat ->
                lng.value?.let { lng ->
                    service.byLocation(
                        Constants.API_KEY,
                        lat,
                        lng,
                        Constants.FORMAT
                    )
                }
            }
            listCall?.enqueue(object : Callback<StoreDetail> {
                override fun onFailure(call: Call<StoreDetail>, t: Throwable) {
                    Log.e("Error", t.message.toString())

                    _progressBarFlag.value = false
                    _apiFlag.value = true
                }

                override fun onResponse(call: Call<StoreDetail>, response: Response<StoreDetail>) {
                    if (response.isSuccessful) {

                        _progressBarFlag.value = false
                        _apiFlag.value = true

                        val list = response.body()!!
                        makeStoreNameList(list)
                    }
                    Log.i("Response", "Success!")
                }
            })
        }
    }

    /** ????????????????????????????????????????????? */
    fun getStoreList() {
        viewModelScope.launch {
            apiConnect()
        }
        Log.i("ViewModel", "getStoreList")
    }

    private suspend fun apiConnect() {
        val service = Constants.retrofit()

        _progressBarFlag.value = true
        _apiFlag.value = false
        _multipleFlag.value = false

        withContext(Dispatchers.IO) {
            val listCall =
                service.getStoreList(
                    Constants.API_KEY,
                    genreCode.value.toString(),
                    etKeyword.value.toString(),
                    coupon.value.toString(),
                    freeDrink.value.toString(),
                    freeFood.value.toString(),
                    Constants.FORMAT
                )

            listCall.enqueue(object : Callback<StoreDetail> {
                override fun onFailure(call: Call<StoreDetail>, t: Throwable) {
                    Log.e("Error", t.message.toString())

                    _progressBarFlag.value = false
                    _apiFlag.value = true
                }

                override fun onResponse(call: Call<StoreDetail>, response: Response<StoreDetail>) {
                    if (response.isSuccessful) {

                        _progressBarFlag.value = false
                        _apiFlag.value = true

                        val list = response.body()!!
                        makeStoreNameList(list)
                    }
                    Log.i("Response", "Success!")
                }
            })
        }
    }

    private fun makeStoreNameList(lists: StoreDetail) {
        if (lists.results.resultsAvailable == 0) {
            _list.value = mutableListOf()
        } else {
            //API????????????????????????????????????????????????????????????????????????
            _list.value = mutableListOf()

            for (i in lists.results.shop.indices) {
                _list.value?.plusAssign(lists.results.shop[i])
            }
            Log.i("ViewModel", list.value.toString())
        }
    }
}