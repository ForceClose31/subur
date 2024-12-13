
package com.bangkit.subur.features.register.data

import com.bangkit.subur.features.register.data.remote.RegisterRemoteDataSource
import com.bangkit.subur.features.register.domain.model.RegisterRequest

class RegisterRepository(
    private val remoteDataSource: RegisterRemoteDataSource = RegisterRemoteDataSource()
) {
    suspend fun register(request: RegisterRequest): Result<Unit> {
        return remoteDataSource.register(request)
    }
}