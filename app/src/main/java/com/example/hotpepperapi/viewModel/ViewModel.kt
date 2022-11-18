package com.example.hotpepperapi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hotpepperapi.Constants
import com.example.hotpepperapi.model.StoreDetail
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

    init {
        _genreCode.value = ""
        _etKeyword.value = ""
        _storeList.value = mutableListOf()
    }

    fun setKeyword(keyword: String) {
        _etKeyword.value = keyword
    }

    fun setGenreCode(genreCode: String) {
        _genreCode.value = genreCode
    }

    fun getStoreList() {
        apiConnect()
        Log.i("ViewModel", "getStoreList")
    }

    private fun apiConnect() {
        val service = Constants.retrofit()

        val listCall = service.getStoreDetail(
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

    private fun makeStoreNameList(list: StoreDetail) {
        val storeNameList = mutableListOf<String>()
        for (i in list.results.shop.indices) {
            storeNameList += list.results.shop[i].name
        }

        _storeList.value = storeNameList
        Log.i("ViewModel", storeList.value.toString())
    }
}