package com.example.newsapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MarsInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MarsInfoScreen()
            }
        }
    }
}

@Composable
fun MarsInfoScreen() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Темный фон
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Изображение Марса
            Image(
                painter = painterResource(id = R.drawable.mars_info), // Замените 'mars_info' на имя файла изображения Марса
                contentDescription = "Mars Image",
                modifier = Modifier
                    .height(300.dp)
                    .background(Color.Black, shape = RoundedCornerShape(16.dp))
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "• Марс — четвёртая планета от Солнца, известная как 'Красная планета' из-за оксидов железа на её поверхности.\n" +
                        "• Марс имеет тонкую атмосферу, в основном состоящую из углекислого газа.\n" +
                        "• На Марсе есть крупнейший вулкан в Солнечной системе — Олимп, а также глубокий каньон Валлес Маринерис.\n" +
                        "• У Марса два маленьких спутника: Фобос и Деймос.",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка для возврата
            Button(
                onClick = { (context as Activity).finish() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Вернуться")
            }
        }
    }
}
