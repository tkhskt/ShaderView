package com.appspell.shaderview.demo.distortion

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.appspell.shaderview.demo.R
import com.appspell.shaderview.demo.databinding.ActivityDistortionBinding
import com.appspell.shaderview.ktx.initialize
import com.appspell.shaderview.ktx.uniform.Texture2D
import com.appspell.shaderview.ktx.uniform.Vec2f
import com.appspell.shaderview.ktx.uniform.Vec4f
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt


class DistortionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDistortionBinding

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    private var targetSpeed: Float = 0f
    private var followPointerX: Float = 0f
    private var followPointerY: Float = 0f

    private var viewWidth: Float = 0f
    private var viewHeight: Float = 0f

    private var imageWidth: Float = 0f
    private var imageHeight: Float = 0f
    private val imageAspectRatio: Float
        get() = imageHeight / imageWidth

    @SuppressLint("ClickableViewAccessibility")
    private val touchListener = View.OnTouchListener { _, e ->
        val x: Float = e.x
        val y: Float = e.y

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                val speed = sqrt(
                    (previousX - x).toDouble().pow(2.0) +
                            (previousY - y).toDouble().pow(2.0)
                )
                targetSpeed -= (0.1 * (targetSpeed - speed)).toFloat()
                followPointerX -= (0.1 * (followPointerX - x)).toFloat()
                followPointerY -= (0.1 * (followPointerY - y)).toFloat()
            }
            MotionEvent.ACTION_UP -> {
                targetSpeed = 0f
                followPointerX = 0f
                followPointerY = 0f
            }
        }

        previousX = x
        previousY = y

        return@OnTouchListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistortionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setImageHeight()
        setUpShaderView()
    }

    private fun setUpShaderView() {
        binding.shaderView.apply {
            initialize {
                fragmentShaderRawResId(R.raw.distort_texture)
                updateContinuously(true)
            }
            distortTextureShaderParameters {
                uPointer(Vec2f(followPointerX, followPointerY))
                uVelo(0f)
                resolution(Vec4f(0f, 0f, 0f, 0f))
                uTexture(Texture2D(textureResourceId = R.drawable.bokeh))
            }
            onDrawFrame {
                viewHeight = measuredHeight.toFloat()
                viewWidth = measuredWidth.toFloat()
                val resZ: Float
                val resW: Float
                if (viewHeight / viewWidth > imageAspectRatio) {
                    resZ = viewWidth / viewHeight * imageAspectRatio
                    resW = 1f
                } else {
                    resZ = 1f
                    resW = viewHeight / viewWidth / imageAspectRatio
                }
                uPointer(Vec2f(followPointerX / 1000, followPointerY / 1000))
                resolution(
                    Vec4f(viewWidth, viewHeight, resZ, resW)
                )
                uVelo(min(targetSpeed / 100, 0.5f))
            }
            setOnTouchListener(touchListener)
        }
    }

    private fun setImageHeight() {
        val bitmapDrawable =
            ContextCompat.getDrawable(applicationContext, R.drawable.bokeh) as BitmapDrawable
        imageWidth = bitmapDrawable.intrinsicWidth.toFloat()
        imageHeight = bitmapDrawable.intrinsicHeight.toFloat()
    }
}
