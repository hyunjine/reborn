package com.hyunjine.reborn.core.di

import com.hyunjine.reborn.ui.home.HomeViewModel
import com.hyunjine.reborn.ui.my.MyViewModel
import com.hyunjine.reborn.ui.regist_store.RegistStoreViewModel
import com.hyunjine.reborn.ui.store_detail.StoreDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel() }
    viewModel { params -> StoreDetailViewModel(storeId = params.get()) }
    viewModel { RegistStoreViewModel() }
    viewModel { MyViewModel() }
}
