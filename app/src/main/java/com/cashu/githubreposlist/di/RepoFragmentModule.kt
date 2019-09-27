package com.cashu.githubreposlist.di

import androidx.fragment.app.Fragment
import com.cashu.githubreposlist.di.scopes.FragmentScope
import com.cashu.githubreposlist.ui.GithubRepoFragment
import dagger.Binds
import dagger.Module

@Module
abstract class RepoFragmentModule {

    @Binds
    @FragmentScope
    abstract fun fragment(githubRepoFragment: GithubRepoFragment): Fragment
}
