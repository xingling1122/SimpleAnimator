package com.github.xingling.animatorlib

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import android.view.View
import android.view.animation.Interpolator

class SimpleAnimator {
    private val animationList = ArrayList<AnimatorBuilder>()
    private var duration: Long = DEFAULT_DURATION
    private var startDelay: Long = 0
    private var interpolator: Interpolator? = null
    private var repeatCount: Int = 0
    private var repeatMode = ValueAnimator.RESTART

    private var animatorSet: AnimatorSet? = null
    private var startListener: AnimatorListener.Start? = null
    private var stopListener: AnimatorListener.Stop? = null

    private var prev: SimpleAnimator? = null
    private var next: SimpleAnimator? = null

    companion object {
        val DEFAULT_DURATION: Long = 3000

        fun animate(vararg views: View): AnimatorBuilder {
            val simpleAnimator = SimpleAnimator()
            return simpleAnimator.addAnimatorBuilder(*views)
        }
    }

    @IntDef(flag = false, value = intArrayOf(ValueAnimator.RESTART, ValueAnimator.REVERSE))
    @Retention(AnnotationRetention.SOURCE)
    annotation class RepeatMode {

    }

    fun addAnimatorBuilder(vararg views: View): AnimatorBuilder {
        val animatorBuilder = AnimatorBuilder(this, *views)
        animationList.add(animatorBuilder)
        return animatorBuilder
    }

    fun thenAnimate(vararg views: View): AnimatorBuilder {
        val nextAnimator = SimpleAnimator()
        this.next = nextAnimator
        nextAnimator.prev = this
        return nextAnimator.addAnimatorBuilder(*views)
    }

    fun createAnimatorSet(): AnimatorSet {
        val animators = ArrayList<Animator>()
        for (animatorBuilder in animationList) {
            val animatorList = animatorBuilder.createAnimators()
            if (animatorBuilder.getSingleInterpolator() != null) {
                for (animator in animatorList) {
                    animator.interpolator = animatorBuilder.getSingleInterpolator()
                }
            }
            animators.addAll(animatorList)
        }
        for (animator in animators) {
            if (animator is ValueAnimator) {
                animator.repeatCount = repeatCount
                animator.repeatMode = repeatMode
            }
        }
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animators)
        animatorSet.duration = duration
        animatorSet.startDelay = startDelay
        if (interpolator != null) {
            animatorSet.interpolator = interpolator
        }
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                stopListener?.onStop()
                next?.prev = null
                next?.start()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
                startListener?.onStart()
            }

        })
        return animatorSet
    }

    fun start(): SimpleAnimator {
        if (prev != null) {
            prev?.start()
        } else {
            animatorSet = createAnimatorSet()
            animatorSet?.start()
        }
        return this
    }

    fun cancel() {
        animatorSet?.cancel()
        next?.cancel()
        next = null
    }

    fun duration(duration: Long): SimpleAnimator {
        this.duration = duration
        return this
    }

    fun startDelay(startDelay: Long): SimpleAnimator {
        this.startDelay = startDelay
        return this
    }

    /**
     * Repeat count of animation.
     *
     * @param repeatCount the repeat count
     * @return the view animation
     */
    fun repeatCount(@IntRange(from = -1) repeatCount: Int): SimpleAnimator {
        this.repeatCount = repeatCount
        return this
    }

    /**
     * Repeat mode view animation.
     *
     * @param repeatMode the repeat mode
     * @return the view animation
     */
    fun repeatMode(@RepeatMode repeatMode: Int): SimpleAnimator {
        this.repeatMode = repeatMode
        return this
    }

    fun onStart(startListener: AnimatorListener.Start): SimpleAnimator {
        this.startListener = startListener
        return this
    }

    fun onStop(stopListener: AnimatorListener.Stop): SimpleAnimator {
        this.stopListener = stopListener
        return this
    }

    /**
     * Interpolator view animator.
     *
     * @param interpolator the interpolator
     * @return the view animator
     * @link https://github.com/cimi-chen/EaseInterpolator
     */
    fun interpolator(interpolator: Interpolator): SimpleAnimator {
        this.interpolator = interpolator
        return this
    }

}