package com.flowz.qrcodescannerandapplicationoverlayapp.appoverlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import com.flowz.qrcodescannerandapplicationoverlayapp.R

class OverlayService : Service(), View.OnTouchListener, View.OnClickListener{

    private var moving = false
    private var initialTouchX = 0.0f
    private var initialTouchY = 0.0f
    private var initialY = 0
    private var initialX = 0
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var overlayButton: ImageButton
    private lateinit var windowManager: WindowManager

    override fun onCreate() {
        super.onCreate()

        Toast.makeText(this, "Overlay Service Created", Toast.LENGTH_LONG).show()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        overlayButton = ImageButton(this)
        overlayButton.setImageResource(R.drawable.ic_launcher_foreground)
        overlayButton.setOnClickListener(this)

        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }else{
            WindowManager.LayoutParams.TYPE_PHONE
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START

        params.x = 0
        params.y = 100

        windowManager.addView(overlayButton,params)

    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayButton)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {

        view!!.performClick()

        when(event!!.action){
            MotionEvent.ACTION_DOWN->{
                initialX = params.x
                initialY = params.y
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                moving = true

            }
            MotionEvent.ACTION_UP->{
               moving = false
            }

            MotionEvent.ACTION_MOVE->{
                params.x = initialX + (event.rawX - initialTouchX).toInt()
                params.y = initialY + (event.rawY - initialTouchY).toInt()
                windowManager.updateViewLayout(overlayButton, params)
            }

        }
        return true
    }


    override fun onClick(v: View?) {
        if (!moving)Toast.makeText(this, "Overlay Image Touched", Toast.LENGTH_LONG).show()

    }
}