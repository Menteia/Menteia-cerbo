package xyz.trankvila.menteia.datumo

import kotlinx.serialization.Serializable

@Serializable
data class HueResponse(
        val state: LightBulbState
)

@Serializable
data class LightBulbState(
        val on: Boolean,
        val bri: Int,
        val alert: String,
        val mode: String,
        val reachable: String
)