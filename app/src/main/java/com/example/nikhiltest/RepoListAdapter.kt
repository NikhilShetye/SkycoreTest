package com.example.nikhiltest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nikhiltest.databinding.ItemRepoBinding

class RepoListAdapter(private var list: List<RepoModel>) :
    RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {
    class RepoViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemRepoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.binding.name.text = list[position].name
    }

    override fun getItemCount() = list.size


}
