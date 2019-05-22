package me.pg999w.mnistgui

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bitmap.*
import me.pg999w.mnistgui.tflite.MnistClassifier

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

        val classifier = MnistClassifier(this, MnistClassifier.Device.CPU, 1)
        val result = classifier.use { it.recognizeImage(bitmap) }
        val maxIndex = result.mapIndexed { i, v -> Pair(i, v) }.maxBy { it.second }!!.first
        result_text.text = maxIndex.toString()
    }
}
