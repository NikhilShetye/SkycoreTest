package com.example.nikhiltest

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.MalformedURLException
import java.net.URL

class MainViewModel() : ViewModel() {

    private val reposLiveData = MutableLiveData<List<RepoModel>>()
    private val userInfoLiveData = MutableLiveData<List<UserInfoEntity>>()

    fun getRepos(user: String): LiveData<List<RepoModel>> {
        val retrofit = Retrofit.Builder()
            .baseUrl(MainActivity.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(GitHubService::class.java)

        if (reposLiveData.value == null) {
            viewModelScope.launch(Dispatchers.IO) {
                val repos: Call<List<RepoModel>> = service.listRepos(user)
                reposLiveData.postValue(repos.execute().body())
            }
        }
        return reposLiveData
    }

    fun downloadPDF(fileName: String, context: Context) {
        try {
            val url = URL(MainActivity.PDF_URL)
            val request = DownloadManager.Request(Uri.parse(url.toString()))
            request.setTitle(fileName)
            request.setMimeType("application/pdf")
            request.setAllowedOverMetered(true)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                "SampleFiles/$fileName")
            val downloadManager =
                context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    fun saveUserInfo(db: AppDatabase,userInfoEntity: UserInfoEntity): MutableLiveData<List<UserInfoEntity>> {
        GlobalScope.launch {
            db.userInfoDAO().insertAll(userInfoEntity)
            val data = db.userInfoDAO().getAll()
            data.forEach {
                println(it)
            }
            val userInfoList= db.userInfoDAO().getAll()
            userInfoLiveData.postValue(userInfoList)
        }
        return userInfoLiveData
    }
}