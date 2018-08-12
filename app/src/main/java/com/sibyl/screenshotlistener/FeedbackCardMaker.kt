package com.sibyl.screenshotlistener

import android.content.Context
import android.graphics.*
import android.util.Log


/**
 * @author Sasuke on 2018/8/11.
 */
class FeedbackCardMaker(context: Context) {
    val TEXT_PAINT_SIZE = 70.0f
    val bottomCard by lazy {
        BitmapFactory.decodeResource(context.resources, R.mipmap.feedback_card)
    }

    /**
     * 把需要显示的信息画到底部卡片上
     */
    fun drawInfo2Card(vararg infos: String): Bitmap {
        val newBitmap = Bitmap.createBitmap(bottomCard.width, bottomCard.height, Bitmap.Config.RGB_565)
        val canvas = Canvas(newBitmap)
        val bitmapPaint = Paint().apply {
            isDither = true
            isFilterBitmap = true
        }
        val src = Rect(0, 0, bottomCard.width, bottomCard.height)
        val dst = Rect(0, 0, bottomCard.width, bottomCard.height)
        canvas.drawBitmap(bottomCard, src, dst, bitmapPaint)

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DEV_KERN_TEXT_FLAG)
        textPaint.apply {
            textSize = TEXT_PAINT_SIZE
            typeface = Typeface.SANS_SERIF
            color = Color.WHITE
        }

        val heightUnit = newBitmap.height / (infos.size + 1).toFloat()//每行字的高度
        var startLine = heightUnit//绘制文字的起始水平线高度

        infos.forEach {
            canvas.drawText(it, newBitmap.width / 3.toFloat() , startLine, textPaint)
            startLine += heightUnit
            Log.i("SasukeLog",heightUnit.toString())
        }
        canvas.save()
        canvas.restore();
        return newBitmap
    }

}