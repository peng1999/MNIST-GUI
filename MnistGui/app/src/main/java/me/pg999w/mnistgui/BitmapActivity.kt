package me.pg999w.mnistgui

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
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

        // render text
        result_text.text = maxIndex.toString()

        // draw chart
        val column = AnyChart.column()
        val dataEntry = result.mapIndexed { i, v -> ValueDataEntry(i, v) }
        column.data(dataEntry)
        column.yAxis(false)
        prob_chart.setChart(column)
    }
}
