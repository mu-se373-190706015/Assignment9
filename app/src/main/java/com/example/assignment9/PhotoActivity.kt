package com.example.assignment9

import android.app.Activity

import android.content.Intent
import android.graphics.Bitmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.provider.MediaStore

import android.widget.ImageView

import com.example.assignment9.databinding.ActivityMainBinding

class PhotoActivity : AppCompatActivity() {

    var choose = 0


    var REQUEST_CODE = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.takepicture.setOnClickListener {
            choose = 0
            REQUEST_CODE = 200
            capturePhoto()

        }
        binding.opengallery.setOnClickListener {
            choose = 1
            REQUEST_CODE = 100
            openGallery()

        }

        binding.videobutton.setOnClickListener {
            startActivity(Intent(this, Video::class.java))
            this.finish()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageView = findViewById<ImageView>(R.id.imageView)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {

            if (choose == 0) {
                MediaStore.Images.Media.insertImage(
                    contentResolver,
                    data.extras?.get("data") as Bitmap,
                    "epic",
                    "epic"
                )
            } else {

                imageView.setImageURI(data?.data)
            }
        }
    }

    fun capturePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }


}