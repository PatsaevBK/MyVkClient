package com.example.myvkclient

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.myvkclient.di.ApplicationComponent
import com.example.myvkclient.di.DaggerApplicationComponent

class MyVkClientApp: Application() {

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    Log.d("COMPOSITION", "getApplicationComponent()")
    return (LocalContext.current.applicationContext as MyVkClientApp).applicationComponent
}