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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import ar.edu.ort.orionviajes.data.Receipt
import ar.edu.ort.orionviajes.databinding.FragmentScanBinding
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

    private lateinit var progressDialog: ProgressDialog

    private lateinit var textRecognizer: TextRecognizer


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentScanBinding.inflate(inflater, container, false)

        cameraPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)


        binding.capturePhoto.setOnClickListener{
            showInputImageDialog()
        }

        binding.recognizeText.setOnClickListener{
            if (imageUri == null){
                showSnackbar("Toma una imagen primero...")
            } else {
                recognizeTextFromImage()
            }
        }
        return binding.root
    }

    private fun recognizeTextFromImage() {
        progressDialog.setMessage("Preparando imagen...")
        progressDialog.show()

        try{
            val inputImage = InputImage.fromFilePath(requireContext(), imageUri!!)
            progressDialog.setMessage("Reconociendo el ticket...")

            val textTaskResult = textRecognizer.process(inputImage)
                .addOnSuccessListener { text ->
                    progressDialog.dismiss()

                    val recognizedText = text.text.toString()
//                    for (block in text.textBlocks){
//                        recognizedText += block.text.toString()
//                    }

                    val regex = Regex("(TOTAL|AMOUNT)\\s*\$?\\s*\\d+[.,-]?\\d+", RegexOption.IGNORE_CASE)
                    val total = regex.find(recognizedText)!!.value //hacer una fun para filtrar y q adentro tenga el try catch para el find


                    val regexReal = Regex("\\d+[.,-]?\\d+")
                    val totalReal = regexReal.find(total)!!.value

                    Log.d("TEXTO RECONOCIDO", totalReal)


                    //hace el replace para reemplazar las comas y guiones de los tickets
                    val receiptTotal = totalReal.replace(",", ".").replace("-",".")

                    val travelId = ScanFragmentArgs.fromBundle(requireArguments()).travelId
                    val receipt = Receipt(receiptTotal.toFloat())
                    val action = ScanFragmentDirections.actionScanFragmentToEditScanedExpenseFragment(receipt, travelId)
                    findNavController().navigate(action)
                    //falta pasar el travel tmb


                }
                .addOnFailureListener{ e->
                    progressDialog.dismiss()
                    showSnackbar("Error al reconocer ticket: ${e.message}")
                }

        } catch (e: Exception) {
            progressDialog.dismiss()
            showSnackbar("Error al preparar la imagen: ${e.message}")
        }
    }


    private fun showInputImageDialog() {
        val popupMenu = PopupMenu(requireContext(), binding.capturePhoto)
        popupMenu.menu.add(Menu.NONE, 1,2, "CÁMARA")
        popupMenu.menu.add(Menu.NONE, 2,2, "GALERÍA")

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
