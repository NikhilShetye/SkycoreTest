package com.example.skycoretest

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artium.app.extensions.onException
import com.artium.app.extensions.onFailure
import com.artium.app.extensions.onSuccess
import com.artium.app.network.ApiService
import com.artium.app.network.Receptor
import com.example.skycoretest.dto.BusinessModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val artiumApp: SkycoreTestApp,
    private val userRepo: UserRepo
) : ViewModel() {

    private val reposLiveData = MutableLiveData<BusinessModel>()

    fun getRepos(location: String,term : String): LiveData<BusinessModel> {
        if (reposLiveData.value == null) {
            viewModelScope.launch(Dispatchers.IO) {
                userRepo.listRepos(location,term)
                    .onSuccess {
                        getResponse()?.let {
                            reposLiveData.postValue(it)
                        }
                    }.onFailure {
                        getErrorMsg()
                    }.onException {
                        getThrowable()?.message
                    }
            }
        }
        return reposLiveData
    }
}