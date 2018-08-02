package com.github.xingling.animatorlib

import android.view.View

class AnimatorListener {
    interface Start {
        fun onStart()
    }

    interface Stop {
        fun onStop()
    }

    interface Update<V : View> {
        fun update(view: V, value: Float)
    }
}