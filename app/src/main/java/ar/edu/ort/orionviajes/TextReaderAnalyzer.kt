package ar.edu.ort.orionviajes

import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

@androidx.camera.core.ExperimentalGetImage
class TextReaderAnalyzer(private val textFoundListener: (String) -> Unit) : ImageAnalysis.Analyzer{
    override fun analyze(imageProxy: ImageProxy) {
//        val mediaImage = imageProxy.image
//        if (mediaImage != null) {
//            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
//        }

        imageProxy.image?.let { process(it, imageProxy) }
    }

    private fun process(image: Image, imageProxy: ImageProxy) {
        try {
            readTextFromImage(InputImage.fromMediaImage(image, 90), imageProxy)
        } catch (e: IOException) {
            Log.d("coso", "Error al cargar la imagen")
            e.printStackTrace()
        }
    }

    private fun readTextFromImage(image: InputImage, imageProxy: ImageProxy) {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            .process(image)
            .addOnSuccessListener { visionText ->
                processTextFromImage(visionText, imageProxy)
                imageProxy.close()
            }
            .addOnFailureListener { error ->
                Log.d("coso2", "Error al procesar el mensaje")
                error.printStackTrace()
                imageProxy.close()
            }
    }

    private  fun processTextFromImage(visionText: Text, imageProxy: ImageProxy) {
        for (block in visionText.textBlocks) {
            //Aca accedemos a toodo el bloque de texto
            for (line in block.lines) {
                //aca accedemos a las lineas de texto
                for (element in line.elements) {
                    textFoundListener(element.text)
                }
            }
        }
    }
}