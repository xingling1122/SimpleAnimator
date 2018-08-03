package com.github.xingling.animatorlib

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Path
import android.graphics.PathMeasure
import android.support.annotation.IntRange
import android.support.v4.view.ViewCompat
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.TextView
import com.opensource.svgaplayer.SVGAImageView
import java.util.ArrayList

class AnimatorBuilder(animator: SimpleAnimator, vararg views: View) {
    private val viewAnimator: SimpleAnimator
    private val views: Array<View>
    private val animatorList = ArrayList<Animator>()
    private var nextValueWillBeDp = false
    private var singleInterpolator: Interpolator? = null

    init {
        this.viewAnimator = animator
        this.views = views as Array<View>
    }

    /**
     * Dp animation builder.
     *
     * @return the animation builder
     */
    fun dp(): AnimatorBuilder {
        nextValueWillBeDp = true
        return this
    }

    /**
     * Add animation builder.
     *
     * @param animator the animator
     * @return the animation builder
     */
    protected fun add(animator: Animator): AnimatorBuilder {
        this.animatorList.add(animator)
        return this
    }

    /**
     * To dp float.
     *
     * @param px the px
     * @return the float
     */
    protected fun toDp(px: Float): Float {
        return px / views[0].context.resources.displayMetrics.density
    }

    /**
     * To px float.
     *
     * @param dp the dp
     * @return the float
     */
    protected fun toPx(dp: Float): Float {
        return dp * views[0].context.resources.displayMetrics.density
    }

    /**
     * Get values float [ ].
     *
     * @param values the values
     * @return the float [ ]
     */
    protected fun getValues(vararg values: Float): FloatArray {
        if (!nextValueWillBeDp) {
            return values
        }
        val pxValues = FloatArray(values.size)
        for (i in values.indices) {
            pxValues[i] = toPx(values[i])
        }
        return pxValues
    }

    /**
     * Property animation builder.
     *
     * @param propertyName the property name
     * @param values       the values
     * @return the animation builder
     */
    fun property(propertyName: String, vararg values: Float): AnimatorBuilder {
        for (view in views) {
            this.animatorList.add(ObjectAnimator.ofFloat(view, propertyName, *getValues(*values)))
        }
        return this
    }

    /**
     * Translation y animation builder.
     *
     * @param y the y
     * @return the animation builder
     */
    fun translationY(vararg y: Float): AnimatorBuilder {
        return property("translationY", *y)
    }

    /**
     * Translation x animation builder.
     *
     * @param x the x
     * @return the animation builder
     */
    fun translationX(vararg x: Float): AnimatorBuilder {
        return property("translationX", *x)
    }

    /**
     * Alpha animation builder.
     *
     * @param alpha the alpha
     * @return the animation builder
     */
    fun alpha(vararg alpha: Float): AnimatorBuilder {
        return property("alpha", *alpha)
    }

    /**
     * Scale x animation builder.
     *
     * @param scaleX the scale x
     * @return the animation builder
     */
    fun scaleX(vararg scaleX: Float): AnimatorBuilder {
        return property("scaleX", *scaleX)
    }

    /**
     * Scale y animation builder.
     *
     * @param scaleY the scale y
     * @return the animation builder
     */
    fun scaleY(vararg scaleY: Float): AnimatorBuilder {
        return property("scaleY", *scaleY)
    }

    /**
     * Scale animation builder.
     *
     * @param scale the scale
     * @return the animation builder
     */
    fun scale(vararg scale: Float): AnimatorBuilder {
        scaleX(*scale)
        scaleY(*scale)
        return this
    }

    /**
     * Pivot x animation builder.
     *
     * @param pivotX the pivot x
     * @return the animation builder
     */
    fun pivotX(pivotX: Float): AnimatorBuilder {
        for (view in views) {
            ViewCompat.setPivotX(view, pivotX)
        }
        return this
    }

    /**
     * Pivot y animation builder.
     *
     * @param pivotY the pivot y
     * @return the animation builder
     */
    fun pivotY(pivotY: Float): AnimatorBuilder {
        for (view in views) {
            ViewCompat.setPivotY(view, pivotY)
        }
        return this
    }

    /**
     * Rotation x animation builder.
     *
     * @param pivotX the rotation x
     * @return the animation builder
     */
    fun pivotX(vararg pivotX: Float): AnimatorBuilder {
        ObjectAnimator.ofFloat(getView(), "pivotX", *getValues(*pivotX))
        return this
    }

    fun pivotY(vararg pivotY: Float): AnimatorBuilder {
        ObjectAnimator.ofFloat(getView(), "pivotY", *getValues(*pivotY))
        return this
    }

    fun rotationX(vararg rotationX: Float): AnimatorBuilder {
        return property("rotationX", *rotationX)
    }

    /**
     * Rotation y animation builder.
     *
     * @param rotationY the rotation y
     * @return the animation builder
     */
    fun rotationY(vararg rotationY: Float): AnimatorBuilder {
        return property("rotationY", *rotationY)
    }

    /**
     * Rotation animation builder.
     *
     * @param rotation the rotation
     * @return the animation builder
     */
    fun rotation(vararg rotation: Float): AnimatorBuilder {
        return property("rotation", *rotation)
    }

    /**
     * Background color animation builder.
     *
     * @param colors the colors
     * @return the animation builder
     */
    fun backgroundColor(vararg colors: Int): AnimatorBuilder {
        for (view in views) {
            val objectAnimator = ObjectAnimator.ofInt(view, "backgroundColor", *colors)
            objectAnimator.setEvaluator(ArgbEvaluator())
            this.animatorList.add(objectAnimator)
        }
        return this
    }

    /**
     * Text color animation builder.
     *
     * @param colors the colors
     * @return the animation builder
     */
    fun textColor(vararg colors: Int): AnimatorBuilder {
        for (view in views) {
            if (view is TextView) {
                val objectAnimator = ObjectAnimator.ofInt(view, "textColor", *colors)
                objectAnimator.setEvaluator(ArgbEvaluator())
                this.animatorList.add(objectAnimator)
            }
        }
        return this
    }

    /**
     * Custom animation builder.
     *
     * @param update the update
     * @param values the values
     * @return the animation builder
     */
    fun custom(update: AnimatorListener.Update<View>?, vararg values: Float): AnimatorBuilder {
        for (view in views) {
            val valueAnimator = ValueAnimator.ofFloat(*getValues(*values))
            valueAnimator.addUpdateListener { animation -> update?.update(view, animation.animatedValue as Float) }
            add(valueAnimator)
        }
        return this
    }

    /**
     * Height animation builder.
     *
     * @param height the height
     * @return the animation builder
     */
    fun height(vararg height: Float): AnimatorBuilder {
        return custom(object : AnimatorListener.Update<View> {
            override fun update(view: View, value: Float) {
                view.layoutParams.height = value.toInt()
                view.requestLayout()
            }
        }, *height)
    }

    /**
     * Width animation builder.
     *
     * @param width the width
     * @return the animation builder
     */
    fun width(vararg width: Float): AnimatorBuilder {
        return custom(object : AnimatorListener.Update<View> {
            override fun update(view: View, value: Float) {
                view.layoutParams.width = value.toInt()
                view.requestLayout()
            }
        }, *width)
    }


    /**
     * Create animators list.
     *
     * @return the list
     */
    fun createAnimators(): List<Animator> {
        return animatorList
    }

    /**
     * And animate animation builder.
     *
     * @param views the views
     * @return the animation builder
     */
    fun andAnimate(vararg views: View): AnimatorBuilder {
        return viewAnimator.addAnimatorBuilder(*views)
    }

    /**
     * Then animate animation builder.
     *
     * @param views the views
     * @return the animation builder
     */
    fun thenAnimate(vararg views: View): AnimatorBuilder {
        return viewAnimator.thenAnimate(*views)
    }

    /**
     * Duration view animator.
     *
     * @param duration the duration
     * @return the animation builder
     */
    fun duration(duration: Long): AnimatorBuilder {
        viewAnimator.duration(duration)
        return this
    }

    /**
     * Start delay view animator.
     *
     * @param startDelay the start delay
     * @return the animation builder
     */
    fun startDelay(startDelay: Long): AnimatorBuilder {
        viewAnimator.startDelay(startDelay)
        return this
    }

    /**
     * Repeat count of animation.
     *
     * @param repeatCount the repeat count
     * @return the animation builder
     */
    fun repeatCount(@IntRange(from = -1) repeatCount: Int): AnimatorBuilder {
        viewAnimator.repeatCount(repeatCount)
        return this
    }

    /**
     * Repeat mode view animation.
     *
     * @param repeatMode the repeat mode
     * @return the animation builder
     */
    fun repeatMode(@RepeatMode repeatMode: Int): AnimatorBuilder {
        viewAnimator.repeatMode(repeatMode)
        return this
    }

    /**
     * On start view animator.
     *
     * @param startListener the start listener
     * @return the animation builder
     */
    fun onStart(startListener: AnimatorListener.Start): AnimatorBuilder {
        viewAnimator.onStart(startListener)
        return this
    }

    /**
     * On stop view animator.
     *
     * @param stopListener the stop listener
     * @return the animation builder
     */
    fun onStop(stopListener: AnimatorListener.Stop): AnimatorBuilder {
        viewAnimator.onStop(stopListener)
        return this
    }

    /**
     * Interpolator view animator.
     *
     * @param interpolator the interpolator
     * @return the animation builder
     */
    fun interpolator(interpolator: Interpolator): AnimatorBuilder {
        viewAnimator.interpolator(interpolator)
        return this
    }

    fun singleInterpolator(interpolator: Interpolator): AnimatorBuilder {
        singleInterpolator = interpolator
        return this
    }

    fun getSingleInterpolator(): Interpolator? {
        return singleInterpolator
    }

    fun accelerate(): SimpleAnimator {
        return viewAnimator.interpolator(AccelerateInterpolator())
    }

    fun decelerate(): SimpleAnimator {
        return viewAnimator.interpolator(DecelerateInterpolator())
    }

    /**
     * Start.
     */
    fun start(): SimpleAnimator {
        viewAnimator.start()
        return viewAnimator
    }

    /**
     * Get views view [ ].
     *
     * @return the view [ ]
     */
    fun getViews(): Array<View> {
        return views
    }

    /**
     * Gets view.
     *
     * @return the view
     */
    fun getView(): View {
        return views[0]
    }

    fun bounce(): AnimatorBuilder {
        return translationY(0f, 0f, -30f, 0f, -15f, 0f, 0f)
    }

    fun bounceIn(): AnimatorBuilder {
        alpha(0f, 1f, 1f, 1f)
        scaleX(0.3f, 1.05f, 0.9f, 1f)
        scaleY(0.3f, 1.05f, 0.9f, 1f)
        return this
    }

    fun bounceOut(): AnimatorBuilder {
        scaleY(1f, 0.9f, 1.05f, 0.3f)
        scaleX(1f, 0.9f, 1.05f, 0.3f)
        alpha(1f, 1f, 1f, 0f)
        return this
    }

    fun fadeIn(): AnimatorBuilder {
        return alpha(0f, 0.25f, 0.5f, 0.75f, 1f)
    }

    fun fadeOut(): AnimatorBuilder {
        return alpha(1f, 0.75f, 0.5f, 0.25f, 0f)
    }

    fun flash(): AnimatorBuilder {
        return alpha(1f, 0f, 1f, 0f, 1f)
    }

    fun flipHorizontal(): AnimatorBuilder {
        return rotationX(90f, -15f, 15f, 0f)
    }

    fun flipVertical(): AnimatorBuilder {
        return rotationY(90f, -15f, 15f, 0f)
    }

    fun pulse(): AnimatorBuilder {
        scaleY(1f, 1.1f, 1f)
        scaleX(1f, 1.1f, 1f)
        return this
    }

    /**
     * Only support single view
     *
     * @return the animation builder
     */
    fun rollIn(): AnimatorBuilder {
        for (view in views) {
            alpha(0f, 1f)
            translationX((-(view.width - view.paddingLeft - view.paddingRight)).toFloat(), 0f)
            rotation(-120f, 0f)
        }
        return this
    }

    fun rollOut(): AnimatorBuilder {
        for (view in views) {
            alpha(1f, 0f)
            translationX(0f, view.width.toFloat())
            rotation(0f, 120f)
        }
        return this
    }

    fun rubber(): AnimatorBuilder {
        scaleX(1f, 1.25f, 0.75f, 1.15f, 1f)
        scaleY(1f, 0.75f, 1.25f, 0.85f, 1f)
        return this
    }

    fun shake(): AnimatorBuilder {
        translationX(0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
        interpolator(CycleInterpolator(5f))
        return this
    }

    /**
     * @return the animation builder
     */
    fun standUp(): AnimatorBuilder {
        for (view in views) {
            val x = ((view.width - view.paddingLeft - view.paddingRight) / 2 + view.paddingLeft).toFloat()
            val y = (view.height - view.paddingBottom).toFloat()
            pivotX(x, x, x, x, x)
            pivotY(y, y, y, y, y)
            rotationX(55f, -30f, 15f, -15f, 0f)
        }
        return this
    }

    fun swing(): AnimatorBuilder {
        return rotation(0f, 10f, -10f, 6f, -6f, 3f, -3f, 0f)
    }

    /**
     * Tada
     *
     * @return the animation builder
     */
    fun tada(): AnimatorBuilder {
        scaleX(1f, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1f)
        scaleY(1f, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1f)
        rotation(0f, -3f, -3f, 3f, -3f, 3f, -3f, 3f, -3f, 0f)
        return this
    }

    fun wave(): AnimatorBuilder {
        for (view in views) {
            val x = ((view.width - view.paddingLeft - view.paddingRight) / 2 + view.paddingLeft).toFloat()
            val y = (view.height - view.paddingBottom).toFloat()
            rotation(12f, -12f, 3f, -3f, 0f)
            pivotX(x, x, x, x, x)
            pivotY(y, y, y, y, y)
        }
        return this
    }

    fun wobble(): AnimatorBuilder {
        for (view in views) {
            val width = view.width.toFloat()
            val one = (width / 100.0).toFloat()
            translationX(0 * one, -25 * one, 20 * one, -15 * one, 10 * one, -5 * one, 0 * one, 0f)
            rotation(0f, -5f, 3f, -3f, 2f, -1f, 0f)
        }
        return this
    }

    fun zoomIn(): AnimatorBuilder {
        scaleX(0.45f, 1f)
        scaleY(0.45f, 1f)
        alpha(0f, 1f)
        return this
    }

    fun zoomOut(): AnimatorBuilder {
        scaleX(1f, 0.3f, 0f)
        scaleY(1f, 0.3f, 0f)
        alpha(1f, 0f, 0f)
        return this
    }

    /**
     * 大转盘。以下几个特效参见：https://github.com/sd6352051/NiftyDialogEffects
     *
     * @return the animation builder
     */
    fun fall(): AnimatorBuilder {
        rotation(1080f, 720f, 360f, 0f)
        return this
    }

    fun newsPaper(): AnimatorBuilder {
        alpha(0f, 1f)
        scaleX(0.1f, 0.5f, 1f)
        scaleY(0.1f, 0.5f, 1f)
        return this
    }

    fun slit(): AnimatorBuilder {
        rotationY(90f, 88f, 88f, 45f, 0f)
        alpha(0f, 0.4f, 0.8f, 1f)
        scaleX(0f, 0.5f, 0.9f, 0.9f, 1f)
        scaleY(0f, 0.5f, 0.9f, 0.9f, 1f)
        return this
    }

    fun slideLeft(): AnimatorBuilder {
        translationX(-300f, 0f)
        alpha(0f, 1f)
        return this
    }

    fun slideRight(): AnimatorBuilder {
        translationX(300f, 0f)
        alpha(0f, 1f)
        return this
    }

    fun slideTop(): AnimatorBuilder {
        translationY(-300f, 0f)
        alpha(0f, 1f)
        return this
    }

    fun slideBottom(): AnimatorBuilder {
        translationY(300f, 0f)
        alpha(0f, 1f)
        return this
    }

    /**
     * 按指定路径运动
     *
     * @param path the path
     * @return the animation builder
     * @link http://blog.csdn.net/tianjian4592/article/details/47067161
     */
    fun path(path: Path?): AnimatorBuilder {
        if (path == null) {
            return this
        }
        val pathMeasure = PathMeasure(path, false)
        return custom(object : AnimatorListener.Update<View> {
            override fun update(view: View, value: Float) {
                val currentPosition = FloatArray(2)// 当前点坐标
                pathMeasure.getPosTan(value, currentPosition, null)
                val x = currentPosition[0]
                val y = currentPosition[1]
                ViewCompat.setX(view, x)
                ViewCompat.setY(view, y)
                Log.d(null, "path: value=$value, x=$x, y=$y")
            }
        }, 0F, pathMeasure.length)
    }

    fun svga(svgaName: String): AnimatorBuilder {
        var svgaViews = ArrayList<View>()
        for (view in views) {
            if (view is SVGAImageView) {
                svgaViews.add(view)
            }
        }
        viewAnimator.svga(svgaViews, svgaName)
        return this
    }
}