package com.example.myapplication

import kotlinx.serialization.Serializable

@Serializable
data class Voucher(
    val id: Int,
    val san: String,
    val ten: String,
    val ma: String,
    val giam: String,
    val dieuKien: String,
    val hanSuDung: String,
    val link: String
)