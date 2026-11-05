package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CamChinh = Color(0xFFFF6D00)
private val NenCam = Color(0xFFFFF3E8)
private val ChuDam = Color(0xFF292524)
private val ChuPhu = Color(0xFF78716C)
private val VienThe = Color(0xFFF0E6DE)

@Composable
fun VoucherCard(
    voucher: Voucher,
    laYeuThich: Boolean = false,
    onToggleYeuThich: () -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val heartScale by animateFloatAsState(
        targetValue = if (laYeuThich) 1.15f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "heartScale"
    )

    val logo = when (voucher.san.lowercase()) {
        "shopee" -> R.drawable.shopee
        "lazada" -> R.drawable.lazada
        "tiktok shop" -> R.drawable.tiktok
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 5.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, VienThe),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Thông tin nền tảng + nút yêu thích
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (logo != null) {
                    Image(
                        painter = painterResource(id = logo),
                        contentDescription = voucher.san,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(NenCam),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = voucher.san.take(1).uppercase(),
                            color = CamChinh,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = voucher.san,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChuDam
                    )

                    Text(
                        text = "Ưu đãi dành cho bạn",
                        fontSize = 12.sp,
                        color = ChuPhu
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (laYeuThich) Color(0xFFFFEBEE) else NenCam)
                        .clickable { onToggleYeuThich() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (laYeuThich) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Yêu thích",
                        tint = if (laYeuThich) Color(0xFFE53935) else ChuPhu,
                        modifier = Modifier
                            .size(20.dp)
                            .scale(heartScale)
                    )
                }
            }

            // Khu vực mức giảm giá
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NenCam)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "ƯU ĐÃI",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChuPhu,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = voucher.giam,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CamChinh,
                        lineHeight = 30.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(44.dp)
                        .background(Color(0xFFFFD6B5))
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1.1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "MÃ GIẢM GIÁ",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChuPhu
                    )

                    Text(
                        text = voucher.ma,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChuDam,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Điều kiện áp dụng
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "Điều kiện",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ChuDam,
                        modifier = Modifier.width(84.dp)
                    )

                    Text(
                        text = voucher.dieuKien,
                        fontSize = 12.sp,
                        color = ChuPhu,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "Hạn sử dụng",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ChuDam,
                        modifier = Modifier.width(84.dp)
                    )

                    Text(
                        text = voucher.hanSuDung,
                        fontSize = 12.sp,
                        color = ChuPhu,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (voucher.luuY.isNotBlank()) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "Lưu ý",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ChuDam,
                            modifier = Modifier.width(84.dp)
                        )

                        Text(
                            text = voucher.luuY,
                            fontSize = 12.sp,
                            color = ChuPhu,
                            lineHeight = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Các nút thao tác
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(voucher.ma))
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CamChinh
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFFB779)),
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 12.dp
                    )
                ) {
                    Text(
                        text = "Sao chép",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                Button(
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(voucher.link)
                        )
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CamChinh,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 12.dp
                    )
                ) {
                    Text(
                        text = "Dùng ngay",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}