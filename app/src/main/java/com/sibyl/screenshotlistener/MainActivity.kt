package com.sibyl.screenshotlistener

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    var manager: ScreenShotListenManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show()

        manager = ScreenShotListenManager.newInstance(this@MainActivity).apply {
            setListener(object: ScreenShotListenManager.OnScreenShotListener{
                override fun onShot(imagePath: String?) {
                    Toast.makeText(this@MainActivity, "screenshot get:$imagePath", Toast.LENGTH_LONG).show()
                    Log.i("SasukeLog","onShot()")
                }
            })
        }
        manager?.startListen()


    }

    override fun onPause() {
        super.onPause()
//        manager?.stopListen()
        Log.i("SasukeLog","onPause()")
    }

    override fun onResume() {
        super.onResume()
//        manager?.startListen()
        Log.i("SasukeLog","onResume()")
    }
}
