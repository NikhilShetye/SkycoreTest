package com.example.nikhiltest

import com.google.gson.annotations.SerializedName

data class RepoModel (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("private") val private : Boolean,
	@SerializedName("description") val description : String,
	@SerializedName("url") val url : String,
	@SerializedName("size") val size : Int,
	@SerializedName("default_branch") val default_branch : String
)