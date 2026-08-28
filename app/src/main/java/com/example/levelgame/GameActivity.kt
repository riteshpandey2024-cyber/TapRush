package com.example.levelgame

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LEVEL = "extra_level"
    }

    // Level configuration: targets needed to win, and time limit (seconds) for each level.
    // Feel free to tweak these numbers to change difficulty.
    private val levelConfig = mapOf(
        1 to LevelSettings(targetsToWin = 10, timeSeconds = 30),
        2 to LevelSettings(targetsToWin = 15, timeSeconds = 25),
        3 to LevelSettings(targetsToWin = 20, timeSeconds = 20)
    )

    private data class LevelSettings(val targetsToWin: Int, val timeSeconds: Int)

    private lateinit var tvTimer: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvLevelTitle: TextView
    private lateinit var playArea: FrameLayout
    private lateinit var btnTarget: Button

    private var level = 1
    private var score = 0
    private var settings = LevelSettings(10, 30)
    private var countDownTimer: CountDownTimer? = null
    private var gameEnded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        level = intent.getIntExtra(EXTRA_LEVEL, 1)
        settings = levelConfig[level] ?: levelConfig[1]!!

        tvTimer = findViewById(R.id.tvTimer)
        tvScore = findViewById(R.id.tvScore)
        tvLevelTitle = findViewById(R.id.tvLevelTitle)
        playArea = findViewById(R.id.playArea)
        btnTarget = findViewById(R.id.btnTarget)

        tvLevelTitle.text = "LEVEL $level"
        updateScoreText()
        tvTimer.text = "Time: ${settings.timeSeconds}"

        btnTarget.setOnClickListener {
            if (gameEnded) return@setOnClickListener
            score++
            updateScoreText()
            moveTargetRandomly()
            if (score >= settings.targetsToWin) {
                endGame(won = true)
            }
        }

        // Wait for layout pass so playArea has real width/height, then place target and start timer.
        playArea.post {
            moveTargetRandomly()
            startTimer()
        }
    }

    private fun updateScoreText() {
        tvScore.text = "Score: $score / ${settings.targetsToWin}"
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(settings.timeSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000L).toInt() + 1
                tvTimer.text = "Time: $secondsLeft"
            }

            override fun onFinish() {
                tvTimer.text = "Time: 0"
                if (!gameEnded) {
                    endGame(won = false)
                }
            }
        }.start()
    }

    private fun moveTargetRandomly() {
        val areaWidth = playArea.width
        val areaHeight = playArea.height
        val btnWidth = btnTarget.width.takeIf { it > 0 } ?: 200
        val btnHeight = btnTarget.height.takeIf { it > 0 } ?: 200

        if (areaWidth <= btnWidth || areaHeight <= btnHeight) return

        val maxX = areaWidth - btnWidth
        val maxY = areaHeight - btnHeight
        val newX = Random.nextInt(0, maxX)
        val newY = Random.nextInt(0, maxY)

        val params = FrameLayout.LayoutParams(btnTarget.layoutParams)
        params.leftMargin = newX
        params.topMargin = newY
        btnTarget.layoutParams = params
    }

    private fun endGame(won: Boolean) {
        gameEnded = true
        countDownTimer?.cancel()
        btnTarget.isEnabled = false

        if (won) {
            Prefs.unlockNextLevel(this, level)
        }

        val nextLevel = level + 1
        val hasNextLevel = level < Prefs.MAX_LEVEL

        val message = if (won) {
            if (hasNextLevel) "You tapped ${settings.targetsToWin} targets in time!\nLevel ${nextLevel} unlocked."
            else "You tapped ${settings.targetsToWin} targets in time!\nYou beat the final level!"
        } else {
            "Time's up! You reached $score / ${settings.targetsToWin}.\nTry again."
        }

        val builder = AlertDialog.Builder(this)
            .setTitle(if (won) "Level $level Complete!" else "Level $level Failed")
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton("Levels") { _, _ ->
                finish()
            }

        if (won && hasNextLevel) {
            builder.setPositiveButton("Next Level") { _, _ ->
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra(EXTRA_LEVEL, nextLevel)
                startActivity(intent)
                finish()
            }
        } else {
            builder.setPositiveButton("Retry") { _, _ ->
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra(EXTRA_LEVEL, level)
                startActivity(intent)
                finish()
            }
        }

        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
