package com.example.newsapp.opengl

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import com.example.newsapp.R
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
class MyGLSurfaceViewES2(context: Context) : GLSurfaceView(context) {
    init {
        setEGLContextClientVersion(2)
        setRenderer(MoonRenderer(context))
    }

    private class MoonRenderer(private val context: Context) : Renderer {
        private var moonShaderProgram = 0
        private lateinit var moon: SphereES2
        private val modelMatrix = FloatArray(16)
        private val viewMatrix = FloatArray(16)
        private val projectionMatrix = FloatArray(16)
        private val mvpMatrix = FloatArray(16)
        private var textureId = 0
        private var angle = 0f // Угол вращения Луны

        private val lightPosition = floatArrayOf(5f, 0f, 0f)

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(0f, 0f, 0f, 1f)
            GLES20.glEnable(GLES20.GL_DEPTH_TEST)

            moon = SphereES2(0.5f, 30, 30)
            moonShaderProgram = createShaderProgram(vertexShaderSource, fragmentShaderSource)

            textureId = loadTexture(R.drawable.moon)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
            val aspectRatio = width.toFloat() / height.toFloat()

            Matrix.perspectiveM(projectionMatrix, 0, 45f, aspectRatio, 1f, 10f)
            Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1f, 0f)
        }

        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

            angle += 0.4f
            Matrix.setIdentityM(modelMatrix, 0)
            Matrix.rotateM(modelMatrix, 0, angle, 0f, 1f, 0f)

            Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0)
            Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0)

            GLES20.glUseProgram(moonShaderProgram)

            val mvpMatrixHandle = GLES20.glGetUniformLocation(moonShaderProgram, "uMVPMatrix")
            GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

            val lightPosHandle = GLES20.glGetUniformLocation(moonShaderProgram, "uLightPosition")
            GLES20.glUniform3f(lightPosHandle, lightPosition[0], lightPosition[1], lightPosition[2])

            val modelMatrixHandle = GLES20.glGetUniformLocation(moonShaderProgram, "uModelMatrix")
            GLES20.glUniformMatrix4fv(modelMatrixHandle, 1, false, modelMatrix, 0)

            val textureHandle = GLES20.glGetUniformLocation(moonShaderProgram, "uTexture")
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
            GLES20.glUniform1i(textureHandle, 0)

            moon.draw(moonShaderProgram)
        }
        // Загрузка текстуры
        private fun loadTexture(resourceId: Int): Int {
            val textureHandle = IntArray(1)
            GLES20.glGenTextures(1, textureHandle, 0)

            if (textureHandle[0] != 0) {
                val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
                bitmap.recycle()
            } else {
                throw RuntimeException("Ошибка загрузки текстуры.")
            }

            return textureHandle[0]
        }

        private fun createShaderProgram(vertexSource: String, fragmentSource: String): Int {
            val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
            val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
            return GLES20.glCreateProgram().apply {
                GLES20.glAttachShader(this, vertexShader)
                GLES20.glAttachShader(this, fragmentShader)
                GLES20.glLinkProgram(this)
            }
        }

        private fun loadShader(type: Int, shaderSource: String): Int {
            return GLES20.glCreateShader(type).also { shader ->
                GLES20.glShaderSource(shader, shaderSource)
                GLES20.glCompileShader(shader)
            }
        }

        private val vertexShaderSource = """
            uniform mat4 uMVPMatrix;
            uniform mat4 uModelMatrix; // Мировая матрица модели
            attribute vec4 aPosition;
            attribute vec3 aNormal;
            attribute vec2 aTexCoordinate;
            
            varying vec3 vNormal;
            varying vec3 vPosition;
            varying vec2 vTexCoordinate;
            
            void main() {
                vec4 worldPosition = uModelMatrix * aPosition; // Применяем только модельную матрицу
                vPosition = worldPosition.xyz;
                vNormal = mat3(uModelMatrix) * aNormal; // Применяем только вращение из модельной матрицы
                vTexCoordinate = aTexCoordinate;
            
                gl_Position = uMVPMatrix * aPosition;
            }

        """

        private val fragmentShaderSource = """
            precision mediump float;
            
            varying vec3 vNormal;
            varying vec3 vPosition;
            varying vec2 vTexCoordinate;
            
            uniform vec3 uLightPosition;
            uniform sampler2D uTexture;
            
            void main() {
                vec3 normal = normalize(vNormal);
                vec3 lightDir = normalize(uLightPosition - vPosition); // Статичное освещение
                float diff = max(dot(normal, lightDir), 0.0);
                vec3 diffuse = diff * vec3(1.0, 1.0, 1.0);
                vec4 textureColor = texture2D(uTexture, vTexCoordinate);
            
                gl_FragColor = vec4(diffuse * textureColor.rgb, textureColor.a);
            }
        """
    }
}