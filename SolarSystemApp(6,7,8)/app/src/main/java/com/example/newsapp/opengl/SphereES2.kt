package com.example.newsapp.opengl

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class SphereES2(radius: Float, stacks: Int, slices: Int) {
    private val vertexBuffer: FloatBuffer
    private val normalBuffer: FloatBuffer
    private val textureBuffer: FloatBuffer // Для текстурных координат
    private val indexBuffer: ShortBuffer
    private val vertexCount: Int
    private val indexCount: Int

    init {
        // Создаем массивы вершин, нормалей и текстурных координат
        val vertices = ArrayList<Float>()
        val normals = ArrayList<Float>()
        val textures = ArrayList<Float>() // Текстурные координаты
        val indices = ArrayList<Short>()

        for (stack in 0..stacks) {
            val theta = stack * Math.PI / stacks
            val sinTheta = Math.sin(theta).toFloat()
            val cosTheta = Math.cos(theta).toFloat()

            for (slice in 0..slices) {
                val phi = slice * 2 * Math.PI / slices
                val sinPhi = Math.sin(phi).toFloat()
                val cosPhi = Math.cos(phi).toFloat()

                val x = cosPhi * sinTheta
                val y = cosTheta
                val z = sinPhi * sinTheta

                vertices.add(x * radius)
                vertices.add(y * radius)
                vertices.add(z * radius)

                normals.add(x)
                normals.add(y)
                normals.add(z)

                // Текстурные координаты
                val u = slice.toFloat() / slices
                val v = stack.toFloat() / stacks
                textures.add(u)
                textures.add(v)
            }
        }

        for (stack in 0 until stacks) {
            for (slice in 0 until slices) {
                val first = (stack * (slices + 1) + slice).toShort()
                val second = (first + slices + 1).toShort()
                indices.add(first)
                indices.add(second)
                indices.add((first + 1).toShort())
                indices.add(second)
                indices.add((second + 1).toShort())
                indices.add((first + 1).toShort())
            }
        }

        vertexCount = vertices.size / 3
        indexCount = indices.size

        // Создаем буферы для вершин, нормалей, текстур и индексов
        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().apply {
            put(vertices.toFloatArray())
            position(0)
        }

        normalBuffer = ByteBuffer.allocateDirect(normals.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().apply {
            put(normals.toFloatArray())
            position(0)
        }

        textureBuffer = ByteBuffer.allocateDirect(textures.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().apply {
            put(textures.toFloatArray())
            position(0)
        }

        indexBuffer = ByteBuffer.allocateDirect(indices.size * 2).order(ByteOrder.nativeOrder()).asShortBuffer().apply {
            put(indices.toShortArray())
            position(0)
        }
    }

    fun draw(shaderProgram: Int) {
        // Атрибуты для вершин, нормалей и текстур
        val positionHandle = GLES20.glGetAttribLocation(shaderProgram, "aPosition")
        val normalHandle = GLES20.glGetAttribLocation(shaderProgram, "aNormal")
        val textureHandle = GLES20.glGetAttribLocation(shaderProgram, "aTexCoordinate") // Для текстур

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        GLES20.glEnableVertexAttribArray(normalHandle)
        GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false, 0, normalBuffer)

        // Включаем текстуры
        GLES20.glEnableVertexAttribArray(textureHandle)
        GLES20.glVertexAttribPointer(textureHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)

        // Рисуем сферу
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount, GLES20.GL_UNSIGNED_SHORT, indexBuffer)

        // Отключаем атрибуты после рисования
        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(normalHandle)
        GLES20.glDisableVertexAttribArray(textureHandle) // Отключаем текстурные атрибуты
    }
}
