package com.github.xingling.animatorlib;

import android.animation.ValueAnimator;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef(flag = false, value = {ValueAnimator.RESTART, ValueAnimator.REVERSE})
@Retention(RetentionPolicy.SOURCE)
public @interface RepeatMode {
}
