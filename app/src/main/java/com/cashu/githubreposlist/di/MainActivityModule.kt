package com.cashu.githubreposlist.di

import android.app.Activity
import android.content.Context
import com.cashu.githubreposlist.di.scopes.ActivityScope
import com.cashu.githubreposlist.di.scopes.FragmentScope
import com.cashu.githubreposlist.ui.GithubRepoFragment
import com.cashu.githubreposlist.ui.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @Binds
    @ActivityScope
    abstract fun activityContext(activity: Activity): Context

    @Binds
    @ActivityScope
    abstract fun activity(mainActivity: MainActivity): Activity

    @FragmentScope
    @ContributesAndroidInjector(modules = [(RepoFragmentModule::class)])
    abstract fun repoFragment(): GithubRepoFragment
}
