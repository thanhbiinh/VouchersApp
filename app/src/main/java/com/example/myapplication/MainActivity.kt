package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import java.util.TimeZone
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

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
                AppRoot()
            }
        }
    }
}

// ───────────────────────── Logo (icon launcher của app) ─────────────────────────
@Composable
fun DongHoTreoTuong(kichThuoc: Dp = 220.dp) {
    val now by produceState(initialValue = System.currentTimeMillis()) {
        while (true) {
            value = System.currentTimeMillis()
            delay(16)
        }
    }

    Canvas(modifier = Modifier.size(kichThuoc)) {
        val tam = center
        val r = this.size.minDimension / 2f

        // Thời gian địa phương (ms trong ngày)
        val t = (now + TimeZone.getDefault().getOffset(now)) % 86_400_000L
        val giay = ((t / 1000) % 60) + (t % 1000) / 1000f
        val phut = ((t / 60_000) % 60) + giay / 60f
        val gio = ((t / 3_600_000) % 12) + phut / 60f

        // Mặt đồng hồ
        drawCircle(Color.White.copy(alpha = 0.96f), radius = r)
        drawCircle(
            Color(0xFFFFD54F),
            radius = r - 5.dp.toPx(),
            style = Stroke(width = 10.dp.toPx())
        )

        // Vạch phút / giờ
        for (i in 0 until 60) {
            val a = i * 6f * PI.toFloat() / 180f
            val laGio = i % 5 == 0
            val dai = if (laGio) 12.dp.toPx() else 5.dp.toPx()
            val ngoai = r - 20.dp.toPx()
            drawLine(
                color = if (laGio) ChuDam else ChuPhu,
                start = Offset(tam.x + ngoai * sin(a), tam.y - ngoai * cos(a)),
                end = Offset(tam.x + (ngoai - dai) * sin(a), tam.y - (ngoai - dai) * cos(a)),
                strokeWidth = if (laGio) 3.dp.toPx() else 1.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        fun tay(doGoc: Float, dai: Float, duoi: Float, day: Float, mau: Color) {
            val a = doGoc * PI.toFloat() / 180f
            drawLine(
                color = mau,
                start = Offset(tam.x - duoi * sin(a), tam.y + duoi * cos(a)),
                end = Offset(tam.x + dai * sin(a), tam.y - dai * cos(a)),
                strokeWidth = day,
                cap = StrokeCap.Round
            )
        }

        tay(gio * 30f, r * 0.45f, 8.dp.toPx(), 7.dp.toPx(), ChuDam)     // kim giờ
        tay(phut * 6f, r * 0.68f, 10.dp.toPx(), 5.dp.toPx(), ChuDam)    // kim phút
        tay(giay * 6f, r * 0.78f, 18.dp.toPx(), 2.5.dp.toPx(), Color(0xFFE53935)) // kim giây

        // Chốt giữa
        drawCircle(Color(0xFFE53935), radius = 7.dp.toPx(), center = tam)
        drawCircle(Color.White, radius = 2.5.dp.toPx(), center = tam)
    }
}
@Composable
fun LogoApp(size: Dp, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bitmap = remember {
        context.packageManager
            .getApplicationIcon(context.packageName)
            .toBitmap(256, 256)
            .asImageBitmap()
    }
    Image(
        bitmap = bitmap,
        contentDescription = "Logo Super Deal",
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.22f))
    )
}

// ───────────────────────── Splash ─────────────────────────

@Composable
fun AppRoot() {
    var hienSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        hienSplash = false
    }

    Crossfade(
        targetState = hienSplash,
        animationSpec = tween(500),
        label = "splash"
    ) { dangSplash ->
        if (dangSplash) SplashScreen() else MainScreen()
    }
}

@Composable
fun SplashScreen() {
    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.6f) }

    LaunchedEffect(Unit) {
        launch { alphaAnim.animateTo(1f, tween(700)) }
        launch { scaleAnim.animateTo(1f, tween(700)) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CamChinh),
        contentAlignment = Alignment.Center
    ) {
        LogoApp(
            size = 140.dp,
            modifier = Modifier.graphicsLayer {
                alpha = alphaAnim.value
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
            }
        )
    }
}

// ───────────────────────── Màn hình chính + taskbar ─────────────────────────

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("super_deal", Context.MODE_PRIVATE)
    }

    var vouchers by remember { mutableStateOf<List<Voucher>>(emptyList()) }
    var dangTai by remember { mutableStateOf(true) }
    var loiTai by remember { mutableStateOf("") }
    var yeuThich by remember {
        mutableStateOf(prefs.getStringSet("yeu_thich", emptySet())?.toSet() ?: emptySet())
    }
    var tab by rememberSaveable { mutableIntStateOf(0) }

    // Smart taskbar: 0 = hiện đủ, chieuCaoBar = ẩn hẳn
    val chieuCaoBar = remember { FloatArray(1) }
    var offsetBar by remember { mutableFloatStateOf(0f) }

    val barConnection = remember {
        object : NestedScrollConnection {
            // Chỉ theo dõi, không tiêu thụ cuộn -> header và taskbar cùng chạy
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (available.y != 0f) {
                    offsetBar = (offsetBar - available.y).coerceIn(0f, chieuCaoBar[0])
                }
                return Offset.Zero
            }

            // Thả tay: snap taskbar về hẳn hiện hoặc hẳn ẩn
            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity
            ): Velocity {
                val h = chieuCaoBar[0]
                if (offsetBar != 0f && offsetBar != h) {
                    val dich = if (offsetBar > h / 2) h else 0f
                    animate(offsetBar, dich) { v, _ -> offsetBar = v }
                }
                return Velocity.Zero
            }
        }
    }

    // Đổi tab -> luôn hiện lại taskbar
    LaunchedEffect(tab) { offsetBar = 0f }

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

    fun toggle(v: Voucher) {
        val id = v.id.toString()
        val moi = if (id in yeuThich) yeuThich - id else yeuThich + id
        yeuThich = moi
        prefs.edit().putStringSet("yeu_thich", moi).apply()
    }

    Scaffold(
        containerColor = NenApp,
        bottomBar = {
            Box(
                modifier = Modifier
                    .clipToBounds()
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        chieuCaoBar[0] = placeable.height.toFloat()
                        val off = offsetBar.roundToInt()
                        val h = (placeable.height - off).coerceAtLeast(0)
                        layout(placeable.width, h) {
                            placeable.placeWithLayer(0, off)
                        }
                    }
            ) {
                NavigationBar(containerColor = Color.White) {
                    val mauItem = NavigationBarItemDefaults.colors(
                        selectedIconColor = CamChinh,
                        selectedTextColor = CamChinh,
                        unselectedIconColor = ChuPhu,
                        unselectedTextColor = ChuPhu,
                        indicatorColor = Color(0xFFFFE8D6)
                    )
                    val muc = listOf(
                        "🏠" to "Trang chủ",
                        "🎟" to "Voucher",
                        "♥" to "Đã lưu (${yeuThich.size})",
                        "⚙" to "Cài đặt"
                    )
                    muc.forEachIndexed { i, (icon, nhan) ->
                        NavigationBarItem(
                            selected = tab == i,
                            onClick = { tab = i },
                            icon = { Text(icon, fontSize = 20.sp) },
                            label = { Text(nhan, fontSize = 11.sp, maxLines = 1) },
                            colors = mauItem
                        )
                    }
                }
            }
        }
    ) { inner ->
        Box(
            Modifier
                .padding(inner)
                .nestedScroll(barConnection)
        ) {
            when (tab) {
                0 -> TrangChu()

                1 -> VoucherApp(
                    vouchers = vouchers,
                    dangTai = dangTai,
                    loiTai = loiTai,
                    yeuThich = yeuThich,
                    chiYeuThich = false,
                    onToggleYeuThich = { toggle(it) }
                )

                2 -> VoucherApp(
                    vouchers = vouchers,
                    dangTai = dangTai,
                    loiTai = loiTai,
                    yeuThich = yeuThich,
                    chiYeuThich = true,
                    onToggleYeuThich = { toggle(it) }
                )

                else -> ManCaiDat()
            }
        }
    }
}

// ───────────────────────── Trang chủ ─────────────────────────

@Composable
fun TrangChu() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_trang_chu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Em chào đại ca ạ",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            DongHoTreoTuong()
            DongHo(mau = Color(0xFFFFD54F))
        }
    }
}

@Composable
fun DongHo(mau: Color = CamChinh) {
    val gio by produceState(initialValue = "") {
        val fmt = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
        while (true) {
            value = fmt.format(Date())
            delay(16)
        }
    }
    Text(
        text = gio,
        fontSize = 32.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        color = mau,
        textAlign = TextAlign.Center
    )
}

// ───────────────────────── Cài đặt ─────────────────────────

@Composable
fun ManCaiDat() {
    val sapRa = listOf(
        "Chế độ tối",
        "Thông báo mã sắp hết hạn",
        "Đăng nhập / đồng bộ tài khoản",
        "Ngôn ngữ",
        "Xoá toàn bộ mã đã lưu"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Cài đặt",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ChuDam
        )
        sapRa.forEach { ten ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(14.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(ten, fontSize = 14.sp, color = ChuDam)
                Text("Sắp ra mắt", fontSize = 11.sp, color = ChuPhu)
            }
        }
    }
}

// ───────────────────────── Danh sách voucher ─────────────────────────

@Composable
fun VoucherApp(
    vouchers: List<Voucher>,
    dangTai: Boolean,
    loiTai: String,
    yeuThich: Set<String>,
    chiYeuThich: Boolean,
    onToggleYeuThich: (Voucher) -> Unit
) {
    var tuKhoa by remember { mutableStateOf("") }
    var sanDangChon by remember { mutableStateOf("Tất cả") }

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

        val khopYeuThich = !chiYeuThich || voucher.id.toString() in yeuThich

        khopTuKhoa && khopDanhMuc && khopYeuThich
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
                    text = if (chiYeuThich) "Đã lưu" else "Super Deal",
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
                        text = if (chiYeuThich) {
                            "Chưa có mã đã lưu nào"
                        } else {
                            "Không tìm thấy voucher phù hợp"
                        },
                        color = ChuPhu,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(
                    items = vouchersLoc,
                    key = { it.id }
                ) { voucher ->
                    VoucherCard(
                        voucher = voucher,
                        laYeuThich = voucher.id.toString() in yeuThich,
                        onToggleYeuThich = { onToggleYeuThich(voucher) }
                    )
                }
            }
        }
    }
}