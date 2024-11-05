package com.example.newsapp

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.newsapp.opengl.MyGLSurfaceView

class MainActivity : ComponentActivity() {
    private var showNews by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Проверка первого запуска
        val sharedPref = getSharedPreferences("news_app", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPref.getBoolean("isFirstLaunch", true)

        if (isFirstLaunch) {
            // Сохраняем, что это больше не первый запуск
            sharedPref.edit().putBoolean("isFirstLaunch", false).apply()
        }

        // Устанавливаем ориентацию в зависимости от начального состояния
        if (showNews) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Вертикальная ориентация для новостей
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE // Горизонтальная ориентация для OpenGL
        }

        // Устанавливаем начальный контент
        setContent {
            if (showNews) {
                NewsScreen(onDismiss = {
                    showNews = false
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE // Переключаем ориентацию на альбомную
                }) // Экран новостей
            } else {
                OpenGLScreen(context = this) // OpenGL экран
            }
        }
    }
}

@Composable
fun OpenGLScreen(context: Context) {
    val glSurfaceView = MyGLSurfaceView(context)

    // Используем Box для наложения элементов друг на друга
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(androidx.compose.ui.graphics.Color.White, androidx.compose.ui.graphics.Color(0xFF003366)), // Темно-синий градиент
                    startY = 0f,
                    endY = 1000f
                )
            )
    ) {
        // Отображаем OpenGL-сцену
        AndroidView(factory = { glSurfaceView }, modifier = Modifier.fillMaxSize())

        // Размещаем кнопки поверх OpenGL сцены
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { glSurfaceView.selectPreviousPlanet() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Text(text = "Влево")
                }
                Button(
                    onClick = {
                        val selectedPlanet = glSurfaceView.getSelectedPlanetName()
                        when (selectedPlanet) {
                            "Луна" -> {
                                context.startActivity(Intent(context, MoonInfoActivity::class.java))
                            }
                            "Солнце" -> {
                                context.startActivity(Intent(context, SunInfoActivity::class.java)) // Запуск активности для Солнца
                            }
                            "Меркурий" -> {
                            context.startActivity(Intent(context, MercuryInfoActivity::class.java)) // Запуск активности для Солнца
                            }
                            "Венера" -> {
                                context.startActivity(Intent(context, VenusInfoActivity::class.java)) // Запуск активности для Солнца
                            }
                            "Земля" -> {
                                context.startActivity(Intent(context, EarthInfoActivity::class.java)) // Запуск активности для Солнца
                            }
                            "Марс" -> {
                                context.startActivity(Intent(context, MarsInfoActivity::class.java)) // Запуск активности для Солнца
                            }
                            "Юпитер" -> {
                                context.startActivity(Intent(context, JupiterInfoActivity::class.java)) // Запуск активности для Солнца
                            }
                            "Сатурн" -> {
                                context.startActivity(Intent(context, SaturnInfoActivity::class.java)) // Запуск активности для Солнца
                            }
                            "Уран" -> {
                                context.startActivity(Intent(context, UranusInfoActivity::class.java)) // Запуск активности для Солнца
                            }
                            "Нептун" -> {
                                context.startActivity(Intent(context, NeptuneInfoActivity::class.java)) // Запуск активности для Солнца
                            }
                            else -> {
                                Toast.makeText(context, selectedPlanet, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Text(text = "Информация")
                }
                Button(
                    onClick = { glSurfaceView.selectNextPlanet() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Text(text = "Вправо")
                }
            }
        }
    }
}
