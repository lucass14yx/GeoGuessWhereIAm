package com.example.geoguesswhereiam

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.geoguesswhereiam.databinding.ActivityVideoViewBinding

class VideoView : AppCompatActivity() {
    private lateinit var binding: ActivityVideoViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
            binding.botPlay.setOnClickListener {
            var mediaControls: MediaController = android.widget.MediaController(this)
            mediaControls.setAnchorView(binding.vv)

            // set the media controller for video view
            binding.vv.setMediaController(mediaControls)

            // set the absolute path of the video file which is going to be played
            binding.vv.setVideoURI(
                Uri.parse("android.resource://" + packageName
                        + "/" + R.raw.toledo)
            )
            binding.vv.requestFocus()

            // arranca the video
            binding.vv.start()

            // display a toast message after the video is completed
            binding.vv.setOnCompletionListener {
                Toast.makeText(
                    applicationContext, "Video completado",
                    Toast.LENGTH_LONG
                ).show()
            }

            // display a toast message if any error occurs while playing the video
            binding.vv.setOnErrorListener { mp, what, extra ->
                Toast.makeText(
                    applicationContext, "Ha ocurrido un errror " +
                            "mientros se reproduce el video !!!", Toast.LENGTH_LONG
                ).show()
                false
            }
        }

        binding.botPausar.setOnClickListener {

            //pausa la ejecución
            binding.vv.pause()

        }

        binding.botContinuar.setOnClickListener {
            //continua la ejecución por dónde iba si se había pausado
            binding.vv.start()
        }

        binding.botDetener.setOnClickListener {
            //detiene completamente
            binding.vv.stopPlayback()
        }
    }
}