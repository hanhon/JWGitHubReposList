package com.cashu.githubreposlist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cashu.jakesrepos.R
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.activityMainFrameLayoutContainer, GithubRepoFragment.newInstance())
                    .commit()
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = fragmentInjector
}