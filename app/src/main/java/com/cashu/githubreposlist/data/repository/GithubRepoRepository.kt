package com.cashu.githubreposlist.data.repository

import android.annotation.SuppressLint
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.cashu.githubreposlist.data.local.GithubRepoDao
import com.cashu.githubreposlist.data.model.GithubRepo
import com.cashu.githubreposlist.data.model.NetworkState
import com.cashu.githubreposlist.data.model.NetworkState.Success
import com.cashu.githubreposlist.data.remote.GithubRepoApi
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepoRepository
@Inject
constructor(
        private val githubRepoApi: GithubRepoApi,
        private val githubRepoDao: GithubRepoDao
) {

    val networkStateRelay: BehaviorRelay<NetworkState> = BehaviorRelay.createDefault<NetworkState>(Success)

    /**
     * Replace old data with the new one if available
     */
    @SuppressLint("CheckResult")
    fun checkRefreshData() {
        githubRepoDao.getAllReposFromNetwork()
                .filter { it.isNotEmpty() }
                .toObservable()
                .flatMap { loadGithubReposFromNetwork(1) }
                .take(1)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    githubRepoDao.deleteAllRepos()
                }, {
                    networkStateRelay.accept(NetworkState.Failure("No network connection available"))
                })

    }

    /**
     * Get Repos from Network & save them in db
     */
    @SuppressLint("CheckResult")
    fun loadAndSaveRepos(page: Int) {
        networkStateRelay.accept(NetworkState.Loading)

        loadGithubReposFromNetwork(page)
                .flatMap<GithubRepo> { Observable.fromIterable(it) }
                .map {
                    it.page = page
                    return@map it
                }
                .toList()
                .doOnSuccess {
                    githubRepoDao.insertReposToDB(it)
                }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    networkStateRelay.accept(Success)
                }, {
                    networkStateRelay.accept(NetworkState.Failure("No network connection available"))
                })
    }

    private fun getRepoDataSource(): DataSource.Factory<Int, GithubRepo> {
        return githubRepoDao.load()
    }

    private fun loadGithubReposFromNetwork(page: Int): Observable<List<GithubRepo>> {
        return githubRepoApi.getGithubRepos(page, 15)
    }

    /**
     * Pagination
     */
    fun getGithubReposPagedList(): Flowable<PagedList<GithubRepo>> {
        return RxPagedListBuilder(
                getRepoDataSource(),
                PagedList.Config
                        .Builder()
                        .setPageSize(15)
                        .setPrefetchDistance(3)
                        .setEnablePlaceholders(false)
                        .build())
                .setBoundaryCallback(
                        object : PagedList.BoundaryCallback<GithubRepo>() {
                            override fun onZeroItemsLoaded() {
                                super.onZeroItemsLoaded()
                                loadAndSaveRepos(1)
                            }

                            override fun onItemAtEndLoaded(itemAtEnd: GithubRepo) {
                                super.onItemAtEndLoaded(itemAtEnd)
                                loadAndSaveRepos(itemAtEnd.page + 1)
                            }
                        })
                .buildFlowable(BackpressureStrategy.LATEST)
    }
}