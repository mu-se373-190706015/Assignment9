package com.example.assignment9

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import android.widget.VideoView
import com.example.assignment9.databinding.ActivityVideoBinding

import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class Video : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestMultiplePermissions()

        //Button click listeners
        binding.videobutton.setOnClickListener {
            takeVideo()
        }
        binding.opengallery.setOnClickListener {
            chooseVideo()
        }
        binding.takephoto.setOnClickListener {
            startActivity(Intent(this,PhotoActivity::class.java))
            this.finish()
        }


    }

    private val GALLERY = 1
    private val CAMERA = 2
    fun chooseVideo() {
        //Opening Gallery
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takeVideo() {
        //Opening Camera
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Choosen media shown on video view
        var videoView = findViewById(R.id.videoView) as VideoView
        videoView.visibility= View.VISIBLE
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY) {

            if (data != null) {
                val contentURI = data!!.data
                videoView.setVideoURI(contentURI)
                videoView.requestFocus()
                videoView.start()

            }

        } else if (requestCode == CAMERA) {

            val contentURI = data!!.data

            videoView.setVideoURI(contentURI)
            videoView.requestFocus()
            videoView.start()
        }
    }


    private fun requestMultiplePermissions() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                    if (report.areAllPermissionsGranted()) {
                        Toast.makeText(
                            applicationContext,
                            "All permissions are granted by user!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(
                    applicationContext,
                    "Some Error! ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .onSameThread()
            .check()
    }
}