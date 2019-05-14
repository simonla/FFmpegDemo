package com.mingyangzeng.ffmpgedemo

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import java.io.File
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    lateinit var davidPlayer: Play
    lateinit var surfaceView: SurfaceView
    lateinit var mTextView: TextView
    lateinit var mTextCurTime: TextView
    lateinit var mSeekBar: SeekBar
    internal var isSetProgress = false
    private val weakReference = WeakReference<MainActivity>(this)
    private val handler = MyHandler(weakReference)

    class MyHandler(weakReference: WeakReference<MainActivity>) : Handler() {
        private val thisRef = weakReference.get()
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == HIDE_CONTROL_LAYOUT) {
                thisRef?.refreshControl()
            } else {
                //  mTextCurTime.setText(formatTime(msg.what));
                if (thisRef != null) {
                    thisRef.mSeekBar.progress = msg.what
                }
            }
            // mSeekBar.setProgress(msg.what);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        surfaceView = findViewById<View>(R.id.surface) as SurfaceView
        davidPlayer = Play()
        davidPlayer.setSurfaceView(surfaceView)
        mTextView = findViewById(R.id.textview)
        mSeekBar = findViewById(R.id.seekBar)
        mTextCurTime = findViewById(R.id.tvcur)
        init()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    private fun init() {
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                //进度改变
                mTextCurTime.text = formatTime(seekBar.progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //开始拖动
                mTextCurTime.text = formatTime(seekBar.progress.toLong())
                isSetProgress = true
                refreshControl()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //停止拖动
                isSetProgress = false
                davidPlayer.seekTo(seekBar.progress)
                refreshControl()
            }
        })
    }

    fun player(view: View) {
        val path = intent.extras.getString("path")
        Log.d("MainActivity", path)
        val file = File(path)
        //   davidPlayer.playJava("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        davidPlayer.playJava(file.absolutePath)
        // mTextView.setText(davidPlayer.getTotalTime()+"");
        if (davidPlayer.totalTime != 0) {
            mTextView.text = formatTime((davidPlayer.totalTime / 1000).toLong())
            mSeekBar.max = davidPlayer.totalTime / 1000
            updateSeekBar()
        }
    }

    fun stop(view: View) {
        davidPlayer.release()
        // Toast.makeText(MainActivity.this,davidPlayer.getTotalTime()+"",Toast.LENGTH_SHORT).show();
        //
        //  mTextView.setText(formatTime(davidPlayer.getTotalTime()/1000));
    }

    fun pause(view: View) {
        davidPlayer.stop()
    }

    fun stepBack(view: View) {
        //快退
        davidPlayer.stepBack()
    }

    fun stepUp(view: View) {
        //快进
        davidPlayer.stepUp()
    }

    private fun formatTime(time: Long): String {
        val format = SimpleDateFormat("mm:ss")
        return format.format(time)
    }

    //更新进度
    fun updateSeekBar() {
        Thread(Runnable {
            while (true) {
                try {
                    val message = Message()
                    message.what = davidPlayer.currentPosition.toInt() * 1000
                    handler.sendMessage(message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }).start()
    }

    private fun refreshControl() {
        if (isSetProgress) {
            isSetProgress = false
        } else {
            isSetProgress = true
            handler.removeMessages(HIDE_CONTROL_LAYOUT)
            handler.sendEmptyMessageDelayed(HIDE_CONTROL_LAYOUT, 5000)
        }
    }

    companion object {
        val TAG = "MainActivity"
        private const val HIDE_CONTROL_LAYOUT = -1
    }
}
