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

class SunInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SunInfoScreen()
            }
        }
    }
}

@Composable
fun SunInfoScreen() {
    val context = LocalContext.current // Получаем текущий контекст

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
            // Изображение Солнца
            Image(
                painter = painterResource(id = R.drawable.sun_info), // Замените 'sun' на имя вашего файла изображения
                contentDescription = "Sun Image",
                modifier = Modifier
                    .height(400.dp) // Размер изображения
                    .background(Color.Black, shape = RoundedCornerShape(16.dp)) // Фон с округлыми углами
                    .padding(8.dp) // Добавить отступ
            )

            Spacer(modifier = Modifier.height(16.dp)) // Пробел между изображением и текстом

            Text(
                text = "• Солнце — звезда, находящаяся в центре Солнечной системы.\n" +
                        "• Оно состоит в основном из водорода и гелия.\n" +
                        "• Температура на поверхности Солнца составляет около 5500 градусов Цельсия.\n" +
                        "• Солнце является источником света и тепла для Земли.",
                fontSize = 20.sp, // Увеличенный размер текста
                color = Color.White, // Цвет текста
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp)) // Пробел перед кнопкой

            // Кнопка для возврата к предыдущей активности
            Button(
                onClick = { (context as Activity).finish() }, // Завершение активности
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Вернуться") // Текст на кнопке
            }
        }
    }
}
