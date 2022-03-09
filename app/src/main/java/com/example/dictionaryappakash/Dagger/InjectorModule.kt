package com.example.dictionaryappakash.Dagger

import com.example.dictionaryappakash.fragments.HomeFragment
import com.example.dictionaryappakash.mainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class InjectorModule {
    @ContributesAndroidInjector
    abstract  fun  contributeModeInjector() : HomeFragment
}