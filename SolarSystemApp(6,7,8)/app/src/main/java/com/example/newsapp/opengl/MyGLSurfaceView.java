package com.example.newsapp.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer renderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(1);
        renderer = new MyGLRenderer(context);
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Преобразуем координаты касания из пикселей в OpenGL координаты
            float normalizedX = (touchX / getWidth()) * 2 - 1; // от -1 до 1
            float normalizedY = 1 - (touchY / getHeight()) * 2; // от 1 до -1

            // Обработка касаний для кнопок
            if (isButtonTouched(normalizedX, normalizedY, -0.5f, -0.8f)) { // кнопка "Влево"
                selectPreviousPlanet();
            } else if (isButtonTouched(normalizedX, normalizedY, 0.5f, -0.8f)) { // кнопка "Вправо"
                selectNextPlanet();
            } else if (isButtonTouched(normalizedX, normalizedY, 0.0f, -0.8f)) { // кнопка "Инфо"
                System.out.println("Selected Planet: " + getSelectedPlanetName());
            }
        }
        return true;
    }

    private boolean isButtonTouched(float touchX, float touchY, float buttonX, float buttonY) {
        // Определите размеры кнопок для проверки касания
        float buttonWidth = 0.4f; // ширина кнопки в OpenGL координатах
        float buttonHeight = 0.2f; // высота кнопки в OpenGL координатах

        return touchX > (buttonX - buttonWidth / 2) && touchX < (buttonX + buttonWidth / 2)
                && touchY > (buttonY - buttonHeight / 2) && touchY < (buttonY + buttonHeight / 2);
    }

    // Метод для выбора предыдущей планеты
    public void selectPreviousPlanet() {
        renderer.selectPreviousPlanet();
    }

    // Метод для выбора следующей планеты
    public void selectNextPlanet() {
        renderer.selectNextPlanet();
    }

    // Метод для получения имени выбранной планеты
    public String getSelectedPlanetName() {
        return renderer.getSelectedPlanetName();
    }
    public int getSelectedPlanetIndex() {
        return renderer.getSelectedPlanetIndex();
    }
}
