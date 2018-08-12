package com.sibyl.screenshotlistener

import android.content.Context
import android.graphics.*
import java.io.*


/**
 * @author Sasuke on 2018/8/11.
 */
class FeedbackCardMaker(val context: Context) {
    val TEXT_PAINT_SIZE = 45.0f
//    val bottomCard by lazy {
//        BitmapFactory.decodeResource(context.resources, R.mipmap.feedback_card)
//    }

    /**
     * 把需要显示的信息画到底部卡片上
     */
    fun drawInfo2BottomCard(vararg infos: String): Bitmap {
        val bottomCard = readBitmapRes(context,R.mipmap.feedback_card)
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

        val heightUnit = newBitmap.height / infos.size.toFloat()//每行字的高度
//        var startLine = ((heightUnit -TEXT_PAINT_SIZE)/2) + TEXT_PAINT_SIZE //绘制文字的起始水平线高度
        var startLine = ((bottomCard.height - infos.size * TEXT_PAINT_SIZE *1.5) / 2 + TEXT_PAINT_SIZE).toFloat()
        infos.forEach {
            canvas.drawText(it, newBitmap.width / 4.toFloat() , startLine, textPaint)
            startLine += TEXT_PAINT_SIZE * 1.5f
        }
        canvas.save()
        canvas.restore()
        return newBitmap
    }


    /**
     * 把截图与底部信息卡拼接起来
     */
    fun mergeScrShot2BottomCard(scrShotPath: String, bottomCard: Bitmap): Boolean{
        var resultBmp: Bitmap
        val shotBmp = BitmapFactory.decodeFile(scrShotPath,BitmapFactory.Options().apply {
            val opt = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(scrShotPath,opt)
            //前面都是铺垫，这个才是目的
            this.inSampleSize = opt.outWidth / bottomCard.width
        })

        val shotWidth = shotBmp.getWidth()
//        if (bottomCard.getWidth() !== shotWidth) {
//            //以第二张图片的宽度为标准，对第一张图片进行缩放。
//            val shotHeight = bottomCard.getHeight() * shotWidth / bottomCard.getWidth()
//            resultBmp = Bitmap.createBitmap(bottomCard.width, shotHeight + bottomCard.height, Bitmap.Config.RGB_565)
//            val canvas = Canvas(resultBmp)
//            val newSizeBmp2 = resizeBitmap(shotBmp, bottomCard.width, shotBmp.height)
//            canvas.drawBitmap(shotBmp,0.toFloat(),0.toFloat(),null)
//            canvas.drawBitmap(newSizeBmp2, 0.toFloat(), shotBmp.height.toFloat(), null)
//        } else {
            //两张图片宽度相等，则直接拼接。
            resultBmp = Bitmap.createBitmap(bottomCard.width, shotBmp.height + bottomCard.height, Bitmap.Config.RGB_565)
            val canvas = Canvas(resultBmp)
            canvas.drawBitmap(shotBmp,0.toFloat(),0.toFloat(),null)
            canvas.drawBitmap(bottomCard, 0.toFloat(), shotBmp.height.toFloat(), null)
//        }

        return saveBmp2File(resultBmp,File(scrShotPath),Bitmap.CompressFormat.JPEG)
    }


    /**
     * 通过流的方式，可以使调用底层JNI来实现减少Java层的内存消耗。
     */
    fun readBitmapRes(context: Context, resId: Int): Bitmap {
        val opt = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.RGB_565
            inPurgeable = true
            inInputShareable = true
        }

        //获取资源图片
        val inputStream = context.resources.openRawResource(resId)
        return BitmapFactory.decodeStream(inputStream, null, opt)
    }


    /**
     * 改变图片尺寸
     */
    fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val scaleWidth = newWidth.toFloat() / bitmap.width
        val scaleHeight = newHeight.toFloat() / bitmap.height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    /**
     * 保存图片到文件
     */
    fun saveBmp2File(src: Bitmap, file: File, format: Bitmap.CompressFormat/*, recycle: Boolean*/): Boolean {
        if(src == null || src.width == 0 || src.height == 0){
            return false
        }

        var os: OutputStream? = null
        var ret = false
        try {
            os = BufferedOutputStream(FileOutputStream(file))
            ret = src.compress(format, 100, os)
//            if (recycle && !src.isRecycled)
//                src.recycle()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }finally {
            os?.close()
        }

        return ret
    }

}