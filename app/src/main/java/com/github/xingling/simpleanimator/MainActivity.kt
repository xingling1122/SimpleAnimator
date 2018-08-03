package com.github.xingling.simpleanimator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.LinearInterpolator
import com.github.xingling.animatorlib.SimpleAnimator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        bt_click.setOnClickListener {
            val simpleAnimator = SimpleAnimator.animate(loading_progress)
                    .alpha(0f, 1f, 0f, 1f, 0f, 1f, 0f, 1f)
                    .translationX(-200f, 0f, 200f, 0f, -200f, 0f, 200f, 0f)
                    .svga("loading.svga")
                    .duration(4000)
                    .interpolator(LinearInterpolator())
                    .start()
//            handler.postDelayed({
//                simpleAnimator.cancel()
//            }, 1000)
        }
    }
}
