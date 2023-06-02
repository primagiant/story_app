//package com.primagiant.storyapp.features.story
//
//import android.Manifest
//import android.content.Intent
//import android.content.Intent.ACTION_GET_CONTENT
//import android.content.pm.PackageManager
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.primagiant.storyapp.R
//import com.primagiant.storyapp.databinding.ActivityAddStoryBinding
//import com.primagiant.storyapp.features.MainViewModel
//import com.primagiant.storyapp.features.MainViewModelFactory
//import com.primagiant.storyapp.utils.reduceFileImage
//import com.primagiant.storyapp.utils.rotateFile
//import com.primagiant.storyapp.utils.uriToFile
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.MultipartBody
//import okhttp3.RequestBody.Companion.asRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import java.io.File
//
//class AddStoryActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityAddStoryBinding
//    private val mainViewModel: MainViewModel by viewModels {
//        MainViewModelFactory.getInstance(this)
//    }
//
//    private var getFile: File? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAddStoryBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        supportActionBar?.title = getString(R.string.add_story)
//
//        if (!allPermissionsGranted()) {
//            ActivityCompat.requestPermissions(
//                this,
//                REQUIRED_PERMISSIONS,
//                REQUEST_CODE_PERMISSIONS
//            )
//        }
//
//        binding.btnCamera.setOnClickListener {
//            startCameraX()
//        }
//
//        binding.btnGalery.setOnClickListener {
//            startGallery()
//        }
//
//        binding.btnSubmit.setOnClickListener {
//            validate()
//        }
//    }
//
//    private fun isNotNullImage(): Boolean = getFile != null
//    private fun isNotNullDesc(): Boolean = binding.inputDesc.text.isNotEmpty()
//
//    private fun validate(){
//        if (isNotNullDesc()) {
//            if (isNotNullImage()) {
//                addStory()
//            } else {
//                Toast.makeText(this, getString(R.string.validate_image), Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            if (isNotNullImage()) {
//                Toast.makeText(this, getString(R.string.validate_desc), Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, getString(R.string.validate_all), Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (!allPermissionsGranted()) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.no_permission),
//                    Toast.LENGTH_SHORT
//                ).show()
//                finish()
//            }
//        }
//    }
//
//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun addStory() {
//        if (getFile != null) {
//
//            val file = reduceFileImage(getFile as File)
//
//            val desc = binding.inputDesc.text.toString().toRequestBody("image/plain".toMediaType())
//            val photo = file.asRequestBody("image/jpeg".toMediaType())
//            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                "photo",
//                file.name,
//                photo
//            )
//
//            mainViewModel.apply {
//                isLoading.observe(this@AddStoryActivity) { isLoading ->
//                    showLoading(isLoading)
//                }
//                message.observe(this@AddStoryActivity) { msg ->
//                    Toast.makeText(this@AddStoryActivity, msg, Toast.LENGTH_SHORT).show()
//                }
//                token.observe(this@AddStoryActivity) { token ->
//                    addStory(desc, imageMultipart, token)
//                }
//            }
//        } else {
//            Toast.makeText(
//                this@AddStoryActivity,
//                getString(R.string.add_picture_first),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//        val intent = Intent(this, StoryActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        finish()
//        startActivity(intent)
//    }
//
//    private fun startCameraX() {
//        val intent = Intent(this, CameraActivity::class.java)
//        launcherIntentCameraX.launch(intent)
//    }
//
//    private val launcherIntentCameraX = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) {
//        if (it.resultCode == CAMERA_X_RESULT) {
//            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                it.data?.getSerializableExtra("picture", File::class.java)
//            } else {
//                @Suppress("DEPRECATION")
//                it.data?.getSerializableExtra("picture")
//            } as? File
//            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
//            myFile?.let { file ->
//                rotateFile(file, isBackCamera)
//                getFile = file
//                binding.imageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
//            }
//        }
//    }
//
//
//    private fun startGallery() {
//        val intent = Intent()
//        intent.action = ACTION_GET_CONTENT
//        intent.type = "image/*"
//        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
//        launcherIntentGallery.launch(chooser)
//    }
//
//    private val launcherIntentGallery = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == RESULT_OK) {
//            val selectedImg = result.data?.data as Uri
//            selectedImg.let { uri ->
//                val myFile = uriToFile(uri, this@AddStoryActivity)
//                getFile = myFile
//                binding.imageView.setImageURI(uri)
//            }
//        }
//    }
//
//    private fun showLoading(isLoading: Boolean) {
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }
//
//    companion object {
//        const val CAMERA_X_RESULT = 200
//
//        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
//        private const val REQUEST_CODE_PERMISSIONS = 10
//    }
//
//}