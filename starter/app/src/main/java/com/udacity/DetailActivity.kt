package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.cancelAll()
        val data = intent.extras
        if (data != null)
        {
            file.text = data.getString("title")
            status.text = data.getString("downloadStatus")
        }
        button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
