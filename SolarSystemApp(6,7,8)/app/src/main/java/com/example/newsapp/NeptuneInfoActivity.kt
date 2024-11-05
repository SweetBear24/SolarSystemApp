package com.example.newsapp
import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.newsapp.opengl.MyGLSurfaceViewNeptune

class NeptuneInfoActivity : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NeptuneInfoScreen()
            }
        }
    }
}

@Composable
fun NeptuneInfoScreen() {
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
            Box(
                modifier = Modifier
                    .size(400.dp) // Размер квадрата
                    .background(Color.Black, shape = RoundedCornerShape(16.dp)) // Фон для Нептуна с округлыми углами
                    .padding(8.dp) // Добавить отступ
            ) {
                AndroidView(factory = { context -> MyGLSurfaceViewNeptune(context) }) // Замените на MyGLSurfaceViewNeptune
            }

            Spacer(modifier = Modifier.height(16.dp)) // Пробел между Нептуном и текстом

            Text(
                text = "• Нептун – восьмая планета от Солнца.\n" +
                        "• Его атмосфера содержит метан, что придает ему синий цвет.\n" +
                        "• Нептун имеет сильные ветры и штормы.\n" +
                        "• Открыт в 1846 году.",
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
