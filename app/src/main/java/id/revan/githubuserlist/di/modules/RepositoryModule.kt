package id.revan.githubuserlist.di.modules

import dagger.Module
import dagger.Provides
import id.revan.githubuserlist.data.repository.GithubRepository
import id.revan.githubuserlist.data.repository.GithubRepositoryImpl
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideGithubRepository(repository: GithubRepositoryImpl): GithubRepository {
        return repository
    }
}