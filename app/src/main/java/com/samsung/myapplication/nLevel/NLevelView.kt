package com.samsung.myapplication.nLevel

import android.view.View

/**
 * Created by sadra on 7/29/17.
 */
interface NLevelView {
    fun getView(item: NLevelItem?): View?
}