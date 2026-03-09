package com.hyunjine.reborn.core.di

import com.hyunjine.reborn.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel() }
}
