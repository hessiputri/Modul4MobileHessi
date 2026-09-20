package com.example.modul4mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import com.example.modul4mobile.ui.theme.Modul4MobileTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Modul4MobileTheme {

                // ==========================
                // STATE DIKELOLA OLEH PARENT
                // ==========================

                var ticketPrice by remember {
                    mutableStateOf(50000)
                }

                var ticketQuantity by remember {
                    mutableStateOf(1)
                }

                var buyerName by remember {
                    mutableStateOf("")
                }

                var status by remember {
                    mutableStateOf("Silakan pesan tiket")
                }

                var isProcessing by remember {
                    mutableStateOf(false)
                }

                // ==========================
                // LAUNCHED EFFECT
                // ==========================

                LaunchedEffect(isProcessing) {

                    if (isProcessing) {

                        status = "Memproses pesanan..."

                        // Sesuai instruksi tugas: 5 detik
                        delay(5000)

                        status = "Tiket telah dipesan"

                        isProcessing = false
                    }
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Pemesanan Tiket",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF1565C0)
                            )
                        )
                    }
                ) { innerPadding ->

                    TicketScreen(
                        modifier = Modifier.padding(innerPadding),

                        // STATE
                        ticketPrice = ticketPrice,
                        ticketQuantity = ticketQuantity,
                        buyerName = buyerName,
                        status = status,
                        isProcessing = isProcessing,

                        // EVENT
                        onBuyerNameChange = {
                            buyerName = it
                        },

                        onDecrease = {
                            if (ticketQuantity > 1) {
                                ticketQuantity--
                            }
                        },

                        onIncrease = {
                            ticketQuantity++
                        },

                        onOrderClick = {

                            if (buyerName.isBlank()) {

                                status = "Nama harus diisi"

                            } else {

                                isProcessing = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TicketScreen(
    modifier: Modifier,

    // STATE DARI PARENT
    ticketPrice: Int,
    ticketQuantity: Int,
    buyerName: String,
    status: String,
    isProcessing: Boolean,

    // EVENT KE PARENT
    onBuyerNameChange: (String) -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onOrderClick: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 26.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ==========================
        // NAMA
        // ==========================

        Text(
            text = "Nama",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF202124)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = buyerName,
            onValueChange = onBuyerNameChange,

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            placeholder = {
                Text(
                    text = "Masukkan nama Anda",
                    color = Color(0xFFBDBDBD)
                )
            },

            singleLine = true,

            shape = RoundedCornerShape(6.dp),

            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color(0xFF9E9E9E),
                unfocusedIndicatorColor = Color(0xFFBDBDBD)
            )
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        // ==========================
        // JUMLAH TIKET
        // ==========================

        Text(
            text = "Jumlah Tiket",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF202124)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // TOMBOL -
            QuantityButton(
                text = "-",
                enabled = ticketQuantity > 1,
                onClick = onDecrease
            )

            // JUMLAH
            Text(
                text = "$ticketQuantity",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202124)
            )

            // TOMBOL +
            QuantityButton(
                text = "+",
                enabled = !isProcessing,
                onClick = onIncrease
            )
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        // ==========================
        // TOMBOL PESAN
        // ==========================

        Button(
            onClick = onOrderClick,

            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),

            enabled = !isProcessing,

            shape = RoundedCornerShape(5.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1565C0),
                disabledContainerColor = Color(0xFFB0B8C5)
            )
        ) {
            Text(
                text = if (isProcessing) {
                    "Pesan Tiket"
                } else {
                    "Pesan Tiket"
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // ==========================
        // STATUS
        // ==========================

        StatusBox(
            status = status,
            isProcessing = isProcessing
        )
    }
}

@Composable
fun QuantityButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        enabled = enabled,

        modifier = Modifier
            .width(100.dp)
            .height(62.dp),

        shape = RoundedCornerShape(12.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFEAF2FC),
            contentColor = Color(0xFF202124),
            disabledContainerColor = Color(0xFFEAF2FC),
            disabledContentColor = Color(0xFF9E9E9E)
        )
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatusBox(
    status: String,
    isProcessing: Boolean
) {

    val isError = status == "Nama harus diisi"
    val isSuccess = status == "Tiket telah dipesan"

    val backgroundColor = when {
        isError -> Color(0xFFFFE9EC)
        isSuccess -> Color(0xFFE5F6E9)
        isProcessing -> Color(0xFFE7F3FF)
        else -> Color(0xFFF2F6FA)
    }

    val textColor = when {
        isError -> Color(0xFFC62828)
        isSuccess -> Color(0xFF388E3C)
        isProcessing -> Color(0xFF1565C0)
        else -> Color(0xFF455A64)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 16.dp
            )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            when {
                isError -> {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(22.dp)
                    )
                }

                isSuccess -> {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(22.dp)
                    )
                }

                isProcessing -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color(0xFF1976D2),
                        strokeWidth = 2.dp
                    )
                }

                else -> {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = Color(0xFF78909C),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Text(
                text = "Status: $status",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}