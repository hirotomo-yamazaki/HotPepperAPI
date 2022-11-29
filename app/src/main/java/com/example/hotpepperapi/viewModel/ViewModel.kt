package com.example.hotpepperapi.viewModel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModel
import com.example.hotpepperapi.Constants
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

    private val _genreCode = MutableLiveData<String>()
    val genreCode: LiveData<String>
        get() = _genreCode

    private val _etKeyword = MutableLiveData<String>()
    val etKeyword: LiveData<String>
        get() = _etKeyword

    private val _storeList = MutableLiveData<MutableList<String>>()
    val storeList: LiveData<MutableList<String>>
        get() = _storeList

    private val _storeGenreList = MutableLiveData<MutableList<String>>()
    val storeGenreList: LiveData<MutableList<String>>
        get() = _storeGenreList

    private val _storeAddressList = MutableLiveData<MutableList<String>>()
    val storeAddressList: LiveData<MutableList<String>>
        get() = _storeAddressList

    private val _accessList = MutableLiveData<MutableList<String>>()
    val accessList: LiveData<MutableList<String>>
        get() = _accessList

    private val _latList = MutableLiveData<MutableList<Double>>()
    val latList: LiveData<MutableList<Double>>
        get() = _latList

    private val _lngList = MutableLiveData<MutableList<Double>>()
    val lngList: LiveData<MutableList<Double>>
        get() = _lngList

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int>
        get() = _position

    private val _lat = MutableLiveData<Double>()
    val lat: LiveData<Double>
        get() = _lat

    private val _lng = MutableLiveData<Double>()
    val lng: LiveData<Double>
        get() = _lng

    private val _storeLat = MutableLiveData<Double>()
    val storeLat: LiveData<Double>
        get() = _storeLat

    private val _storeLng = MutableLiveData<Double>()
    val storeLng: LiveData<Double>
        get() = _storeLng

    private val _storeName = MutableLiveData<String>()
    val storeName: LiveData<String>
        get() = _storeName

    init {
        _progressBarFlag.value = false
        _genreCode.value = ""
        _etKeyword.value = ""
        _storeList.value = mutableListOf()
        _storeGenreList.value = mutableListOf()
        _storeAddressList.value = mutableListOf()
        _position.value = 0
        _accessList.value = mutableListOf()
        _lat.value = 35.6809591
        _lng.value = 139.7673068
        _storeLat.value = 35.6809591
        _storeLng.value = 139.7673068
        _storeName.value = ""
        _latList.value = mutableListOf()
        _lngList.value = mutableListOf()
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

    fun setStoreName(storeName: String) {
        _storeName.value = storeName
    }

    fun setLatLng(latitude: Double, longitude: Double) {
        _lat.value = latitude
        _lng.value = longitude
    }

    fun setStoreLatLng(latitude: Double, longitude: Double) {
        _storeLat.value = latitude
        _storeLng.value = longitude
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
                }

                override fun onResponse(call: Call<StoreDetail>, response: Response<StoreDetail>) {
                    if (response.isSuccessful) {

                        _progressBarFlag.value = false

                        val list = response.body()!!
                        makeStoreNameList(list)
                    }
                    Log.i("Response", "Success!")
                }
            })
        }
    }

    /** 駅名、ジャンルから飲食店を検索 */
    fun getStoreList() {
        viewModelScope.launch {
            apiConnect()
        }
        Log.i("ViewModel", "getStoreList")
    }

    private suspend fun apiConnect() {
        val service = Constants.retrofit()

        _progressBarFlag.value = true
        withContext(Dispatchers.IO) {
            val listCall = service.getStoreList(
                Constants.API_KEY,
                genreCode.value.toString(),
                etKeyword.value.toString(),
                Constants.FORMAT
            )
            listCall.enqueue(object : Callback<StoreDetail> {
                override fun onFailure(call: Call<StoreDetail>, t: Throwable) {
                    Log.e("Error", t.message.toString())

                    _progressBarFlag.value = false
                }

                override fun onResponse(call: Call<StoreDetail>, response: Response<StoreDetail>) {
                    if (response.isSuccessful) {

                        _progressBarFlag.value = false

                        val list = response.body()!!
                        makeStoreNameList(list)
                    }
                    Log.i("Response", "Success!")
                }
            })
        }
    }

    private fun makeStoreNameList(list: StoreDetail) {
        val storeNameList = mutableListOf<String>()
        val storeGenreList = mutableListOf<String>()
        val storeAddressList = mutableListOf<String>()
        val logoImage = mutableListOf<String>()
        val accessList = mutableListOf<String>()
        val latList = mutableListOf<Double>()
        val lngList = mutableListOf<Double>()

        for (i in list.results.shop.indices) {
            storeNameList += list.results.shop[i].name
            storeGenreList += list.results.shop[i].genre.name
            storeAddressList += list.results.shop[i].address
            logoImage += list.results.shop[i].logo_image
            accessList += list.results.shop[i].access
            latList += list.results.shop[i].lat
            lngList += list.results.shop[i].lng
        }

        _storeList.value = storeNameList
        _storeGenreList.value = storeGenreList
        _storeAddressList.value = storeAddressList
        _accessList.value = accessList
        _latList.value = latList
        _lngList.value = lngList
        Log.i("ViewModel", storeList.value.toString())
    }
}