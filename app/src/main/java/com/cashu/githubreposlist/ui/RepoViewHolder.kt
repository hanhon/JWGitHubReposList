package com.cashu.githubreposlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cashu.githubreposlist.data.model.GithubRepo
import com.cashu.jakesrepos.R

class RepoViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
                R.layout.item_github_repo,
                parent,
                false
        )) {

    private val name = itemView.findViewById<TextView>(R.id.tv_repo_name)
    private val description = itemView.findViewById<TextView>(R.id.tv_repo_description)

    fun bindTo(item: GithubRepo?) {
        item?.let {
            name.text = it.name
            description.text = it.description
        }

    }
}
