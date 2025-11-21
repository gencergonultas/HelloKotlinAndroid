package com.oreilly.hellokotlin

import com.wireguard.android.backend.Tunnel
import com.wireguard.android.backend.Tunnel.State

class MyTunnel(private val name: String) : Tunnel {
    override fun getName() = name
    
    override fun onStateChange(newState: State) {
        // Bağlantı durumu değiştiğinde burası tetiklenir (Bağlandı/Ayrıldı).
        // Burada kullanıcı arayüzünü güncelleyebilirsiniz.
        println("Tünel Durumu Değişti: $name -> $newState")
    }
}
