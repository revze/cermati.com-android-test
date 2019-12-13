package id.revan.githubuserlist.di.modules

import dagger.Module
import dagger.Provides
import id.revan.githubuserlist.data.services.ApiServices
import id.revan.githubuserlist.helper.constants.Endpoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideApiServices(): ApiServices {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Endpoint.BASE_URL)
            .build().create(ApiServices::class.java)
    }
}