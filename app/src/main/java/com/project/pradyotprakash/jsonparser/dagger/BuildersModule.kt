package com.project.pradyotprakash.jsonparser.dagger

import com.project.pradyotprakash.jsonparser.home.MainActivity
import com.project.pradyotprakash.jsonparser.home.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivity(): MainActivity
}