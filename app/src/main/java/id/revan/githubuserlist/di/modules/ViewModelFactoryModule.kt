package id.revan.githubuserlist.di.modules

import dagger.Module
import dagger.Provides
import id.revan.githubuserlist.ui.base.BaseViewModelFactory
import id.revan.githubuserlist.ui.main.MainViewModel
import javax.inject.Singleton

@Module
class ViewModelFactoryModule {

    @Singleton
    @Provides
    fun provideMainViewModelFactory(viewModel: MainViewModel): BaseViewModelFactory<MainViewModel> {
        return BaseViewModelFactory { viewModel }
    }
}