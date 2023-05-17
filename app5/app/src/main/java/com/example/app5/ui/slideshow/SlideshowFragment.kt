package com.example.app5.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app5.MyService
import com.example.app5.R
import com.example.app5.databinding.FragmentSlideshowBinding
import kotlin.time.Duration.Companion.milliseconds


class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.nowplaying.visibility = View.GONE
        val songs = intArrayOf(R.raw.nevergonna, R.raw.whatislove)
        var i = 0

        binding.pause.visibility = View.GONE
        binding.go.setOnClickListener{
            var intent = Intent(context, MyService::class.java)
            intent.putExtra("song", songs[i])
            activity!!.startService(intent)
            binding.pause.visibility = View.VISIBLE
            binding.go.visibility = View.GONE
            binding.nowplaying.visibility = View.VISIBLE
            binding.songname.text = resources.getResourceEntryName(songs[i])
        }
        binding.pause.setOnClickListener{
            activity!!.stopService(Intent(context, MyService::class.java))
            binding.pause.visibility = View.GONE
            binding.go.visibility = View.VISIBLE
            Toast.makeText(context, i.toString(), Toast.LENGTH_SHORT).show()
        }
        binding.next.setOnClickListener{
            if(i+1 != songs.count()){
                activity!!.stopService(Intent(context, MyService::class.java))

                var intent = Intent(context, MyService::class.java)
                intent.putExtra("song", songs[++i])
                activity!!.startService(intent)
            }
            binding.songname.text = resources.getResourceEntryName(songs[i])
        }
        binding.prev.setOnClickListener{
            if(i != 0){
                activity!!.stopService(Intent(context, MyService::class.java))

                var intent = Intent(context, MyService::class.java)
                intent.putExtra("song", songs[--i])
                activity!!.startService(intent)
            }
            binding.songname.text = resources.getResourceEntryName(songs[i])
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}