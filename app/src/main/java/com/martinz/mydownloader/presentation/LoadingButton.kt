package com.martinz.mydownloader.presentation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.martinz.mydownloader.R
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var textWidth = 0f
    private val mDuration: Long = 5000

    private var textSize: Float = resources.getDimension(R.dimen.default_text_size)
    private var circleXOffset = textSize / 2

    private var buttonTitle: String

    private var progressBarCircle = 0f
    private var progressBarWidth = 0f

    private var buttonColor = context.getColor(R.color.colorPrimary)
    private var loadingColor = context.getColor(R.color.colorPrimaryDark)
    private var circleColor = context.getColor(R.color.colorAccent)

    private var valueAnimator = ValueAnimator()

    var mButtonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { property, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                buttonTitle = "Clicked"
                invalidate()
            }
            ButtonState.Loading -> {
                buttonTitle = context.getString(R.string.button_loading)
                valueAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat())
                valueAnimator.duration = mDuration
                valueAnimator.addUpdateListener { animation ->
                    progressBarWidth = animation.animatedValue as Float
                    progressBarCircle = (widthSize.toFloat() / 365) * progressBarWidth
                    invalidate()
                }
                valueAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        progressBarWidth = 0f
                        if (mButtonState == ButtonState.Loading) {
                            mButtonState = ButtonState.Loading
                        }
                    }
                })
                valueAnimator.start()

            }
            ButtonState.Completed -> {
                valueAnimator.cancel()
                progressBarWidth = 0f
                progressBarCircle = 0f
                buttonTitle = context.getString(R.string.button_download)
                postInvalidate()
            }
        }

    }

    private val paint = Paint().apply {
        isAntiAlias = true
        textSize = resources.getDimension(R.dimen.default_text_size)
    }

    init {
        buttonTitle = getContext().getString(R.string.button_download)

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
            loadingColor = getColor(R.styleable.LoadingButton_buttonLoadingColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_loadingCircleColor, 0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawViewBackgroundColor(canvas)
        drawProgressBackground(canvas)
        drawViewTitle(canvas)
        drawCircleProgress(canvas)
    }

    private fun drawViewBackgroundColor(canvas: Canvas?) {
        paint.color = buttonColor
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
    }

    private fun drawProgressBackground(canvas: Canvas?) {
        paint.color = loadingColor
        canvas?.drawRect(0f, 0f, progressBarWidth, heightSize.toFloat(), paint)
    }

    private fun drawCircleProgress(canvas: Canvas?) {
        canvas?.save()
        canvas?.translate(
            widthSize / 2 + textWidth / 2 + circleXOffset,
            heightSize / 2 - textSize / 2
        )
        paint.color = circleColor
        canvas?.drawArc(
            RectF(0f, 0f, textSize, textSize),
            0F,
            progressBarCircle * 0.365f,
            true,
            paint
        )
        canvas?.restore()
    }

    private fun drawViewTitle(canvas: Canvas?) {
        paint.color = Color.WHITE
        textWidth = paint.measureText(buttonTitle)
        canvas?.drawText(
            buttonTitle,
            widthSize / 2 - textWidth / 2,
            heightSize / 2 - (paint.descent() + paint.ascent()) / 2,
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val mWidth: Int = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        val mHeight: Int = resolveSizeAndState(
            MeasureSpec.getSize(mWidth),
            heightMeasureSpec,
            0
        )
        widthSize = mWidth
        heightSize = mHeight
        setMeasuredDimension(mWidth, mHeight)
    }

}