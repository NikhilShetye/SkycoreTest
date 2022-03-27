package com.example.skycoretest

import com.google.gson.annotations.SerializedName


data class BusinessModel (

  @SerializedName("businesses" ) var businesses : ArrayList<Businesses> = arrayListOf(),
  @SerializedName("total"      ) var total      : Int?                  = null,
  @SerializedName("region"     ) var region     : Region?               = Region()

)