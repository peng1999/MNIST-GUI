package me.pg999w.mnistgui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val BITMAP_MESSAGE = "yes"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        draw_view.setStrokeWidth(50f)
    }

    fun onClearClicked(v: View) {
        draw_view.clearCanvas()
    }

    fun showBitmap(v: View) {
        val intent = Intent(this, BitmapActivity::class.java)
        val bitmap = draw_view.getBitmap()
        val compressed = Bitmap.createScaledBitmap(bitmap, 28, 28, true)

        intent.putExtra(BITMAP_MESSAGE, compressed)
        startActivity(intent)
    }
}
