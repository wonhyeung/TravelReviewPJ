package com.won.travelreviewpj.common

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle

class TravelApplication : Application() {
    companion object {
        private lateinit var instance: TravelApplication
        fun getTravelApplication(): TravelApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        screenSetup()
    }

    /**
     * Screen setup
     * 현재 화면을 수직 모드로 전부 설정한다
     */
    private fun screenSetup() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }
}


