package com.alexeyyuditsky.test.foundation.sideeffects.navigator

import com.alexeyyuditsky.test.foundation.sideeffects.navigator.plugin.NavigatorPlugin
import com.alexeyyuditsky.test.foundation.views.BaseScreen

/**
 * Side-effects interface for doing navigation from view-models.
 * You need to add [NavigatorPlugin] to your activity before using this feature.
 */
interface Navigator {

    /**
     * Launch a new screen at the top of back stack.
     */
    fun launch(screen: BaseScreen)

    /**
     * Go back to the previous screen and optionally send some results.
     */
    fun goBack(result: Any? = null)

    fun goToMainScreen(result: Any? = null)

}