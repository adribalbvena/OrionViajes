package ar.edu.ort.orionviajes.fragments

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import ar.edu.ort.orionviajes.data.Receipt
import ar.edu.ort.orionviajes.databinding.FragmentScanBinding
import ar.edu.ort.orionviajes.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.lang.Exception


class ScanFragment : Fragment() {
    private lateinit var binding: FragmentScanBinding

    private companion object{
        private const val CAMERA_REQUEST_CODE = 100
        private const val STORAGE_REQUEST_CODE = 101
    }

    private var imageUri: Uri? = null

    private lateinit var cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>

    private lateinit var progressBarScan : ProgressBar

    private lateinit var textRecognizer: TextRecognizer


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentScanBinding.inflate(inflater, container, false)

        cameraPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        progressBarScan = binding.progressBarScan

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)


        binding.capturePhoto.setOnClickListener{
            showInputImageDialog()
        }

        binding.recognizeText.setOnClickListener{
            it.hideKeyboard()
            if (imageUri == null){
                showSnackbar("Selecciona una imagen primero...")
            } else {
                recognizeTextFromImage()
            }
        }
        return binding.root
    }

    private fun recognizeTextFromImage() {
        progressBarScan.visibility = View.VISIBLE

        try{
            val inputImage = InputImage.fromFilePath(requireContext(), imageUri!!)
            progressBarScan.visibility = View.VISIBLE


            val textTaskResult = textRecognizer.process(inputImage)
                .addOnSuccessListener { text ->
                    progressBarScan.visibility = View.GONE


                    val recognizedText = text.text.toString()

                    val totalReal = passToRegex(recognizedText)

                    Log.d("TEXTO RECONOCIDO", totalReal)

                    //hace el replace para reemplazar las comas y guiones de los tickets
                    val receiptTotal = totalReal.replace(",", ".").replace("-",".")

                    val travelId = ScanFragmentArgs.fromBundle(requireArguments()).travelId
                    val receipt = Receipt(receiptTotal.toFloat())
                    val action = ScanFragmentDirections.actionScanFragmentToEditScanedExpenseFragment(receipt, travelId)
                    findNavController().navigate(action)
                }
                .addOnFailureListener{ e->
                    progressBarScan.visibility = View.GONE
                    showSnackbar("Error al reconocer la imagen")
                }
        } catch (e: Exception) {
            progressBarScan.visibility = View.GONE
            showSnackbar("Error al preparar la imagen")
        }
    }

    private fun passToRegex(total : String) : String {
        var filteredAmount = "0"
        val regex = Regex("(TOTAL|AMOUNT)\\s*\$?\\s*\\d+[.,-]?\\d+", RegexOption.IGNORE_CASE)
        val regexNumbers = Regex("\\d+[.,-]?\\d+")

        try {
            filteredAmount = regex.find(total)!!.value
            filteredAmount = regexNumbers.find(filteredAmount)!!.value

        } catch (e: Exception) {
            Log.d("ERROR", e.message.toString())
            showSnackbar("No se pudo reconocer el ticket")
        }
        return filteredAmount
    }


    private fun showInputImageDialog() {
        val popupMenu = PopupMenu(requireContext(), binding.capturePhoto)
        popupMenu.menu.add(Menu.NONE, 1,2, "Cámara")
        popupMenu.menu.add(Menu.NONE, 2,2, "Galería")

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if(id == 1){
                if(checkCameraPermission()){
                    pickImageCamera()
                } else {
                    requestCmeraPermission()
                }

            } else if(id == 2) {
                if (checkStoragePermission()){
                    pickImageGallery()
                } else {
                    requestStoragePermission()
                }
            }

            return@setOnMenuItemClickListener true
        }

    }

    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private val galleryActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                imageUri = data!!.data
                binding.imageView.setImageURI(imageUri)
            } else {
                showSnackbar("Cancelado...")
            }
        }

    private fun pickImageCamera(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Título")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción")

        imageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == Activity.RESULT_OK){
               binding.imageView.setImageURI(imageUri)
            } else {
                showSnackbar("Cancelado...")
            }
        }

    private fun checkStoragePermission() : Boolean{
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCameraPermission() : Boolean{
        val cameraResult = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val storageResult = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        return cameraResult && storageResult
    }

    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(requireActivity(), storagePermissions, STORAGE_REQUEST_CODE)
    }

    private fun requestCmeraPermission(){
        ActivityCompat.requestPermissions(requireActivity(), cameraPermissions, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE ->{
                if(grantResults.isNotEmpty()){
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted) {
                        pickImageCamera()
                    } else {
                        showSnackbar("Los permisos de camara y almacenamiento son requeridos...")
                    }
                }
            }
            STORAGE_REQUEST_CODE ->{
                if (grantResults.isNotEmpty()){
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    if (storageAccepted){
                        pickImageGallery()
                    } else {
                        showSnackbar("El permiso de almacenamiento es requerido...")
                    }
                }

            }
        }
    }

    private fun showSnackbar(message: String){
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }
}
