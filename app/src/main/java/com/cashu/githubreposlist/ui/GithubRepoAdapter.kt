package com.cashu.githubreposlist.ui

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cashu.githubreposlist.data.model.GithubRepo
import com.cashu.jakesrepos.R

class GithubRepoAdapter(diffCallback: DiffUtil.ItemCallback<GithubRepo>) : PagedListAdapter<GithubRepo, RecyclerView.ViewHolder>(diffCallback) {

    private var loading = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_loading -> LoadingViewHolder(parent)
            R.layout.item_github_repo -> RepoViewHolder(parent)
            else -> throw IllegalArgumentException("error_view_type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_github_repo -> (holder as RepoViewHolder).bindTo(getItem(position))
            R.layout.item_loading -> (holder as LoadingViewHolder).bindTo()
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (loading) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (loading && position == itemCount - 1) {
            R.layout.item_loading
        } else {
            R.layout.item_github_repo
        }
    }

    fun showLoading() {
        loading = true
        notifyItemInserted(itemCount)
    }

    fun hideLoading() {
        loading = false
        notifyItemRemoved(itemCount)
    }
}