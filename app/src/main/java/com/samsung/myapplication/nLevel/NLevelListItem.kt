package com.samsung.myapplication.nLevel

import android.view.View

/**
 * Created by sadra on 7/29/17.
 */
interface NLevelListItem {
    val isExpanded: Boolean
    fun toggle()
    val parent: NLevelListItem?
    val view: View?
}