package com.cashu.githubreposlist.ui

import androidx.lifecycle.ViewModel
import com.cashu.githubreposlist.data.repository.GithubRepoRepository
import javax.inject.Inject

class GithubRepoViewModel
@Inject
constructor(private val githubRepoRepository: GithubRepoRepository) : ViewModel() {

    val allGithubRepos = githubRepoRepository.getGithubReposPagedList()

    val networkState = githubRepoRepository.networkStateRelay

    fun checkReposAreRefresh() {
        githubRepoRepository.checkRefreshData()
    }
}