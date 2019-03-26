package com.mingyangzeng.ffmpgedemo

import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView


class Play : SurfaceHolder.Callback {

    private var surfaceView: SurfaceView? = null

    val totalTime: Int
        external get

    val currentPosition: Double
        external get

    fun playJava(path: String) {
        if (surfaceView == null) {
            return
        }
        play(path)
    }

    fun setSurfaceView(surfaceView: SurfaceView) {
        this.surfaceView = surfaceView
        display(surfaceView.holder.surface)
        surfaceView.holder.addCallback(this)
    }

    external fun play(path: String): Int

    external fun display(surface: Surface)

    external fun release()

    external fun stop()

    external fun seekTo(msec: Int)

    external fun stepBack() //快退

    external fun stepUp() //快进


    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        display(surfaceHolder.surface)
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {

    }

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}
