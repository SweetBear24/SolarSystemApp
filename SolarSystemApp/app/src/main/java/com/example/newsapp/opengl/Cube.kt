package com.example.newsapp.opengl

import android.opengl.GLES20
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube {
    private lateinit var shaderProgram: ShaderProgram
    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var indexBuffer: ShortBuffer

    private val vertices = floatArrayOf(
        // Positions        // Colors
        -1f, 1f, 1f,       1f, 0f, 0f, 1f, // Front-top-left (red)
        -1f, -1f, 1f,      0f, 1f, 0f, 1f, // Front-bottom-left (green)
        1f, -1f, 1f,       0f, 0f, 1f, 1f, // Front-bottom-right (blue)
        1f, 1f, 1f,        1f, 1f, 0f, 1f, // Front-top-right (yellow)
        -1f, 1f, -1f,      1f, 0f, 1f, 1f, // Back-top-left (magenta)
        -1f, -1f, -1f,     0f, 1f, 1f, 1f, // Back-bottom-left (cyan)
        1f, -1f, -1f,      1f, 1f, 1f, 1f, // Back-bottom-right (white)
        1f, 1f, -1f,       0f, 0f, 0f, 1f  // Back-top-right (black)
    )

    private val indices = shortArrayOf(
        0, 1, 2, 0, 2, 3,     // Front face
        4, 5, 6, 4, 6, 7,     // Back face
        0, 1, 5, 0, 5, 4,     // Left face
        3, 2, 6, 3, 6, 7,     // Right face
        0, 3, 7, 0, 7, 4,     // Top face
        1, 2, 6, 1, 6, 5      // Bottom face
    )

    fun initialize() {
        shaderProgram = ShaderProgram(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE)

        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertices)
                position(0)
            }
        }

        indexBuffer = ByteBuffer.allocateDirect(indices.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(indices)
                position(0)
            }
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        shaderProgram.use()

        val positionHandle = GLES20.glGetAttribLocation(shaderProgram.programId, "a_Position")
        val colorHandle = GLES20.glGetAttribLocation(shaderProgram.programId, "a_Color")
        val mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgram.programId, "u_MVPMatrix")

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glEnableVertexAttribArray(colorHandle)

        val stride = 7 * 4 // 7 элементов на вершину, 4 байта на элемент

        vertexBuffer.position(0)
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, stride, vertexBuffer)

        vertexBuffer.position(3)
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, stride, vertexBuffer)

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.size, GLES20.GL_UNSIGNED_SHORT, indexBuffer)

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(colorHandle)

        Log.d("Cube", "Drawn cube")
    }




    companion object {
        private const val VERTEX_SHADER_CODE = """
            attribute vec4 a_Position;
            attribute vec4 a_Color;
            uniform mat4 u_MVPMatrix;
            varying vec4 v_Color;

            void main() {
                gl_Position = u_MVPMatrix * a_Position;
                v_Color = a_Color;
            }
        """

        private const val FRAGMENT_SHADER_CODE = """
            precision mediump float;
            varying vec4 v_Color;

            void main() {
                gl_FragColor = v_Color;
            }
        """
    }
}
