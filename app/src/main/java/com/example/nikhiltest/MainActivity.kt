package com.example.nikhiltest

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.nikhiltest.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val BASE_URL = "https://api.github.com/"
        const val PDF_URL: String = "http://www.africau.edu/images/default/sample.pdf"
        const val USER: String = "nikhilshetyemobicule"
    }

    private lateinit var repoListAdapter: RepoListAdapter
    private val fileName: String = "Pdf_${Date()}.pdf"
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnDownloadPdf.setOnClickListener { downloadPdf(fileName, this@MainActivity) }
        binding.btnApiCall.setOnClickListener { callApi() }
        binding.btnSaveUser.setOnClickListener {
            validateUserData()
        }
        binding.spinnerBooks.setSelection(0)
    }

    private fun validateUserData() {
        var isValid  =true
        if(binding.tvUsername.text.trim().isEmpty()) {
            binding.tvUsername.error = getString(R.string.user_name_valid_message)
            isValid = false
        }
        if(binding.tvUsermobile.text.trim().isEmpty()) {
            binding.tvUsermobile.error = getString(R.string.user_mobile_valid_message)
            isValid = false
        }
        if(binding.spinnerBooks.selectedItem.toString() == "SELECT_BOOK"){
            Toast.makeText(this, "Please select Book", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if(isValid){
            saveUserInfo(
                UserInfoEntity(0,
                    binding.tvUsername.text.trim().toString(),
                    binding.tvUsermobile.text.trim().toString(),
                    binding.spinnerBooks.selectedItem.toString()
                )
            )
        }
    }

    private fun downloadPdf(fileName: String, context: Context) {
        mainViewModel.downloadPDF(fileName, context)
    }

    private fun callApi() {
        mainViewModel.getRepos(USER)
            .observe(this) { repoList ->
                setRepoList(repoList)
            }
    }

    private fun saveUserInfo(userInfoEntity: UserInfoEntity) {
        mainViewModel.saveUserInfo(getDatabase(), userInfoEntity)
    }

    private fun setRepoList(list: List<RepoModel>) {
        repoListAdapter = RepoListAdapter(list)
        binding.recyclerview.adapter = repoListAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun getDatabase(): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "user-info.db"
        ).build()
    }
}