package com.sibyl.sasukescreenshotlistener

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.sibyl.screenshotlistener.FeedbackCardMaker
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File


class MainActivity : AppCompatActivity() {
    var manager: com.sibyl.screenshotlistener.ScreenShotListenManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show()

        manager = com.sibyl.screenshotlistener.ScreenShotListenManager.newInstance(applicationContext).apply {
            setListener(object : com.sibyl.screenshotlistener.ScreenShotListenManager.OnScreenShotListener {
                override fun onShot(imagePath: String?) {

                    Toast.makeText(this@MainActivity, "screenshot get:$imagePath", Toast.LENGTH_LONG).show()
                    doAsync {
                        Thread.sleep(1500)//有些垃圾系统截图时写入磁盘比较慢，所以这边要等一下。
                        FeedbackCardMaker(this@MainActivity).apply {
                            val bottomCard = drawInfo2BottomCard("版本：v4.6.0.1", "门店：0110074 工号：690", "设备：Android 8.0.1 Oneplus3t")
                            imagePath?.let {
                                if (mergeScrShot2BottomCard(imagePath, bottomCard)) {
                                    uiThread {
                                        Picasso.with(this@MainActivity).load(File(imagePath)).into(img)
//                                        img.setImageBitmap(bottomCard)
                                        img.setOnClickListener {
                                            val shareIntent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                type = "image/*"
                                                putExtra(Intent.EXTRA_STREAM, Uri.fromFile(File(imagePath)))
                                            }
                                            //创建分享的Dialog
                                            application.startActivity(Intent.createChooser(shareIntent, "转发到"))
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            })
        }
        manager?.startListen()
//        img.setImageResource(R.mipmap.feedback_card)


    }

    override fun onPause() {
        super.onPause()
        manager?.stopListen()
//        Log.i("SasukeLog", "onPause()")
    }

    override fun onResume() {
        super.onResume()
        manager?.startListen()
//        Log.i("SasukeLog", "onResume()")
    }
}
