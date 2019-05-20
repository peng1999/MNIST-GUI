package me.pg999w.mnistgui

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bitmap.*

class BitmapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap)

        val bitmap = intent.extras?.get(MainActivity.BITMAP_MESSAGE)

        if (bitmap !is Bitmap) {
            finishActivity(0)
            return
        }

        image_view.setImageBitmap(bitmap)
    }
}
