package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.provider.CalendarContract
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var M_WidthSize = 0
    private var M_HeightSize = 0
    private var M_ButttonColor = 0
    private var M_CircleColor = 0
    private var M_Text = ""
    private var M_Button = Rect()
    private var M_AnimationRect = Rect()
    private var M_Arc = RectF()
    private var M_SweepAngle = 0f
    private var M_ButtonPaint = Paint().apply{
        style = Paint.Style.FILL
        isAntiAlias = true

    }
    private var M_AnimationButtonPaint = Paint().apply{
        style = Paint.Style.FILL
        color = Color.parseColor("#FF047768")
        isAntiAlias = true

    }
    private var M_TextPaint = Paint().apply{
        style = Paint.Style.FILL
        isAntiAlias = true
        color = Color.WHITE
        textSize = 70f
        textAlign = Paint.Align.CENTER

    }
    private var M_ArcPaint = Paint().apply{
        style = Paint.Style.FILL
        isAntiAlias = true

    }

    private val M_CirclevalueAnimator = ValueAnimator.ofFloat(0f,360f).setDuration(1000).apply {
        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART

        addUpdateListener {
            M_SweepAngle = it.animatedValue as Float
            invalidate()
        }

    }
    lateinit var M_RectvalueAnimator : ValueAnimator


     var M_buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
if (new == ButtonState.Clicked)
{
M_CirclevalueAnimator.start()
    M_RectvalueAnimator.start()
} else if (new == ButtonState.Completed)
{
    M_CirclevalueAnimator.end()
    M_RectvalueAnimator.end()
    M_SweepAngle = 0f
    M_AnimationRect.right = 0
    invalidate()
}
    }


    init {
        context.obtainStyledAttributes(attrs, R.styleable.LoadingButton).apply{
            M_ButttonColor = getColor(R.styleable.LoadingButton_buttonColor, Color.BLUE)
            M_ButtonPaint.color = M_ButttonColor
            M_CircleColor = getColor(R.styleable.LoadingButton_loadingCircleColor, Color.YELLOW)
            M_ArcPaint.color = M_CircleColor
            M_Text = getString(R.styleable.LoadingButton_text).toString()
            recycle()
        }
        isClickable = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(M_Button, M_ButtonPaint)
        canvas?.drawRect(M_AnimationRect,M_AnimationButtonPaint)
        canvas?.drawText(M_Text,M_WidthSize/2f,M_HeightSize/2f,M_TextPaint)
        canvas?.drawArc(M_Arc,0f,M_SweepAngle,true,M_ArcPaint)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        M_WidthSize = w
        M_HeightSize = h
        M_Arc.top = M_HeightSize/4f
        M_Arc.left = M_WidthSize*13/16f
        M_Arc.right = M_WidthSize*13/16f + 60f
        M_Arc.bottom = M_HeightSize*3/4f
        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        M_Button.top = 0
        M_Button.left = 0
        M_Button.right = w
        M_Button.bottom = h
        M_AnimationRect.top = 0
        M_AnimationRect.left = 0
        M_AnimationRect.right = 0
        M_AnimationRect.bottom = h
        M_RectvalueAnimator = ValueAnimator.ofInt(0,w).setDuration(1000).apply {
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART

            addUpdateListener {
                M_AnimationRect.right = it.animatedValue as Int
                invalidate()
            }

        }
    }

}