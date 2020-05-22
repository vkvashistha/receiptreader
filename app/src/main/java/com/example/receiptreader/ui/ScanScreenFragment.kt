package com.example.receiptreader.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.receiptreader.R
import com.example.receiptreader.data.Item
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.scan_fragment.*


class ScanScreenFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {
        fun newInstance() = ScanScreenFragment()
    }

    private val readExternalStorageRequestCode = 0
    private val photoUploadRequestCode = 1
    private val CAMERA_REQUEST = 1888
    private val MY_CAMERA_PERMISSION_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scan_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        scan_btn.setOnClickListener {
            if (checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
            } else {
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        }
        upload_btn.setOnClickListener {
            pickImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show()
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            var photo = data!!.extras!!.get("data") as Bitmap
            scan_img.setImageBitmap(photo)
            runTextRecognition(photo)
        } else if (requestCode == readExternalStorageRequestCode && resultCode == Activity.RESULT_OK) {
//            var photo = data!!.extras!!.get("data") as Bitmap
//            scan_img.setImageBitmap(photo)

            val photo = BitmapFactory.decodeFile(getPath(context, data!!.data!!))
            scan_img.setImageBitmap(photo)
//            scan_img.setImageURI(data!!.extras!!.get("data") as Uri)
            runTextRecognition(photo)
        }
    }

    private fun getPath(context: Context?, uri: Uri): String? {
        // Will return "image:x*"
        val wholeID = DocumentsContract.getDocumentId(uri)

        // Split at colon, use second item in the array
        val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val column = arrayOf(MediaStore.Images.Media.DATA)

        // where id is equal to
        val sel = MediaStore.Images.Media._ID + "=?"

        val cursor = this.requireContext().applicationContext.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            column, sel, arrayOf(id), null
        )

        var filePath: String? = null
        val columnIndex = cursor!!.getColumnIndex(column[0])
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }
        cursor.close()
        return filePath
    }

    private fun pickImage() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(
                Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            startActivityForResult(intent, readExternalStorageRequestCode)
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                readExternalStorageRequestCode
            )
        }
    }

    private fun runTextRecognition(photo: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(photo)
        val recognizer = FirebaseVision.getInstance()
            .onDeviceTextRecognizer
//        mTextButton.setEnabled(false)
        recognizer.processImage(image)
            .addOnSuccessListener { texts ->
                //                mTextButton.setEnabled(true)
                processTextRecognitionResult(texts)
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                //                        mTextButton.setEnabled(true)
                e.printStackTrace()
            }
    }


    private fun processTextRecognitionResult(texts: FirebaseVisionText) {
        val blocks = texts.textBlocks
        if (blocks.size == 0) {
//            showToast("No text found")
            return
        }
        val blockList = ArrayList<ArrayList<String>>()
//        mGraphicOverlay.clear()
        Log.i("Text Block", blocks.toString())
        for (i in blocks.indices) {
            val block = ArrayList<String>()
            val lines = blocks[i].lines
            val text = StringBuilder()
            for (j in lines.indices) {
                val elements = lines[j].elements
                val lineText = StringBuilder()
                for (k in elements.indices) {
//                    val textGraphic = TextGraphic(mGraphicOverlay, elements[k])

                    text.append(" ").append(elements[k].text)
                    lineText.append(" ").append(elements[k].text)

//                    Log.i("Text Recognized-----", elements[k].text)

                }

                block.add(lineText.toString())
//                Log.i("Text Recognized--line--", lineText.toString())

            }
            blockList.add(block)
//            Log.i("Text Recognized", text.toString())
        }

        Log.i("Text Recognized", blockList.toString())
        parseData(blockList)
    }

    private fun processText(blockList: java.util.ArrayList<java.util.ArrayList<String>>) {
        var date = ""
        var itemList: ArrayList<String>? = null
        var amtList: ArrayList<String>? = null
        val responseList = ArrayList<Item>()
        var isItem = false
        var isAmt = false
        for (blocks in blockList) {
            for (item in blocks) {
                when {
                    item.toLowerCase().contains("date", true) -> date = item.toLowerCase().split("date:")[1]
                    item.toLowerCase().contains("item", true) -> {
                        itemList = ArrayList()
                        isItem = true
                    }
                    isItem -> itemList?.add(item)
                }


                if (item.toLowerCase().contains("amt", true)
                    || item.toLowerCase().contains("amount", true)
                ) {
                    amtList = ArrayList()
                    isAmt = true
                } else if(isAmt) amtList?.add(item)
            }
            isItem = false
            isAmt = false
        }

        when {
            amtList == null -> Log.i("Text Recognized", "Amount List is empty")
            itemList == null -> Log.i("Text Recognized", "Item List is empty")
            else -> {
                val size: Int = if (amtList.size < itemList.size) amtList.size else itemList.size
                for (i in 0 until size) {
                    responseList.add(
                        Item(
                            0,
                            itemList[i],
                            0.0,
                            null,
                            amtList[i].toDouble(),
                            date,
                            null,
                            null
                        )
                    )
                }
            }
        }
        Log.i("Text Recognized", responseList.toString())

        val bundle = Bundle()
        bundle.putSerializable("data", responseList)
        findNavController().navigate(R.id.actionLaunchReceiptReader, bundle)
    }

    fun parseData(blockList: java.util.ArrayList<java.util.ArrayList<String>>) {
        var date = ""
        var merchant = ""
        val itemList = ArrayList<String>()
        val amtList =  ArrayList<String>()
        val qtyList = ArrayList<String>()

        val responseList = ArrayList<Item>()

        val BLOCK_TYPE_ITEM_NAME = 0
        val BLOCK_TYPE_ITEM_QTY = 1
        val BLOCK_TYPE_ITEM_AMT = 2

        var blockType = -1
        for(block in blockList) {
            for(token in block) {
                when {
                    token.contains("date", true) -> date = token.split(":")[1]
                    token.contains("merchant", true) -> merchant = token.split(":")[1]
                    token.contains("item", true) -> blockType = BLOCK_TYPE_ITEM_NAME
                    token.contains("qty", true) -> blockType = BLOCK_TYPE_ITEM_QTY
                    token.contains("amt", true) -> blockType = BLOCK_TYPE_ITEM_AMT
                    else -> {
                        when(blockType) {
                            BLOCK_TYPE_ITEM_NAME -> itemList.add(token.trim())
                            BLOCK_TYPE_ITEM_AMT -> amtList.add(token.trim())
                            BLOCK_TYPE_ITEM_QTY -> qtyList.add(token.trim())
                        }
                    }
                }

            }
        }

        for(i in 0 until itemList.size) {
            responseList.add(Item(0,itemList[i], 1.0, null, amtList[i].toDouble(), date, null, merchant))
        }

        val bundle = Bundle()
        bundle.putSerializable("data", responseList)
        findNavController().navigate(R.id.actionLaunchReceiptReader, bundle)
    }

}
