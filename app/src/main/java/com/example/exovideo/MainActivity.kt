package com.example.exovideo

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.app.hngr.Utils.ExoPlayerItem
import com.example.exovideo.databinding.ActivityMainBinding
import com.example.exovideo.databinding.HomepageItemsBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class MainActivity : AppCompatActivity() {
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var binding: ActivityMainBinding
     var exoplayermain : ExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val list = arrayListOf<String>()
        list.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        list.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
        list.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")

        list.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        list.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
        list.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")
//
        list.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        list.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
        list.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")


        setUpViewPager(list)

    }

    fun setUpViewPager(list : ArrayList<String>) {
        viewPagerAdapter = ViewPagerAdapter(
            list,
            object : ViewPagerAdapter.OnvideoPrepareListener {
                override fun onVideoPrepared(exoplayerItem: ExoPlayerItem, position: Int) {
                    exoPlayerItems.add(exoplayerItem)
                }


            })
        binding.viewpager2.adapter = viewPagerAdapter
        binding.viewpager2.offsetTopAndBottom(1)





        binding.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                try {

                    if(exoplayermain!=null){
                        exoplayermain!!.stop()
                        exoplayermain!!.release()
                        exoplayermain!!.clearMediaItems()
                    }
                    exoplayermain  = ExoPlayer.Builder(applicationContext).build()

                    viewPagerAdapter.setPlayer(position,exoplayermain!!)
                    val dataSourceFactory = DefaultDataSource.Factory(applicationContext)
                    val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                        MediaItem.fromUri(Uri.parse(list[position])))
                    exoplayermain!!.setMediaSource(mediaSource)
                    exoplayermain!!.prepare()
                    exoplayermain!!.playWhenReady = true
                    exoplayermain!!.play()


                }catch (e : Exception){
                    e.printStackTrace()
                }
            }

        })


    }
    class ViewPagerAdapter(val list : ArrayList<String>,var onvideoPrepareListener: OnvideoPrepareListener) :
        RecyclerView.Adapter<ViewPagerAdapter.ViewPagerDataHolder>() {

        val listholder = ArrayList<ViewPagerDataHolder>()
        var i = 0
        lateinit var viewHolder: ViewPagerDataHolder

        fun setPlayer(position: Int,player: ExoPlayer){
            Log.e("as@1233", "setPlayer: $position", )
            listholder[position].binding.exoplayer1.player=player
//            viewHolder.binding.exoplayer1.player=player

        }

        inner class ViewPagerDataHolder(itemview : View, val context: Context, var onvideoPrepareListener: OnvideoPrepareListener) : RecyclerView.ViewHolder(itemview) {
            val binding  = HomepageItemsBinding.bind(itemview.rootView)

            fun setVideoPath(index: Int){
                onvideoPrepareListener.onVideoPrepared(ExoPlayerItem(absoluteAdapterPosition), index)
            }





        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerDataHolder {

            return ViewPagerDataHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.homepage_items,parent,false),parent.context,onvideoPrepareListener
            )
        }

        override fun onBindViewHolder(holder: ViewPagerDataHolder, position: Int) {

            viewHolder = holder
                holder.setVideoPath(position)
                listholder.add(holder)

        }

        override fun getItemCount(): Int {
            return list.size
        }


        interface OnvideoPrepareListener{
            fun onVideoPrepared(exoplayerItem : ExoPlayerItem, position: Int)
        }



    }

}