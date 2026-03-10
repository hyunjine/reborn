package com.hyunjine.reborn.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module


@Module(includes = [ViewModelModule::class])
@Configuration
class AppModule

@KoinApplication
object RebornAppKoin

@ComponentScan("com.hyunjine.reborn")
@Module
class ViewModelModule
