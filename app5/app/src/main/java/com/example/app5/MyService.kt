package com.example.app5

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MyService: Service() {
    private lateinit var player: MediaPlayer
    override fun onStartCommand(init: Intent, flag: Int, startId: Int): Int{
        var song = init!!.getIntExtra("song", R.raw.nevergonna)
        player = MediaPlayer.create(this, song)
        player.isLooping = true
        player.currentPosition
        player.start()
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}