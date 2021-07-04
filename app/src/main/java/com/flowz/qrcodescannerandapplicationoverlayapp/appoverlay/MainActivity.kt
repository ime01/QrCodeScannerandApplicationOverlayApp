package com.flowz.qrcodescannerandapplicationoverlayapp.appoverlay

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.flowz.qrcodescannerandapplicationoverlayapp.appoverlay.OverlayService
import com.flowz.qrcodescannerandapplicationoverlayapp.databinding.ActivityMainBinding
import com.flowz.qrcodescannerandapplicationoverlayapp.qrscanner.QrScannerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var canDraw = true

        var intent : Intent? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            canDraw = Settings.canDrawOverlays(this)

            if (!canDraw && intent != null){
                startActivity(intent)
            }
        }



        binding.apply {

            start.setOnClickListener {
                val service = Intent(this@MainActivity, OverlayService::class.java)
                startService(service)
            }
            stop.setOnClickListener {
                val service = Intent(this@MainActivity, OverlayService::class.java)
                stopService(service)
            }
            qrscanner.setOnClickListener {
                val gotoQrScanner = Intent(this@MainActivity, QrScannerActivity::class.java)
                startActivity(gotoQrScanner)
            }
        }
    }
}