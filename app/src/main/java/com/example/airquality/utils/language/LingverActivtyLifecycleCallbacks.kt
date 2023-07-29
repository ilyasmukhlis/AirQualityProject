package com.example.airquality.utils.language

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * LingverActivityLifecycleCallbacks Class
 */

internal class LingverActivityLifecycleCallbacks(
    private val callback: (Activity) -> Unit
) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        callback.invoke(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        // no-op
    }

    override fun onActivityResumed(activity: Activity) {
        // no-op
    }

    override fun onActivityPaused(activity: Activity) {
        // no-op
    }

    override fun onActivityStopped(activity: Activity) {
        // no-op
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // no-op
    }

    override fun onActivityDestroyed(activity: Activity) {
        // no-op
    }
}