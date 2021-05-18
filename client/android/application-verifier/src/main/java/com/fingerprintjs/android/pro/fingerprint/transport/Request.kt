package com.fingerprintjs.android.pro.fingerprint.transport


interface Request {
    val url: String
    val type: String
    val headers: Map<String, String>
    fun bodyAsMap(): Map<String, Any>
}