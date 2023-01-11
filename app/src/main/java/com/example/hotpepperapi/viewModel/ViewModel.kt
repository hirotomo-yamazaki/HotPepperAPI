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

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int>
        get() = _position

    private val _lat = MutableLiveData<Double?>()
    val lat: LiveData<Double?>
        get() = _lat

    private val _lng = MutableLiveData<Double?>()
    val lng: LiveData<Double?>
        get() = _lng

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
        _position.value = 0
        _lat.value = null
        _lng.value = null
        _list.value = mutableListOf()
        _url.value = ""
        _forMap.value = null
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

    fun setMultiple(flag: Boolean) {
        _multipleFlag.value = flag
    }

    fun setForMap(list: ForMap) {
        _forMap.value = list
    }

    /** 位置情報から周辺飲食店を検索 */
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

    /** 駅名、ジャンルから飲食店を検索 */
    fun getStoreList(
        keyword: String,
        genreCode: String,
        coupon: String,
        freeDrink: String,
        freeFood: String
    ) {
        viewModelScope.launch {
            apiConnect(
                keyword,
                genreCode,
                coupon,
                freeDrink,
                freeFood
            )
        }
        Log.i("ViewModel", "getStoreList")
    }

    private suspend fun apiConnect(
        keyword: String,
        genreCode: String,
        coupon: String,
        freeDrink: String,
        freeFood: String
    ) {
        val service = Constants.retrofit()

        _progressBarFlag.value = true
        _apiFlag.value = false
        _multipleFlag.value = false

        withContext(Dispatchers.IO) {
            val listCall =
                service.getStoreList(
                    Constants.API_KEY,
                    genreCode,
                    keyword,
                    coupon,
                    freeDrink,
                    freeFood,
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
            //APIから返っていくるデータがあれば、元のデータを削除
            _list.value = mutableListOf()

            for (i in lists.results.shop.indices) {
                _list.value?.plusAssign(lists.results.shop[i])
            }
            Log.i("ViewModel", list.value.toString())
        }
    }
}