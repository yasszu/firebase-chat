package com.example.firebasechat.helper

/**
 * Created by Yasuhiro Suzuki on 2016/12/11.

 * This class is used for Picasso.
 */
import android.graphics.*

import com.squareup.picasso.Transformation

class CircleTransformation : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)
        val r = size / 2f
        val squaredBitmap = createSquaredBitmap(source, size)
        val bitmap = Bitmap.createBitmap(size, size, source.config)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true
        canvas.drawCircle(r, r, r, paint)
        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return "circle"
    }

    fun createSquaredBitmap(source: Bitmap, size: Int): Bitmap {
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        return Bitmap.createBitmap(source, x, y, size, size).apply {
            if (this != source) {
                source.recycle()
            }
        }
    }

}