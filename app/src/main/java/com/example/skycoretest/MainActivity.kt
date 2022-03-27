package com.example.skycoretest

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skycoretest.databinding.ActivityMainBinding
import com.example.skycoretest.dto.BusinessModel
import com.example.skycoretest.dto.Businesses
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var userInfoListAdapter: UserInfoListAdapter
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callApi(location = "NYC", term = "restaurants")
    }

    private fun callApi(location: String, term: String) {
        mainViewModel.getRepos(location, term)
            .observe(this) { repoList ->
                setUserInfoList(repoList.businesses)
            }
    }

    private fun setUserInfoList(list: List<Businesses>) {
        userInfoListAdapter = UserInfoListAdapter(list)
        binding.recyclerviewUserInfo.adapter = userInfoListAdapter
        binding.recyclerviewUserInfo.layoutManager = LinearLayoutManager(this@MainActivity)
    }

}