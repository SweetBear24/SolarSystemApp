package com.example.newsapp.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Cube {

    private FloatBuffer vertexBuffer;  // Буфер для вершин куба
    private FloatBuffer colorBuffer;   // Буфер для цветов
    private float scale = 1.0f; // Масштаб куба
    private float size = 1.0f; // Изменяемый размер куба

    // Координаты вершин куба (каждая вершина описывается 3 значениями для X, Y и Z)
    private float[] vertices = {
            -1.0f, -1.0f, -1.0f,   // Левая нижняя задняя вершина
            1.0f, -1.0f, -1.0f,    // Правая нижняя задняя вершина
            1.0f,  1.0f, -1.0f,    // Правая верхняя задняя вершина
            -1.0f,  1.0f, -1.0f,   // Левая верхняя задняя вершина
            -1.0f, -1.0f,  1.0f,   // Левая нижняя передняя вершина
            1.0f, -1.0f,  1.0f,    // Правая нижняя передняя вершина
            1.0f,  1.0f,  1.0f,    // Правая верхняя передняя вершина
            -1.0f,  1.0f,  1.0f    // Левая верхняя передняя вершина
    };

    // Цвета для каждой вершины (RGBA)
    private float[] colors = {
            1.0f, 1.0f, 1.0f, 0.5f,  // Белый с прозрачностью
            1.0f, 1.0f, 1.0f, 0.5f,  // Белый с прозрачностью
            1.0f, 1.0f, 1.0f, 0.5f,  // Белый с прозрачностью
            1.0f, 1.0f, 1.0f, 0.5f,  // Белый с прозрачностью
            1.0f, 1.0f, 1.0f, 0.5f,  // Белый с прозрачностью
            1.0f, 1.0f, 1.0f, 0.5f,  // Белый с прозрачностью
            1.0f, 1.0f, 1.0f, 0.5f,  // Белый с прозрачностью
            1.0f, 1.0f, 1.0f, 0.5f   // Белый с прозрачностью
    };

    // Индексы для соединения вершин в треугольники
    private byte[] indices = {
            0, 4, 5, 0, 5, 1,  // Задняя грань
            1, 5, 6, 1, 6, 2,  // Правая грань
            2, 6, 7, 2, 7, 3,  // Передняя грань
            3, 7, 4, 3, 4, 0,  // Левая грань
            4, 7, 6, 4, 6, 5,  // Нижняя грань
            3, 0, 1, 3, 1, 2   // Верхняя грань
    };
    public void setSize(float newSize) {
        this.size = newSize;
    }

    public Cube() {
        // Создание буфера для вершин
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // Создание буфера для цветов
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void draw(GL10 gl) {
        // Включение смешивания
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        // Включение массивов вершин и цветов
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        // Применение масштаба
        gl.glPushMatrix(); // Сохранение текущей матрицы
        gl.glScalef(scale, scale, scale); // Применение масштаба
        gl.glScalef(size, size, size); // Измените масштаб куба

        // Установка указателя на массив вершин
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        // Установка указателя на массив цветов
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

        // Рисование куба с использованием индексов
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, ByteBuffer.wrap(indices));

        // Отключение массивов вершин и цветов
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        gl.glPopMatrix(); // Восстановление сохраненной матрицы

        // Отключение смешивания после рисования
        gl.glDisable(GL10.GL_BLEND);
    }

}
