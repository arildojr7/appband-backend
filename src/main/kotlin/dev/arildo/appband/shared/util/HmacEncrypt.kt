package dev.arildo.appband.shared.util

import java.security.InvalidKeyException
import java.security.SignatureException
import java.util.Formatter
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val HMAC_SHA1_ALGORITHM = "HmacSHA1"

private fun toHexString(bytes: ByteArray): String {
    val formatter = Formatter()
    for (b in bytes) { formatter.format("%02x", b) }

    return formatter.toString()
}

@Throws(SignatureException::class, InvalidKeyException::class)
fun encryptHmac(data: String, key: String?): String {
    val signingKey = SecretKeySpec(key?.toByteArray(), HMAC_SHA1_ALGORITHM)
    val mac = Mac.getInstance(HMAC_SHA1_ALGORITHM)
    mac.init(signingKey)
    return toHexString(mac.doFinal(data.toByteArray()))
}