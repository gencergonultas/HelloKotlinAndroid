package com.oreilly.hellokotlin

import android.content.Intent
import android.net.VpnService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.oreilly.hellokotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    
    // BURAYA KENDİ SUNUCU BİLGİLERİNİ GİRMELİSİN
    private val configString = """
        [Interface]
        PrivateKey = <BURAYA_CLIENT_PRIVATE_KEY_GELECEK>
        Address = 10.200.200.2/32 
        DNS = 1.1.1.1

        [Peer]
        PublicKey = <BURAYA_SERVER_PUBLIC_KEY_GELECEK>
        AllowedIPs = 0.0.0.0/0
        Endpoint = <SUNUCU_IP_ADRESI_VEYA_DOMAIN>:51820
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mevcut helloButton işlevini VPN bağlantısı ile değiştirdik
        binding.helloButton.text = "VPN Bağlan" // Varsayalım butonun adı bu olsun
        binding.helloButton.setOnClickListener(this::toggleVpn)
    }

    @Suppress("UNUSED_PARAMETER")
    fun toggleVpn(v: View?) {
        if (binding.helloButton.text == "VPN Bağlan") {
            tryConnect()
        } else {
            VpnManager.disconnect("wg0") 
            binding.helloButton.text = "VPN Bağlan"
            Toast.makeText(this, "Bağlantı kesildi.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tryConnect() {
        // VPN izni istemek zorunludur
        val intent = VpnService.prepare(this)
        if (intent != null) {
            startActivityForResult(intent, 0)
        } else {
            connectVpn()
        }
    }

    private fun connectVpn() {
        try {
            VpnManager.connect("wg0", configString, this)
            binding.helloButton.text = "Bağlantıyı Kes"
            Toast.makeText(this, "VPN bağlanıyor...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Bağlantı hatası: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            connectVpn()
        } else if (requestCode == 0) {
            Toast.makeText(this, "VPN bağlantı izni reddedildi.", Toast.LENGTH_SHORT).show()
        }
    }
}
