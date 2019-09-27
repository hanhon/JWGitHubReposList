package com.cashu.githubreposlist.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cashu.githubreposlist.data.model.GithubRepo
import com.cashu.githubreposlist.data.model.NetworkState
import com.cashu.jakesrepos.R
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GithubRepoFragment : Fragment(), HasAndroidInjector {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    private val disposable = CompositeDisposable()

    private lateinit var githubRepoViewModel: GithubRepoViewModel

    private lateinit var githubRepoAdapter: GithubRepoAdapter

    private lateinit var recylerViewRepos: RecyclerView

    private val diffCallback = object : DiffUtil.ItemCallback<GithubRepo>() {
        override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo) = oldItem == newItem
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        githubRepoAdapter = GithubRepoAdapter(diffCallback)
        recylerViewRepos.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recylerViewRepos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recylerViewRepos.adapter = githubRepoAdapter

        githubRepoViewModel = ViewModelProviders.of(this, viewModelFactory).get(GithubRepoViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_github_repo, container, false)
        recylerViewRepos = view.findViewById(R.id.rv_repos)
        return view
    }

    override fun onStart() {
        super.onStart()

        disposable.addAll(
                githubRepoViewModel.allGithubRepos
                        .subscribe {
                            githubRepoAdapter.submitList(it)
                        }
                ,
                githubRepoViewModel.networkState
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            when (it) {
                                is NetworkState.Success -> {
                                    githubRepoAdapter.hideLoading()
                                }
                                is NetworkState.Failure -> {
                                    githubRepoAdapter.hideLoading()
                                    Snackbar.make(recylerViewRepos, it.message, Snackbar.LENGTH_LONG).show()
                                }
                                is NetworkState.Loading -> {
                                    githubRepoAdapter.showLoading()
                                }
                            }
                        }
        )

        githubRepoViewModel.checkReposAreRefresh()
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return injector
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        if (context != null) {
            super.onAttach(context)
        }
    }

    companion object {
        fun newInstance(): GithubRepoFragment = GithubRepoFragment()
    }
}