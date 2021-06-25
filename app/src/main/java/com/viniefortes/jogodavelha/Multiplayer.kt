package com.viniefortes.jogodavelha

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.appodeal.ads.Appodeal

private val buttons = Array(3) {arrayOfNulls<Button>(3)}
private var player1Turn = true
private var roundCount = 0
private var player1Points = 0
private var player2Points = 0
@SuppressLint("StaticFieldLeak")
private lateinit var textViewPlayer1: TextView
@SuppressLint("StaticFieldLeak")
private lateinit var textViewPlayer2: TextView
@SuppressLint("StaticFieldLeak")
private lateinit var bntReset: Button

private var nome1: String? = null
private var nome2: String? = null

class Multiplayer : AppCompatActivity(),  View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)

       // Appodeal.initialize(this, "298fb5c526f171dae5a84df6e9f1821521ec54ac8d1b0215", Appodeal.BANNER, true)
        Appodeal.initialize(this, "298fb5c526f171dae5a84df6e9f1821521ec54ac8d1b0215", Appodeal.INTERSTITIAL, true)

        Appodeal.setBannerViewId(R.id.appodealBannerView)
        Appodeal.show(this, Appodeal.BANNER_VIEW)

        val nomeplayer1 = findViewById<TextView>(R.id.nomeplayer1)
        val nomeplayer2 = findViewById<TextView>(R.id.nomeplayer2)

        nome1 = intent.getStringExtra("NOME1")
        nome2 =  intent.getStringExtra("NOME2")

        if (nome1?.isEmpty() == true) {
            nomeplayer1.text = getString(R.string.jogador1)
        } else {
            nomeplayer1.text = nome1
        }
        if (nome2?.isEmpty() == true) {
            nomeplayer2.text = getString(R.string.jogador2)
        } else {
            nomeplayer2.text = nome2
        }

        textViewPlayer1 = findViewById(R.id.text_view_p1)
        textViewPlayer2 = findViewById(R.id.text_view_p2)
        bntReset = findViewById(R.id.button_reset)

        bntReset.setOnClickListener {
            resetGame()
        }

        for (i in 0..2) {
            for (j in 0..2) {
                val buttonID = "button_$i$j"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                buttons[i][j] = findViewById(resID)
                buttons[i][j]?.setOnClickListener(this)
            }
        }

    }

    override fun onClick(v: View?) {
        if ((v as Button).text.toString() != "") {
            return
        }
        if (player1Turn) {
            v.text = "X"
            v.setTextColor(Color.parseColor("#0000ff"))
        } else {
            v.text = "O"
            v.setTextColor(Color.parseColor("#ff0000"))
        }
        roundCount++
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins()
            } else {
                player2Wins()
            }
        } else if (roundCount == 9) {
            draw()
        } else {
            player1Turn = !player1Turn
        }
    }
    private fun checkForWin(): Boolean {
        val field = Array(3) {
            arrayOfNulls<String>(
                3
            )
        }
        for (i in 0..2) {
            for (j in 0..2) {
                field[i][j] = buttons[i][j]!!.text.toString()
            }
        }
        for (i in 0..2) {
            if (field[i][0] == field[i][1] && field[i][0] == field[i][2] && field[i][0] != "") {
                return true
            }
        }
        for (i in 0..2) {
            if (field[0][i] == field[1][i] && field[0][i] == field[2][i] && field[0][i] != "") {
                return true
            }
        }
        if (field[0][0] == field[1][1] && field[0][0] == field[2][2] && field[0][0] != "") {
            return true
        }
        return field[0][2] == field[1][1] && field[0][2] == field[2][0] && field[0][2] != ""
    }
    private fun player1Wins() {
        player1Points++
        dialog(nome1 + getString(R.string.venceu))
    }

    private fun player2Wins() {
        player2Points++
        dialog(nome2 + getString(R.string.venceu))
    }

    private fun draw() {
        dialog(getString(R.string.empate))
    }

    private fun updatePointsText() {
        textViewPlayer1.text = "$player1Points"
        textViewPlayer2.text = "$player2Points"
    }

    private fun resetBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]!!.text = ""
            }
        }
        roundCount = 0
        player1Turn = true
    }

    private fun resetGame() {
        player1Points = 0
        player2Points = 0
        updatePointsText()
        resetBoard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("roundCount", roundCount)
        outState.putInt("player1Points", player1Points)
        outState.putInt("player2Points", player2Points)
        outState.putBoolean("player1Turn", player1Turn)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        roundCount = savedInstanceState.getInt("roundCount")
        player1Points = savedInstanceState.getInt("player1Points")
        player2Points = savedInstanceState.getInt("player2Points")
        player1Turn = savedInstanceState.getBoolean("player1Turn")
    }
    private fun dialog(resultado: String) {
        val dialogBuilder = AlertDialog.Builder(this@Multiplayer)
        dialogBuilder.setMessage(resultado)
            // if the dialog is cancelable
            .setCancelable(false)
            .setPositiveButton(getString(R.string.continuar)) { dialog, _ ->
                dialog.dismiss()
                resetBoard()
                updatePointsText()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(R.string.app_name)
        alert.show()

    }
    override fun onBackPressed() {
        showAdvertisement()
        finish()
    }

    private fun showAdvertisement() {
        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
            Appodeal.show(this, Appodeal.INTERSTITIAL)
        } else {
            finish()
        }
    }
}