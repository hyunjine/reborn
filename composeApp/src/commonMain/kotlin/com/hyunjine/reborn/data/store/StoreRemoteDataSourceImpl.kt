package com.hyunjine.reborn.data.store

import org.koin.core.annotation.Single

@Single(binds = [StoreRemoteDataSource::class])
class StoreRemoteDataSourceImpl: StoreRemoteDataSource {
    override suspend fun getData(): String {
        return "hi"
    }
}