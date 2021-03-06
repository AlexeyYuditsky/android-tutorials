package com.alexeyyuditsky.simplemvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alexeyyuditsky.foundation.ActivityScopeViewModel
import com.alexeyyuditsky.foundation.navigator.IntermediateNavigator
import com.alexeyyuditsky.foundation.navigator.StackFragmentNavigator
import com.alexeyyuditsky.foundation.uiactions.AndroidUIActions
import com.alexeyyuditsky.foundation.utils.viewModelCreator
import com.alexeyyuditsky.foundation.views.FragmentsHolder
import com.alexeyyuditsky.simplemvvm.view.currentcolor.CurrentColorFragment

/** This application is a single-activity app. MainActivity is a container for all screens. */
class MainActivity : AppCompatActivity(R.layout.activity_main), FragmentsHolder {

    private lateinit var navigator: StackFragmentNavigator

    private val activityViewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            uiActions = AndroidUIActions(applicationContext),
            navigator = IntermediateNavigator()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator = StackFragmentNavigator(
            activity = this,
            containerId = R.id.fragmentContainer,
            defaultTitle = getString(R.string.app_name),
            animations = StackFragmentNavigator.Animations(
                R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit,
            ),
            initialScreen = CurrentColorFragment.Screen()
        )
        navigator.onCreate(savedInstanceState = savedInstanceState)
    }

    override fun onDestroy() {
        navigator.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        // execute navigation actions only when activity is active
        activityViewModel.navigator.setTarget(navigator)
    }

    override fun onPause() {
        super.onPause()
        // postpone navigation actions if activity is not active
        activityViewModel.navigator.setTarget(null)
    }

    override fun notifyScreenUpdates() = navigator.notifyScreenUpdates()

    override fun getActivityScopeViewModel(): ActivityScopeViewModel = activityViewModel

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}