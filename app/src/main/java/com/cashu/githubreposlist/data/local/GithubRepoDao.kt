package com.cashu.githubreposlist.data.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cashu.githubreposlist.data.model.GithubRepo
import io.reactivex.Flowable

@Dao
interface GithubRepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReposToDB(repositories: List<GithubRepo>)

    @Query("SELECT * FROM githubrepo")
    fun load(): DataSource.Factory<Int, GithubRepo>

    @Query("SELECT * FROM githubrepo")
    fun getAllReposFromNetwork(): Flowable<List<GithubRepo>>

    @Query("DELETE FROM githubrepo")
    fun deleteAllRepos()
}
