package com.project.pradyotprakash.jsonparser.dagger

import android.app.Application
import com.project.pradyotprakash.jsonparser.ApplicationManager
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AndroidInjectionModule::class, BuildersModule::class, AppModule::class]
)
interface AppComponent: AndroidInjector<DaggerApplication> {
    fun inject(application: ApplicationManager)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}