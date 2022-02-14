package com.alexeyyuditsky.test.foundation.navigator

import com.alexeyyuditsky.test.foundation.views.BaseScreen

/** Navigation for application */
interface Navigator {

    /** Launch a new screen at the top of back stack. */
    fun launch(screen: BaseScreen)

    /** Go back to the previous screen and optionally send some results. */
    fun goBack(result: Any? = null)

    fun goToMain()

}