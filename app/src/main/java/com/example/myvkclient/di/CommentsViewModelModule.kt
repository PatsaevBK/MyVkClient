package com.example.myvkclient.di

import androidx.lifecycle.ViewModel
import com.example.myvkclient.presentation.comments.CommentsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CommentsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CommentsViewModel::class)
    fun bindCommentsViewModel(commentsViewModel: CommentsViewModel): ViewModel
}