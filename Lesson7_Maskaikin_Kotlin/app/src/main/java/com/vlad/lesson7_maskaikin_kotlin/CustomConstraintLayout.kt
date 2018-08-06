package com.vlad.lesson7_maskaikin_kotlin

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet


@CoordinatorLayout.DefaultBehavior(MoveUpwardBehavior::class)
class CustomConstraintLayout : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}