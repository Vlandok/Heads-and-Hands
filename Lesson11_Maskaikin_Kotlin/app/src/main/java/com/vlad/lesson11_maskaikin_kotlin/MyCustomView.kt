package com.vlad.lesson11_maskaikin_kotlin

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.graphics.Paint
import android.os.Build
import android.util.TypedValue
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.annotation.SuppressLint
import java.text.SimpleDateFormat


class MyCustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var showInfo: Boolean = false
    private var timeVisiteInMinutes: Int = 0
    private var date: String
    private var colorDate: Int
    private var colorVisiteMinute: Int
    private var colorColumn: Int

    private var visiters: List<Visiter>? = null
    private var countVisiters: Int = 1

    private var dateLong: Long = System.currentTimeMillis()

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("dd.MM")

    private var factorHeightColomn: Float = 0f
    private var heightColomn: Float = 0f
    private var yPositionColomnVisite: Float = 0f
    private var animation: ValueAnimator? = null

    private var bottomPositionColomnVisite: Float = 0f
    private var topPositionColomnVisite: Float = 0f
    private var leftPadding: Float = 0f
    private var leftPaddingForTime: Float = 0f
    private var leftPaddingForDate: Float = 0f

    private lateinit var timeVisiteInMinutesPaint: Paint
    private lateinit var datePaint: Paint
    private lateinit var rectanglePaint: Paint

    private var dateBounds = Rect()

    companion object {
        private const val PADDING_BEETWEN_TIME_AND_COLOMN = 15f
        private const val PADDING_BEETWEN_DATE_AND_COLOMN = 20f
        private const val WIDTH_COLOMN = 8f
        private const val WIDTH_FACTOR = 1
        private const val DEFAULT_HEIGHT_COLOMN = 10f
        private const val NUMBER_COLOMNS = 9
        private const val CORNER_RADIUS = 100f
        private const val MULTIPLIER_HEIGHT_COLOMN = 3
    }

    init {
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MyCustomView,
                0, 0)
        try {
            date = formatter.format(dateLong)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                colorDate = a.getColor(R.styleable.MyCustomView_colorDate, resources.getColor(R.color.white, null))
                colorVisiteMinute = a.getColor(R.styleable.MyCustomView_colorVisiteMinute, resources.getColor(R.color.light_gold, null))
                colorColumn = a.getColor(R.styleable.MyCustomView_colorVisiteMinute, resources.getColor(R.color.light_gold, null))
            } else {
                colorDate = a.getColor(R.styleable.MyCustomView_colorDate, resources.getColor(R.color.white))
                colorVisiteMinute = a.getColor(R.styleable.MyCustomView_colorVisiteMinute, resources.getColor(R.color.light_gold))
                colorColumn = a.getColor(R.styleable.MyCustomView_colorVisiteMinute, resources.getColor(R.color.light_gold))
            }
        } finally {
            a.recycle()
        }
        init()
    }

    private fun init() {
        timeVisiteInMinutesPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        timeVisiteInMinutesPaint.color = colorVisiteMinute
        datePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        datePaint.color = colorDate
        rectanglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        rectanglePaint.color = colorColumn
        val spSizeTime = 11
        timeVisiteInMinutesPaint.textSize = spToPx(spSizeTime.toFloat())

        val spSizeDate = 10
        datePaint.textSize = spToPx(spSizeDate.toFloat())

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d("LES", "onMeasure")

        datePaint.getTextBounds(date, 0, date.length, dateBounds)

        val desiredWidth = paddingLeft + paddingRight
        val width = View.resolveSizeAndState(desiredWidth, widthMeasureSpec, 0)

        val desiredHeight = paddingBottom + paddingTop
        val height = View.resolveSizeAndState(desiredHeight, heightMeasureSpec, 0)

        Log.d("LES", "w= $width, h= $height")

        bottomPositionColomnVisite = height - paddingBottom.toFloat() - dateBounds.height() - PADDING_BEETWEN_DATE_AND_COLOMN
        topPositionColomnVisite = bottomPositionColomnVisite - DEFAULT_HEIGHT_COLOMN

        leftPadding = (measuredWidth / 2).toFloat()
        leftPaddingForTime = leftPadding - dateBounds.width() / 4
        leftPaddingForDate = leftPadding - dateBounds.width() / 2

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        Log.d("LES", "onDraw")
        super.onDraw(canvas)

        if (visiters != null && visiters!!.isNotEmpty()) {
            countVisiters = visiters!!.count()
            for ((i, _) in visiters!!.withIndex()) {
                leftPadding = (measuredWidth / (countVisiters + 1) * (i + WIDTH_FACTOR)).toFloat()
                leftPaddingForTime = leftPadding - dateBounds.width() / 4
                leftPaddingForDate = leftPadding - dateBounds.width() / 2
                heightColomn = (visiters!![i].timeInMinute * MULTIPLIER_HEIGHT_COLOMN * factorHeightColomn)
                yPositionColomnVisite = topPositionColomnVisite - heightColomn

                canvas?.drawText(formatter.format(visiters!![i].date),
                        leftPaddingForDate,
                        canvas.height.toFloat() - paddingBottom,
                        datePaint)
                canvas?.drawText(visiters!![i].timeInMinute.toString(),
                        leftPaddingForTime,
                        (topPositionColomnVisite - PADDING_BEETWEN_TIME_AND_COLOMN) - heightColomn,
                        timeVisiteInMinutesPaint)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas?.drawRoundRect(leftPadding - WIDTH_COLOMN / 2,
                            yPositionColomnVisite,
                            leftPadding + WIDTH_COLOMN / 2,
                            bottomPositionColomnVisite,
                            dpToPx(CORNER_RADIUS),
                            dpToPx(CORNER_RADIUS),
                            rectanglePaint)
                } else {
                    canvas?.drawRect(leftPadding - WIDTH_COLOMN / 2,
                            yPositionColomnVisite,
                            leftPadding + WIDTH_COLOMN / 2,
                            bottomPositionColomnVisite,
                            rectanglePaint)
                }
            }
        } else {
            canvas?.drawText(formatter.format(System.currentTimeMillis()),
                    leftPaddingForDate
                    , canvas.height.toFloat() - paddingBottom,
                    datePaint)
            canvas?.drawText(timeVisiteInMinutes.toString(),
                    leftPaddingForTime,
                    topPositionColomnVisite - PADDING_BEETWEN_TIME_AND_COLOMN,
                    timeVisiteInMinutesPaint)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas?.drawRoundRect(leftPadding - WIDTH_COLOMN / 2,
                        topPositionColomnVisite,
                        leftPadding + WIDTH_COLOMN / 2,
                        bottomPositionColomnVisite,
                        dpToPx(CORNER_RADIUS),
                        dpToPx(CORNER_RADIUS),
                        rectanglePaint)
            } else {
                canvas?.drawRect(leftPadding - WIDTH_COLOMN / 2,
                        topPositionColomnVisite,
                        leftPadding + WIDTH_COLOMN / 2,
                        bottomPositionColomnVisite,
                        rectanglePaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event != null) {
            return when {
                event.action == MotionEvent.ACTION_DOWN -> true
                event.action == MotionEvent.ACTION_UP -> {
                    startMyAnimation()
                    true
                }
                else -> false
            }
        }
        return false
    }

    private fun startMyAnimation() {
        if (showInfo) {
            animation = ValueAnimator.ofFloat(1f, 0f)
            animation?.duration = 600
            showInfo = false
        } else {
            animation = ValueAnimator.ofFloat(0f, 1f)
            animation?.duration = 600
            showInfo = true
        }
        animation?.addUpdateListener { animation ->
            factorHeightColomn = animation.animatedValue as Float
            invalidate()
        }
        animation?.start()
        return
    }

    fun getColorDate(): Int {
        return colorDate
    }

    fun setColorDate(colorDate: Int) {
        this.colorDate = colorDate
    }

    fun getColorVisiteMinute(): Int {
        return colorVisiteMinute
    }

    fun setColorVisiteMinute(colorVisiteMinute: Int) {
        this.colorVisiteMinute = colorVisiteMinute
    }

    fun getColorColumn(): Int {
        return colorColumn
    }

    fun setColorColumn(colorColumn: Int) {
        this.colorColumn = colorColumn
    }

    fun setData(visiters: List<Visiter>) {
        this.visiters = visiters.take(NUMBER_COLOMNS)
    }

    private fun spToPx(sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}