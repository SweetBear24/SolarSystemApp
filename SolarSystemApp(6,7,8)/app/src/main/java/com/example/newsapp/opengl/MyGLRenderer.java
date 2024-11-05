package com.example.newsapp.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.newsapp.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private int selectedPlanetIndex = 0; // По умолчанию выбрано Солнце
    private Cube selectionCube;
    private Square background;
    private Sphere sun, earth, moon, venus, mars, jupiter, saturn, mercury, uranus, neptune;
    private Sphere[] planets;
    private float[][] planetPositions;
    private String[] planetNames;

    private float angleSun = 0.0f;

    // Углы вращения планет вокруг оси
    private float[] planetRotationAngles;

    // Углы орбиты планет
    private float[] planetOrbitAngles;

    // Скорости вращения планет вокруг своей оси
    private float[] planetRotationSpeeds = {
            6.0f,   // Меркурий
            1.62f,  // Венера
            0.986f, // Земля
            0.972f, // Марс
            0.414f, // Юпитер
            0.444f, // Сатурн
            0.718f, // Уран
            0.671f  // Нептун
    };

    // Скорости вращения планет вокруг Солнца
    private float[] planetOrbitSpeeds = {
            4.74f / 3,  // Меркурий
            3.5f / 3,   // Венера
            2.98f / 3,  // Земля
            2.41f / 3,  // Марс
            1.31f / 3,  // Юпитер
            0.97f / 3,  // Сатурн
            0.68f / 3,  // Уран
            0.54f / 3   // Нептун
    };

    // Позиция Луны относительно Земли
    private float moonOrbitDistance = 0.5f;
    private float moonOrbitAngle = 0.0f;
    private Sphere blackHole;
    private float blackHolePositionX = -20.0f;  // Start off-screen on the left
    private float blackHolePositionY = 0.0f;
    private float blackHoleSpeed = 0.05f;       // Speed of movement

    public MyGLRenderer(Context context) {
        this.context = context;
        planetNames = new String[]{"Солнце", "Меркурий", "Венера", "Земля", "Луна", "Марс", "Юпитер", "Сатурн", "Уран", "Нептун"};

        // Инициализация куба выбора
        selectionCube = new Cube();

        planetRotationAngles = new float[10]; // Для всех 10 объектов
        planetOrbitAngles = new float[10];
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_TEXTURE_2D);

        // Инициализация объектов планет и фона
        background = new Square();
        background.loadTexture(gl, context, R.drawable.galaxy_texture);

        sun = new Sphere(0.5f);
        earth = new Sphere(0.25f);
        moon = new Sphere(0.05f);
        venus = new Sphere(0.25f);
        mars = new Sphere(0.2f);
        jupiter = new Sphere(0.5f);
        saturn = new Sphere(0.6f);
        mercury = new Sphere(0.15f);
        uranus = new Sphere(0.3f);
        neptune = new Sphere(0.3f);

        planets = new Sphere[]{sun, mercury, venus, earth, moon, mars, jupiter, saturn, uranus, neptune};

        // Позиции планет
        planetPositions = new float[][]{
                {0.0f, 0.0f, 0.0f},  // Солнце
                {4.0f, 0.0f, 0.0f},  // Меркурий
                {5.0f, 0.0f, 0.0f},  // Венера
                {6.0f, 0.0f, 0.0f},  // Земля
                {6.5f, 0.0f, 0.0f},  // Луна
                {7.5f, 0.0f, 0.0f},  // Марс
                {10.0f, 0.0f, 0.0f}, // Юпитер
                {12.0f, 0.0f, 0.0f}, // Сатурн
                {14.0f, 0.0f, 0.0f}, // Уран
                {16.0f, 0.0f, 0.0f}  // Нептун
        };

        // Загрузка текстур для планет
        sun.loadTexture(gl, context, R.drawable.sun);
        earth.loadTexture(gl, context, R.drawable.earth);
        moon.loadTexture(gl, context, R.drawable.moon);
        venus.loadTexture(gl, context, R.drawable.venus);
        mars.loadTexture(gl, context, R.drawable.mars);
        jupiter.loadTexture(gl, context, R.drawable.jupiter);
        saturn.loadTexture(gl, context, R.drawable.saturn);
        mercury.loadTexture(gl, context, R.drawable.mercury);
        uranus.loadTexture(gl, context, R.drawable.uranus);
        neptune.loadTexture(gl, context, R.drawable.neptune);
        // Initialize the black hole sphere
        blackHole = new Sphere(0.7f);  // Size for the black hole
        blackHole.loadTexture(gl, context, R.drawable.black_hole1);  // Texture for black hole

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float) width / height, 0.1f, 100.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Рисование фона
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -40.0f);
        background.draw(gl);
        gl.glPopMatrix();

        // Рисование Солнца
        gl.glTranslatef(0.0f, 0.0f, -15.0f); // Перемещаем камеру ближе к Солнцу
        gl.glRotatef(10.0f, 1.0f, 0.0f, 0.0f); // Наклоняем на 45 градусов вокруг оси X
        gl.glPushMatrix();
        gl.glRotatef(angleSun, 0.0f, 1.0f, 0.0f);
        sun.draw(gl);
        gl.glPopMatrix();

        // Рисование планет
        for (int i = 0; i < planets.length; i++) {
            drawPlanet(gl, i);
        }
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f); // Установите белый цвет

        drawBlackHole(gl);

        // Рисование куба выбора
        drawSelectionCube(gl);

        // Обновление углов для следующего кадра
        updateAngles();
    }

    private void drawSelectionCube(GL10 gl) {
        gl.glPushMatrix();

        if (selectedPlanetIndex == 4) { // Для Луны
            float earthOrbitDistance = planetPositions[3][0]; // Позиция Земли
            gl.glRotatef(planetOrbitAngles[3], 0.0f, 1.0f, 0.0f); // Вращение по орбите Земли
            gl.glTranslatef(earthOrbitDistance, 0.0f, 0.0f); // Перемещаем к Земле

            // Вращение Луны вокруг Земли
            gl.glRotatef(moonOrbitAngle, 0.0f, 1.0f, 0.0f);
            gl.glTranslatef(moonOrbitDistance, 0.0f, 0.0f);
        } else {
            float orbitDistance = planetPositions[selectedPlanetIndex][0];
            gl.glRotatef(planetOrbitAngles[selectedPlanetIndex], 0.0f, 1.0f, 0.0f);
            gl.glTranslatef(orbitDistance, 0.0f, 0.0f);
        }

        // Получаем размер выбранной планеты, включая Солнце
        float planetSize = planets[selectedPlanetIndex].getSize();

        // Увеличиваем размер куба на фиксированный коэффициент относительно планеты
        selectionCube.setSize(planetSize * 1.1f); // Куб будет на 20% больше, чем планета

        selectionCube.draw(gl);
        gl.glPopMatrix();
    }

    private void drawPlanet(GL10 gl, int index) {
        gl.glPushMatrix();

        if (index == 4) {
            // Рисование Луны
            // Вращение Луны по орбите вокруг Земли
            float earthOrbitDistance = planetPositions[3][0]; // Позиция Земли
            gl.glRotatef(planetOrbitAngles[3], 0.0f, 1.0f, 0.0f); // Вращение по орбите Земли
            gl.glTranslatef(earthOrbitDistance, 0.0f, 0.0f); // Перемещаем к Земле

            // Вращение Луны вокруг Земли
            gl.glRotatef(moonOrbitAngle, 0.0f, 1.0f, 0.0f);
            gl.glTranslatef(moonOrbitDistance, 0.0f, 0.0f);

            moon.draw(gl);
        } else {
            // Вращение по орбите вокруг Солнца
            float orbitDistance = planetPositions[index][0];
            gl.glRotatef(planetOrbitAngles[index], 0.0f, 1.0f, 0.0f);
            gl.glTranslatef(orbitDistance, 0.0f, 0.0f);

            // Вращение вокруг своей оси
            gl.glRotatef(planetRotationAngles[index], 0.0f, 1.0f, 0.0f);

            planets[index].draw(gl);
        }

        gl.glPopMatrix();
    }

    private void drawBlackHole(GL10 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(blackHolePositionX, blackHolePositionY, -15.0f);  // Set position of black hole
        blackHole.draw(gl);
        blackHolePositionX += blackHoleSpeed;  // Move the black hole left to right
        if (blackHolePositionX > 20.0f || blackHolePositionX < -20.0f) {  // Reverse direction at boundaries
            blackHoleSpeed = -blackHoleSpeed;
        }
        gl.glPopMatrix();
    }

    private void updateAngles() {
        // Вращение Солнца
        angleSun += 1.0f;
        if (angleSun > 360) {
            angleSun -= 360;
        }

        // Обновляем углы вращения и орбит для планет
        for (int i = 1; i < planets.length; i++) { // Начинаем с 1, чтобы пропустить Солнце
            if (i == 4) continue; // Пропускаем Луну

            planetRotationAngles[i] += planetRotationSpeeds[i < 4 ? i - 1 : i - 2]; // Корректируем индексы
            if (planetRotationAngles[i] > 360) {
                planetRotationAngles[i] -= 360;
            }

            planetOrbitAngles[i] += planetOrbitSpeeds[i < 4 ? i - 1 : i - 2]; // Корректируем индексы
            if (planetOrbitAngles[i] > 360) {
                planetOrbitAngles[i] -= 360;
            }
        }

        // Обновление угла орбиты Луны вокруг Земли
        moonOrbitAngle += 5.0f; // Скорость вращения Луны вокруг Земли
        if (moonOrbitAngle > 360) {
            moonOrbitAngle -= 360;
        }
        blackHolePositionX += blackHoleSpeed;
        if (blackHolePositionX > 20.0f) {  // Reset position if it goes off-screen on the right
            blackHolePositionX = -20.0f;
        }
    }



    // Метод для выбора следующей планеты (вправо)
    public void selectNextPlanet() {
        selectedPlanetIndex = (selectedPlanetIndex + 1) % planets.length;
    }

    // Метод для выбора предыдущей планеты (влево)
    public void selectPreviousPlanet() {
        selectedPlanetIndex = (selectedPlanetIndex - 1 + planets.length) % planets.length;
    }

    public String getSelectedPlanetName() {
        return planetNames[selectedPlanetIndex];
    }
    public int getSelectedPlanetIndex() {
        return selectedPlanetIndex;
    }

}
