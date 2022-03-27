package com.artium.app.hilt

import android.app.Activity
import com.example.skycoretest.BaseActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun provideBaseActivity(activity: Activity): BaseActivity {
        return activity as BaseActivity
    }
}