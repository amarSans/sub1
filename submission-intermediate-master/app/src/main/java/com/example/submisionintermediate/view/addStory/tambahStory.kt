package com.example.submisionintermediate.view.addStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.example.submisionintermediate.R
import com.example.submisionintermediate.databinding.ActivityTambahStoryBinding
import com.example.submisionintermediate.view.ViewModelFactory
import com.example.submisionintermediate.view.main.MainActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class tambahStory : AppCompatActivity() {
    private val viewModel by viewModels<AddStoryViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var binding: ActivityTambahStoryBinding
    private var selectedImage: Uri? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddCamera.setOnClickListener {
            bukaKamera()
        }

        binding.btnAddGallery.setOnClickListener {
            bukaGaleri()
        }
        showLoading(false)
        binding.btnKirim.setOnClickListener {
            uploadImage()
            lifecycleScope.launch {
                viewModel.tambahStoryResult.collect { result ->
                    when (result) {
                        is StoryResult.Success -> {
                            showToast("cerita berhasil ditambahkan")
                            navigateToMainActivity()
                        }
                        is StoryResult.Error -> {
                            showToast("gagal menambahkan cerita")
                            showLoading(false)
                        }
                        else ->{showLoading(true)}
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalPagingApi::class)
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uploadImage() {
        selectedImage?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("image File", "showImage:${imageFile.path}")
            val description = binding.edtDescrip.text.toString().trim()
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val reguestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo", imageFile.name, reguestImageFile
            )
            if (description.isEmpty()) {
                showToast("isi description dulu")
            }
            viewModel.tambahStory(multipartBody, requestBody)
        } ?: showToast(getString(R.string.gambar_kosong))
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val REQUEST_CAMERA_PERMISSION = 123
    private fun bukaKamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            selectedImage = getImageUri(this)
            launcherIntentCamera.launch(selectedImage)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectedImage = getImageUri(this)
                    launcherIntentCamera.launch(selectedImage)
                } else {
                    Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val launcherIntentCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSucces ->
            if (isSucces) {
                showImage()
            }
        }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImage = uri
            showImage()
        } else {
            Log.d("photo picker", "no media selected")
        }
    }

    private fun showImage() {
        selectedImage?.let {
            Log.d("Image Uri", "showImage:$it")
            binding.previewGambar.setImageURI(it)
        }
    }

    private fun bukaGaleri() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    }


}


