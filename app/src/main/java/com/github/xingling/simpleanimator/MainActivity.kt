package com.github.xingling.simpleanimator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import com.github.xingling.animatorlib.SimpleAnimator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        bt_click.setOnClickListener {
            SimpleAnimator.animate(tv_content)
                    .alpha(1f, 0f)
                    .andAnimate(bt_click)
                    .translationX(0f, -100f, 100f, 0f)
                    .singleInterpolator(LinearInterpolator())
                    .thenAnimate(tv_content)
                    .alpha(0f, 1f)
                    .start()
        }
    }
}
