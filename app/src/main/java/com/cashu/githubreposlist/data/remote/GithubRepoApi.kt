package com.cashu.githubreposlist.data.remote

import com.cashu.githubreposlist.data.model.GithubRepo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubRepoApi {
    @GET("users/JakeWharton/repos")
    fun getGithubRepos(
            @Query("page") page: Int,
            @Query("per_page") per_page: Int
    ): Observable<List<GithubRepo>>
}
