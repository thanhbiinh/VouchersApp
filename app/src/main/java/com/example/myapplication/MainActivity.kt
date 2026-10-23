package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.math.roundToInt

private val CamChinh = Color(0xFFFF6D00)
private val NenApp = Color(0xFFFFF8F2)
private val ChuDam = Color(0xFF292524)
private val ChuPhu = Color(0xFF78716C)

private val danhMucCuaHang = listOf(
    "Tất cả",
    "Shopee",
    "Lazada",
    "TikTok Shop"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                VoucherApp()
            }
        }
    }
}

@Composable
fun VoucherApp() {
    var vouchers by remember { mutableStateOf<List<Voucher>>(emptyList()) }
    var tuKhoa by remember { mutableStateOf("") }
    var sanDangChon by remember { mutableStateOf("Tất cả") }
    var dangTai by remember { mutableStateOf(true) }
    var loiTai by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    // 0 = hiện đủ, -chieuCao = thu hẳn
    val chieuCaoDauTrang = remember { FloatArray(1) }
    var offsetDauTrang by remember { mutableFloatStateOf(0f) }

    // Smart Header: vuốt lên -> thu, vuốt xuống -> hiện ngay ở bất kỳ vị trí nào
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (available.y == 0f) return Offset.Zero
                val cu = offsetDauTrang
                val moi = (cu + available.y).coerceIn(-chieuCaoDauTrang[0], 0f)
                offsetDauTrang = moi
                return Offset(0f, moi - cu)
            }

            // Thả tay: snap header về hẳn hiện hoặc hẳn ẩn
            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity
            ): Velocity {
                val h = chieuCaoDauTrang[0]
                if (offsetDauTrang != 0f && offsetDauTrang != -h) {
                    val dich = if (offsetDauTrang < -h / 2) -h else 0f
                    animate(offsetDauTrang, dich) { v, _ -> offsetDauTrang = v }
                }
                return Velocity.Zero
            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            vouchers = loadVouchers()
            loiTai = ""
        } catch (e: Exception) {
            loiTai = e.message ?: "Không thể tải voucher"
        } finally {
            dangTai = false
        }
    }

    val vouchersLoc = vouchers.filter { voucher ->
        val khopTuKhoa =
            voucher.ma.contains(tuKhoa, ignoreCase = true) ||
                    voucher.san.contains(tuKhoa, ignoreCase = true) ||
                    voucher.dieuKien.contains(tuKhoa, ignoreCase = true) ||
                    voucher.luuY.contains(tuKhoa, ignoreCase = true) ||
                    voucher.giam.contains(tuKhoa, ignoreCase = true)

        val khopDanhMuc =
            sanDangChon == "Tất cả" ||
                    voucher.san.equals(sanDangChon, ignoreCase = true)

        khopTuKhoa && khopDanhMuc
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NenApp)
            .nestedScroll(nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    chieuCaoDauTrang[0] = placeable.height.toFloat()
                    val off = offsetDauTrang.roundToInt()
                    val h = (placeable.height + off).coerceAtLeast(0)
                    layout(placeable.width, h) {
                        placeable.placeWithLayer(0, off) {
                            alpha = (1f + offsetDauTrang / placeable.height)
                                .coerceIn(0f, 1f)
                        }
                    }
                }
                .background(NenApp)
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Super Deal",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ChuDam
                )

                Text(
                    text = "${vouchersLoc.size} ưu đãi",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CamChinh,
                    modifier = Modifier
                        .background(
                            Color(0xFFFFE8D6),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 9.dp, vertical = 5.dp)
                )
            }

            OutlinedTextField(
                value = tuKhoa,
                onValueChange = { tuKhoa = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Tìm mã giảm giá...",
                        fontSize = 14.sp,
                        color = ChuPhu
                    )
                },
                leadingIcon = {
                    Text(
                        text = "⌕",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = CamChinh
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CamChinh,
                    unfocusedBorderColor = Color(0xFFF0E0D4),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = CamChinh
                )
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(danhMucCuaHang) { san ->
                    FilterChip(
                        selected = sanDangChon == san,
                        onClick = { sanDangChon = san },
                        label = {
                            Text(
                                text = san,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CamChinh,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = ChuPhu
                        )
                    )
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = 4.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (dangTai) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = CamChinh)
                    }
                }
            } else if (loiTai.isNotEmpty()) {
                item {
                    Text(
                        text = "Lỗi tải dữ liệu: $loiTai",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else if (vouchersLoc.isEmpty()) {
                item {
                    Text(
                        text = "Không tìm thấy voucher phù hợp",
                        color = ChuPhu,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(
                    items = vouchersLoc,
                    key = { it.id }
                ) { voucher ->
                    VoucherCard(voucher)
                }
            }
        }
    }
}