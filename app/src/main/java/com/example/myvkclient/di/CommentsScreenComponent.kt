package com.example.myvkclient.di

import com.example.myvkclient.domain.entity.FeedPost
import com.example.myvkclient.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [CommentsViewModelModule::class]
)
interface CommentsScreenComponent {


    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance post: FeedPost
        ): CommentsScreenComponent
    }
}