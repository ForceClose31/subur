package com.bangkit.subur.features.login.di
import com.bangkit.subur.features.login.network.LoginApiService
import com.bangkit.subur.features.login.repository.LoginRepository
import com.bangkit.subur.features.login.viewmodel.LoginViewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val loginModule = module {
    single { get<Retrofit>().create(LoginApiService::class.java) }
    factory { LoginRepository(get()) }
    factory { LoginViewModel(get()) }
}