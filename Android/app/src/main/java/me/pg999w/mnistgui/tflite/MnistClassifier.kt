package me.pg999w.mnistgui.tflite

import android.app.Activity
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MnistClassifier(activity: Activity, device: Device, numThreads: Int) : AutoCloseable {
    enum class Device {
        CPU,
        GPU,
        NNAPI,
    }

    companion object {
        const val MODEL_PATH = "tf-cnn-mnist.tflite"

        const val HEIGHT = 28

        const val WIDTH = 28

        private fun convertBitmapToByteArray(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
            val intValues = IntArray(HEIGHT * WIDTH)
            bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
            // Convert the image to bytes
            val floatValues = Array(1) { Array(28) { Array(28) { FloatArray(1) } } }
            intValues.forEachIndexed { i, v ->
                val acc = (v and 0xff +
                        (v shr 8) and 0xff +
                        (v shr 16) and 0xff) / 3
                floatValues[0][i / 28][i % 28][0] = v.toFloat()
            }
            return floatValues
        }

        private fun loadModelFile(activity: Activity): MappedByteBuffer {
            val fileDescriptor = activity.assets.openFd(MODEL_PATH)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }
    }

    private val tfliteModel: MappedByteBuffer

    private val tflite: Interpreter

    private var gpuDelegate: GpuDelegate? = null

    init {
        tfliteModel = loadModelFile(activity)
        val tfliteOptions = Interpreter.Options()
        when (device) {
            Device.NNAPI -> {
                tfliteOptions.setUseNNAPI(true)
            }
            Device.GPU -> {
                gpuDelegate = GpuDelegate()
                tfliteOptions.addDelegate(gpuDelegate)
            }
            Device.CPU -> {
            }
        }
        tfliteOptions.setNumThreads(numThreads)
        tflite = Interpreter(tfliteModel as ByteBuffer, tfliteOptions)
    }

    fun recognizeImage(bitmap: Bitmap): FloatArray {
        val input = convertBitmapToByteArray(bitmap)
        val output = Array(1) { FloatArray(10) }
        tflite.run(input, output)
        return output[0]
    }

    override fun close() {
        tflite.close()
        gpuDelegate?.close()
    }

}