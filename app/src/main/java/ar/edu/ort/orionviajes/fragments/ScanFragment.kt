package ar.edu.ort.orionviajes.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.TextReaderAnalyzer
import ar.edu.ort.orionviajes.databinding.FragmentScanBinding
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanFragment : Fragment() {

    private lateinit var binding: FragmentScanBinding
    private val cameraExecutor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    private lateinit var viewFinder: PreviewView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentScanBinding.inflate(inflater, container, false)



        return binding.root
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            Runnable {
                val preview = Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(binding.cameraPreviewView.surfaceProvider) }
                cameraProviderFuture.get().bind(preview, imageAnalyzer)
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    private fun ProcessCameraProvider.bind(
        preview: Preview,
        imageAnalyzer: ImageAnalysis
    ) = try {
        unbindAll()
        bindToLifecycle(
            this@ScanFragment,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            imageAnalyzer
        )
    } catch (ise: IllegalStateException) {
        Log.e(TAG.name, "Binding fallido", ise)
    }

    @androidx.camera.core.ExperimentalGetImage
    private val imageAnalyzer by lazy {
        ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .build()
            .also {
                it.setAnalyzer(
                    cameraExecutor,
                    TextReaderAnalyzer(::onTextFound)
                )
            }
    }

    private fun onTextFound(foundText: String){
        Log.d(TAG.name, "Tenemos este texto: $foundText")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Permisos no concedidos.", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private companion object {
        val TAG = ScanFragment::class.java
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}