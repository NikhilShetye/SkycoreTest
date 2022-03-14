package com.example.nikhiltest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nikhiltest.databinding.ItemRepoBinding
import com.example.nikhiltest.databinding.ItemUserInfoBinding

class UserInfoListAdapter(private var list: List<UserInfoEntity>) :
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
        holder.binding.tvName.text = list[position].userName
        holder.binding.tvMobile.text = list[position].userMobile
        holder.binding.tvBook.text = list[position].userBook
    }

    override fun getItemCount() = list.size


}
