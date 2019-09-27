package com.cashu.githubreposlist.di

import com.cashu.githubreposlist.GithubRepoApp
import com.cashu.githubreposlist.data.DataModule
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class, ViewModelModule::class])
interface ApplicationComponent : AndroidInjector<GithubRepoApp> {

    @Component.Factory
    abstract class Builder : AndroidInjector.Factory<GithubRepoApp>
}