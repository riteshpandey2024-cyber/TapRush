package com.example.levelgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnLevel1: Button
    private lateinit var btnLevel2: Button
    private lateinit var btnLevel3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLevel1 = findViewById(R.id.btnLevel1)
        btnLevel2 = findViewById(R.id.btnLevel2)
        btnLevel3 = findViewById(R.id.btnLevel3)

        findViewById<Button>(R.id.btnReset).setOnClickListener {
            Prefs.resetProgress(this)
            refreshLevelButtons()
            Toast.makeText(this, "Progress reset", Toast.LENGTH_SHORT).show()
        }

        btnLevel1.setOnClickListener { startLevel(1) }
        btnLevel2.setOnClickListener { attemptStartLevel(2) }
        btnLevel3.setOnClickListener { attemptStartLevel(3) }
    }

    override fun onResume() {
        super.onResume()
        refreshLevelButtons()
    }

    private fun attemptStartLevel(level: Int) {
        val unlocked = Prefs.getUnlockedLevel(this)
        if (level <= unlocked) {
            startLevel(level)
        } else {
            Toast.makeText(
                this,
                "Locked! Finish Level ${level - 1} first.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startLevel(level: Int) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(GameActivity.EXTRA_LEVEL, level)
        startActivity(intent)
    }

    private fun refreshLevelButtons() {
        val unlocked = Prefs.getUnlockedLevel(this)

        btnLevel1.text = "LEVEL 1"
        btnLevel1.isEnabled = true

        btnLevel2.text = if (unlocked >= 2) "LEVEL 2" else "LEVEL 2  \uD83D\uDD12"
        btnLevel2.isEnabled = true

        btnLevel3.text = if (unlocked >= 3) "LEVEL 3" else "LEVEL 3  \uD83D\uDD12"
        btnLevel3.isEnabled = true
    }
}
