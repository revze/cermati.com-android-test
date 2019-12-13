package id.revan.githubuserlist.di

object Injector {
    fun getApp(): AppComponent = DaggerAppComponent.builder().build()
}