package com.viniefortes.jogodavelha

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.appodeal.ads.Appodeal

class EntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

       // Appodeal.initialize(this, "298fb5c526f171dae5a84df6e9f1821521ec54ac8d1b0215", Appodeal.BANNER, true)
        Appodeal.setBannerViewId(R.id.appodealBannerView)
        Appodeal.show(this, Appodeal.BANNER_VIEW)

        val multiplayer: Button = findViewById(R.id.multilayer)
        multiplayer.setOnClickListener {
            nomearPlayers()
        }
    }

    private fun nomearPlayers() {
        val dialog = Dialog(this@EntryActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.multiplayername)
        val body = dialog.findViewById(R.id.body) as TextView
        body.text = title
        val playernome1 = dialog.findViewById(R.id.play1name) as EditText
        val playernome2 = dialog.findViewById(R.id.play2name) as EditText
        val yesBtn = dialog.findViewById(R.id.yesBtn) as Button
        yesBtn.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, Multiplayer::class.java)
            intent.putExtra("NOME1", playernome1.text.toString())
            intent.putExtra("NOME2", playernome2.text.toString())
            startActivity(intent)
        }
        dialog.show()
    }
}