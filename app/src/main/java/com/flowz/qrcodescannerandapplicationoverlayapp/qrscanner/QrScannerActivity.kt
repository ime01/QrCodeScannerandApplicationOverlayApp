package com.flowz.qrcodescannerandapplicationoverlayapp.qrscanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.flowz.qrcodescannerandapplicationoverlayapp.R
import com.flowz.qrcodescannerandapplicationoverlayapp.databinding.ActivityMainBinding
import com.flowz.qrcodescannerandapplicationoverlayapp.databinding.ActivityQrScannerBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import org.json.JSONException
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class QrScannerActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private lateinit var reveal: Animation
    private lateinit var hide: Animation
    private lateinit var binding: ActivityQrScannerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

         hide = AnimationUtils.loadAnimation(this@QrScannerActivity, android.R.anim.fade_out)
         reveal = AnimationUtils.loadAnimation(this@QrScannerActivity, android.R.anim.fade_in)


        binding.apply {

            textView.startAnimation(reveal)
            textView.setText("Scan Qr Code Here")
            cardview2.startAnimation(reveal)
            cardview2.visibility = View.VISIBLE


            btnScan.setOnClickListener {
                textView.startAnimation(reveal)
                cardview2.startAnimation(reveal)
                cardview1.startAnimation(hide)

                cardview2.visibility = View.VISIBLE
                cardview1.visibility = View.GONE
                textView.setText("Scan Qr Code Here")

                cameraTask()
            }

            cardview2.setOnClickListener {
                cameraTask()
            }

            btnEnter.setOnClickListener {
                if (edtCode.text.toString().isNullOrEmpty()){
                    Toast.makeText(this@QrScannerActivity, "Please enter code", Toast.LENGTH_LONG).show()
                }else{

                var value = edtCode.text.toString()
                Toast.makeText(this@QrScannerActivity, value, Toast.LENGTH_LONG).show()
                }
            }

            btnEnterScan.setOnClickListener {
                textView.startAnimation(reveal)
                cardview1.startAnimation(reveal)
                cardview2.startAnimation(hide)

                textView.setText("Enter Qr Code here")
                cardview2.visibility = View.GONE
                cardview1.visibility = View.VISIBLE

            }
        }
    }

    private fun hasCameraAccess(): Boolean{
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA)

    }

    private fun cameraTask(){

        if (hasCameraAccess()){

            var qrScanner = IntentIntegrator(this)
            qrScanner.setPrompt("Scan a QR code")
            qrScanner.setCameraId(0)
            qrScanner.setOrientationLocked(true)
            qrScanner.setBeepEnabled(true)
            qrScanner.captureActivity = CaptureActivity::class.java
            qrScanner.initiateScan()


        }else{
            EasyPermissions.requestPermissions(this, "This app needs access to your camera so you can take pictures", 123, android.Manifest.permission.CAMERA)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null){
            if (result.contents == null){
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show()
                binding.edtCode.setText("")
            }else{
                try {
                    binding.apply {
                        cardview1.startAnimation(reveal)
                        cardview2.startAnimation(hide)
                        cardview1.visibility = View.VISIBLE
                        cardview2.visibility = View.GONE
                        edtCode.setText(result.contents.toString())
                    }

                }catch (exception: JSONException){
                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
                    binding.edtCode.setText(result.contents.toString())
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }



    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
       if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
           AppSettingsDialog.Builder(this).build().show()
       }
    }

    override fun onRationaleAccepted(requestCode: Int) {

    }

    override fun onRationaleDenied(requestCode: Int) {

    }
}