package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VoucherApp()
        }
    }
}

@Composable
fun VoucherApp() {

    var vouchers by remember {
        mutableStateOf<List<Voucher>>(emptyList())
    }

    LaunchedEffect(Unit) {
        vouchers = loadVouchers()
    }

    // Sàn đang chọn
    var sanDangChon by remember {
        mutableStateOf("Tất cả")
    }

    // Từ khóa tìm kiếm
    var tuKhoa by remember {
        mutableStateOf("")
    }

    // Lọc voucher
    val vouchersHienThi = vouchers.filter { voucher ->

        val dungSan =
            sanDangChon == "Tất cả" ||
                    voucher.san == sanDangChon

        val dungTuKhoa =
            voucher.ten.contains(
                tuKhoa,
                ignoreCase = true
            ) ||
                    voucher.ma.contains(
                        tuKhoa,
                        ignoreCase = true
                    )

        dungSan && dungTuKhoa
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // Ô tìm kiếm
        TextField(
            value = tuKhoa,
            onValueChange = {
                tuKhoa = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = {
                Text("Tìm voucher...")
            }
        )

        // Chọn sàn
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    sanDangChon = "Tất cả"
                }
            ) {
                Text("Tất cả")
            }

            Button(
                onClick = {
                    sanDangChon = "Shopee"
                }
            ) {
                Text("Shopee")
            }

            Button(
                onClick = {
                    sanDangChon = "Lazada"
                }
            ) {
                Text("Lazada")
            }

            Button(
                onClick = {
                    sanDangChon = "TikTok Shop"
                }
            ) {
                Text("TikTok")
            }
        }

        // Danh sách voucher
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(vouchersHienThi) { voucher ->

                VoucherCard(
                    voucher = voucher
                )
            }
        }
    }
}