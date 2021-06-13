package com.meltemtufekci.myproject.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.meltemtufekci.myproject.MyApp
import com.meltemtufekci.myproject.data.MainRepository
import com.meltemtufekci.myproject.data.User
import com.meltemtufekci.myproject.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(
    app: Application,
    private val mainRepository: MainRepository
) : AndroidViewModel(app) {

    val userRes: MutableLiveData<Resource<MutableList<User>>> = MutableLiveData()
    private var userResponse: MutableList<User>? = null

    fun getUser() = viewModelScope.launch {
        safeGetUserCall()
    }

    private suspend fun safeGetUserCall() {
        userRes.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = mainRepository.getUsers()
                userRes.postValue(handleGetUserResponse(response))
            } else {
                userRes.postValue(Resource.Error("İnternet bağlantısı hatası"))
            }
        } catch (e: Exception) {
            userRes.postValue(Resource.Error(e.message.toString()))
        }
    }

    private fun handleGetUserResponse(response: Response<MutableList<User>>): Resource<MutableList<User>> {
        if (response.isSuccessful) {
            response.body()?.let { users ->
                return Resource.Success(userResponse ?: users)
            }
        }
        return Resource.Error("Bilinmeyen bir hata oluştu:\n" + response.code().toString())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }
}