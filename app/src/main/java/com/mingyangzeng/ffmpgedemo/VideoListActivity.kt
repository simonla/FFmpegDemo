package com.mingyangzeng.ffmpgedemo

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class VideoListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("搜索视频中")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val paths = ArrayList<String>()
        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null
        )
        val index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        while (cursor.moveToNext()) {
            paths.add(cursor.getString(index))
        }
        cursor.close()
        progressDialog.dismiss()
        var rv = findViewById<RecyclerView>(R.id.rv)
        rv.adapter = VideoListAdapter(paths) { _, pos ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("path", paths[pos])
            startActivity(intent)
        }
    }

    class VideoListAdapter(val paths: ArrayList<String>, private val listener: (View, Int) -> Unit) :
        RecyclerView.Adapter<VideoListVH>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VideoListVH {
            return VideoListVH(LayoutInflater.from(p0.context).inflate(R.layout.item, p0, false))
        }

        override fun getItemCount() = paths.size

        override fun onBindViewHolder(p0: VideoListVH, p1: Int) {
            p0.textView.text = paths[p1]
            p0.textView.setOnClickListener {
                listener(it, p1)
            }
        }
    }

    class VideoListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv)
    }
}
