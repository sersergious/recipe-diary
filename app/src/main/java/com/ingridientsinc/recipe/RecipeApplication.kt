package com.ingridientsinc.recipe

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("ON APP START", "App Started")
    }
}
