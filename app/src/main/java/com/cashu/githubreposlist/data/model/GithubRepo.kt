package com.cashu.githubreposlist.data.model

import androidx.room.Entity

@Entity(
        primaryKeys = ["name"]
)
data class GithubRepo(
        val id: Int,
        val name: String,
        val description: String?,
        var page: Int
)
