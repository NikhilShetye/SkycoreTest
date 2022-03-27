package com.example.skycoretest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skycoretest.databinding.ItemUserInfoBinding
import com.example.skycoretest.dto.BusinessModel
import com.example.skycoretest.dto.Businesses
import com.example.skycoretest.extensions.loadProfilePhoto
import kotlin.math.roundToInt

class UserInfoListAdapter(private var list: List<Businesses>) :
    RecyclerView.Adapter<UserInfoListAdapter.UserInfoViewHolder>() {
    class UserInfoViewHolder(val binding: ItemUserInfoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoViewHolder {
        val binding = ItemUserInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserInfoViewHolder, position: Int) {
        holder.binding.tvName.text = list[position].name
        holder.binding.tvAddress.text = "${list[position].distance?.roundToInt().toString()}m ${list[position].location?.address1.toString()}"
        holder.binding.img.loadProfilePhoto(list[position].imageUrl)
        holder.binding.tvRating.text = list[position].rating.toString()
    }

    override fun getItemCount() = list.size


}
