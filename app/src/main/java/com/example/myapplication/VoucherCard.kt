package com.example.myapplication

import android.content.Intent
import android.net.Uri

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun VoucherCard(voucher: Voucher) {

    val clipboardManager = LocalClipboardManager.current

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // Tên sàn
            Text(
                text = voucher.san,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // Tên voucher
            Text(
                text = voucher.ten,
                fontSize = 22.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // Mức giảm
            Text(
                text = "Giảm ${voucher.giam}",
                fontSize = 18.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // Điều kiện
            Text(
                text = voucher.dieuKien,
                fontSize = 14.sp
            )

            // Hạn sử dụng
            Text(
                text = "HSD: ${voucher.hanSuDung}",
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // Mã voucher + nút
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = voucher.ma,
                    fontSize = 18.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    // Nút sao chép
                    Button(
                        onClick = {
                            clipboardManager.setText(
                                AnnotatedString(voucher.ma)
                            )
                        }
                    ) {
                        Text("SAO CHÉP")
                    }

                    // Nút dùng ngay
                    Button(
                        onClick = {

                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(voucher.link)
                            )

                            context.startActivity(intent)
                        }
                    ) {
                        Text("DÙNG NGAY")
                    }
                }
            }
        }
    }
}