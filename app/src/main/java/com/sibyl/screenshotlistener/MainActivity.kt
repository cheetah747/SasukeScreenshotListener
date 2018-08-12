package com.sibyl.screenshotlistener

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var manager: ScreenShotListenManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show()

        manager = ScreenShotListenManager.newInstance(this@MainActivity).apply {
            setListener(object : ScreenShotListenManager.OnScreenShotListener {
                override fun onShot(imagePath: String?) {

                    Toast.makeText(this@MainActivity, "screenshot get:$imagePath", Toast.LENGTH_LONG).show()
                    img.setImageBitmap(
                            FeedbackCardMaker(this@MainActivity)
                                    .drawInfo2Card("版本：v4.6.0.1", "门店：0110074 工号：690", "设备：Android 8.0.1 Oneplus3t")
                    )
                }
            })
        }
        manager?.startListen()


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
