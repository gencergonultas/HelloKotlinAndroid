package com.oreilly.hellokotlin

import android.content.Context
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.Tunnel
import com.wireguard.config.Config
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

object VpnManager {
    private var backend: GoBackend? = null

    fun init(context: Context) {
        if (backend == null) {
            backend = GoBackend(context)
        }
    }

    @Throws(Exception::class)
    fun connect(interfaceName: String, configText: String, context: Context) {
        init(context)
        val inputStream = ByteArrayInputStream(configText.toByteArray(StandardCharsets.UTF_8))
        val config = Config.parse(inputStream)
        
        val tunnel = MyTunnel(interfaceName)
        backend?.setState(tunnel, Tunnel.State.UP, config)
    }

    fun disconnect(interfaceName: String) {
        val tunnel = MyTunnel(interfaceName)
        backend?.setState(tunnel, Tunnel.State.DOWN, null)
    }
}
