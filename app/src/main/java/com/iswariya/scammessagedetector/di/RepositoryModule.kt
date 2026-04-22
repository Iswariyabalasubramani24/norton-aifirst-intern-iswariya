package com.iswariya.scammessagedetector.di

import com.iswariya.scammessagedetector.data.repository.ScanRepositoryImpl
import com.iswariya.scammessagedetector.domain.repository.ScanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindScanRepository(impl: ScanRepositoryImpl): ScanRepository
}
