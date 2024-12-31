package com.example.media_player_10

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaParser
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.media_player_10.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaplayer: MediaPlayer? = null
    private var audio: AudioManager? = null

    var track = 0
    var playlist: List<Int> = listOf()


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
          setContentView(binding.root)
          binding.toolbar.setTitle("Медиаплеер")
          binding.toolbar.setBackgroundColor(getColor(R.color.color_toolbar))
           setSupportActionBar(binding.toolbar)

        playlist = listOf<Int>(R.raw.metallica_enter_sandman,R.raw.metallica_the_unforgiven,R.raw.metallica_the_memory_remains,R.raw.metallica_the_unforgiven_2,R.raw.metallica_orion)

         audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        audio!!.setStreamVolume(AudioManager.STREAM_MUSIC,10,0)

        binding.textTrack.setText(resources.getResourceName(playlist[track]).toString().takeLastWhile { it != '/' })
        if ( audio != null ) {
         val maxvolume = audio!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
         val curentvalue = audio!!.getStreamVolume(AudioManager.STREAM_MUSIC)

             binding.seekBarValume.max = maxvolume
             binding.seekBarValume.progress = curentvalue


        }






                    if (track == 0) binding.buttonPrevious.setEnabled(false)


             binding.buttonPlay.setOnClickListener { playSound(track) }

             binding.buttonPause.setOnClickListener {
                 if (( mediaplayer == null ) or (binding.seekBarPlay.progress == binding.seekBarPlay.max)) return@setOnClickListener else { if (mediaplayer!!.isPlaying) mediaplayer?.pause() else mediaplayer!!.start()} }

             binding.buttonStop.setOnClickListener { if ( mediaplayer != null ) mediaplayerReset() }


             binding.seekBarPlay.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

                 override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                     if ( fromUser ) mediaplayer?.seekTo(progress) }

                 override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                 override fun onStopTrackingTouch(seekBar: SeekBar?) {}
             } )

             binding.seekBarValume.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                 override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean
                 ) {
                     if (fromUser) audio?.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0)
                 }

                 override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                 override fun onStopTrackingTouch(seekBar: SeekBar?) {} } )

                 binding.buttonMute.setOnClickListener { audio?.setStreamVolume(AudioManager.STREAM_MUSIC,0,0)
                                                          binding.seekBarValume.progress = 0
                 }



    }

    private fun mediaplayerReset() {
        mediaplayer?.stop(); mediaplayer?.reset(); mediaplayer?.release(); mediaplayer = null

        binding.seekBarPlay.progress = 0
    }

    @SuppressLint("SuspiciousIndentation")
    private fun playSound(track: Int) {

           if ( mediaplayer != null ) mediaplayerReset()
               mediaplayer = MediaPlayer.create(this,playlist[track]);

               inisializeSeekBar()
                mediaplayer?.start()

                 binding.textTrack.setText(resources.getResourceName(playlist[track]).toString().takeLastWhile { it != '/' })




    }

    private fun inisializeSeekBar() {

        binding.seekBarPlay.max = mediaplayer!!.duration
         val handler = Handler()

            handler.postDelayed( object: Runnable {

                override fun run() {
                    try { binding.seekBarPlay.progress = mediaplayer!!.currentPosition; handler.postDelayed(this,1000 ) } catch (e:Exception){ binding.seekBarPlay.progress = 0}
                }
                                                  },0)
    }






    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu) }

    @SuppressLint("SuspiciousIndentation")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val builder = AlertDialog.Builder(this)
            builder.setTitle("Выход из программы")
                .setPositiveButton("Да") { _,_ -> mediaplayer?.stop();finishAffinity() }
                .setNegativeButton("Нет") { _,_ ->  }
            builder.create().show()

        return super.onOptionsItemSelected(item)
    }

    fun onClick(view: View) {

        when ( view.id) {
            R.id.button_next -> track++
            R.id.button_previous -> track-- }
        binding.textTrack.setText(resources.getResourceName(playlist[track]).toString().takeLastWhile { it != '/' })

            if ( track == 0 ) binding.buttonPrevious.setEnabled(false) else binding.buttonPrevious.setEnabled(true)
            if ( track >= playlist.size-1) binding.buttonNext.setEnabled(false) else binding.buttonNext.setEnabled(true)
        playSound(track)
       // mediaplayerReset()



    }

    override fun onDestroy() {
        super.onDestroy()
        mediaplayerReset()
    }


}







