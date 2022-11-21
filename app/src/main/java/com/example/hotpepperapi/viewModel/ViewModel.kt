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

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int>
        get() = _position

    init {
        _genreCode.value = ""
        _etKeyword.value = ""
        _storeList.value = mutableListOf()
        _storeGenreList.value = mutableListOf()
        _storeAddressList.value = mutableListOf()
        _position.value = 0
        _accessList.value = mutableListOf()
    }

    fun setKeyword(keyword: String) {
        _etKeyword.value = keyword
    }

    fun setGenreCode(genreCode: String) {
        _genreCode.value = genreCode
    }

    fun setPosition(position:Int){
        _position.value = position
    }

    fun getStoreList() {
        viewModelScope.launch {
            apiConnect()
        }
        Log.i("ViewModel", "getStoreList")
    }

    private suspend fun apiConnect() {
        val service = Constants.retrofit()

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
                }

                override fun onResponse(call: Call<StoreDetail>, response: Response<StoreDetail>) {
                    if (response.isSuccessful) {

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

        for (i in list.results.shop.indices) {
            storeNameList += list.results.shop[i].name
            storeGenreList += list.results.shop[i].genre.name
            storeAddressList += list.results.shop[i].address
            logoImage += list.results.shop[i].logo_image
            accessList += list.results.shop[i].access
        }

        _storeList.value = storeNameList
        _storeGenreList.value = storeGenreList
        _storeAddressList.value = storeAddressList
        _accessList.value = accessList
        Log.i("ViewModel", storeList.value.toString())
    }
}