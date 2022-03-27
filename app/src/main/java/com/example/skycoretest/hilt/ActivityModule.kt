package com.artium.app.hilt

import android.app.Activity
import android.content.Context
import com.artium.app.base.BaseActivity
import com.artium.app.helper.BioMetricAuthHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun provideBioMetricAuthHelper(@ActivityContext context: Context): BioMetricAuthHelper {
        return BioMetricAuthHelper(context)
    }

    @Provides
    fun provideBaseActivity(activity: Activity): BaseActivity {
        return activity as BaseActivity
    }
}