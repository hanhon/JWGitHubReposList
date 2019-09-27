package com.cashu.githubreposlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cashu.jakesrepos.R

class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater
                .from(parent.context)
                .inflate(
                        R.layout.item_loading,
                        parent,
                        false)) {
    fun bindTo() {
    }
}
