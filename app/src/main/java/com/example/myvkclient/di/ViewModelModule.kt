package com.example.myvkclient.di

import androidx.lifecycle.ViewModel
import com.example.myvkclient.presentation.main.MainViewModel
import com.example.myvkclient.presentation.news.NewsFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsFeedViewModel::class)
    fun bindNewsFeedViewModel(newsFeedViewModel: NewsFeedViewModel): ViewModel


}