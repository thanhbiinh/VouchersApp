
package com.example.myapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

@Serializable
data class Vouchers(
    val id: Int,
    val san: String,
    val ten: String,
    val ma: String,
    val giam: String,
    val dieuKien: String,
    val hanSuDung: String
)

suspend fun loadVouchers(): List<Voucher> = withContext(Dispatchers.IO) {

    val url =
        "https://raw.githubusercontent.com/thanhbiinh/VouchersApp/main/vouchers.json"

    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .build()

    val response = client.newCall(request).execute()

    if (!response.isSuccessful) {
        throw Exception("Lỗi HTTP: ${response.code}")
    }

    val json = response.body?.string()
        ?: throw Exception("Không có dữ liệu")

    Json {
        ignoreUnknownKeys = true
    }.decodeFromString(json)
}