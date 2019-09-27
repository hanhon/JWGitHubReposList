package com.cashu.githubreposlist.di

import android.app.Application
import com.cashu.githubreposlist.GithubRepoApp
import com.cashu.githubreposlist.di.scopes.ActivityScope
import com.cashu.githubreposlist.ui.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module(includes = [(AndroidInjectionModule::class)])
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun application(appGithub: GithubRepoApp): Application

    @ActivityScope
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    abstract fun mainActivity(): MainActivity
}
