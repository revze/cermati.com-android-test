package id.revan.githubuserlist.di

import dagger.Component
import id.revan.githubuserlist.di.modules.ApiModule
import id.revan.githubuserlist.di.modules.RepositoryModule
import id.revan.githubuserlist.di.modules.ViewModelFactoryModule
import id.revan.githubuserlist.ui.main.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, RepositoryModule::class, ViewModelFactoryModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}