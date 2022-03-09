package com.example.dictionaryappakash.Dagger

import com.example.dictionaryappakash.fragments.HomeFragment
import com.example.dictionaryappakash.mainActivity
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

import javax.inject.Singleton

@Singleton
@Component(modules = [
    DictModule::class,
   AndroidInjectionModule :: class,
           InjectorModule::class
   ])

interface  DictComponent : AndroidInjector<DictApplication> {
   fun inject(target : mainActivity)
   fun inject(target : HomeFragment)

}
