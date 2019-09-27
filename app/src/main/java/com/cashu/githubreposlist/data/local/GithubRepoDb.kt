package com.cashu.githubreposlist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cashu.githubreposlist.data.model.GithubRepo

@Database(
        entities = [GithubRepo::class],
        version = 1,
        exportSchema = false
)
abstract class GithubRepoDb : RoomDatabase() {
    abstract fun repoDao(): GithubRepoDao
}