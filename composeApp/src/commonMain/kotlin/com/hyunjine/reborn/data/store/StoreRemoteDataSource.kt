package com.hyunjine.reborn.data.store

interface StoreRemoteDataSource {
    suspend fun getData(): String
}