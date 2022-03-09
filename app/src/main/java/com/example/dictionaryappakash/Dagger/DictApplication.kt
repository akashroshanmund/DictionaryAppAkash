package com.example.dictionaryappakash.Dagger

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class DictApplication : Application(), HasAndroidInjector {




    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerDictComponent.builder().dictModule(DictModule(this))
           .build().inject(this)

    }

    override fun androidInjector(): AndroidInjector<Any> {
       return injector
    }





}
