package com.irongroids.cameraopenglpreview.tmpopengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLES;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.irongroids.cameraopenglpreview.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class OpenGLRenderer implements Renderer {

    private Context context;
    private int programId;
    private FloatBuffer vertexData;
    private int uColorLocation;
    private int aPositionLocation;

    public OpenGLRenderer(Context context) {
        this.context = context;
        prepareData();
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        // ставим черным дефолтный цвет очистки.
        glClearColor(0f, 0f, 0f, 1f);

        /*
         * Затем, методами класса ShaderUtils создаем шейдеры,
         * получаем их id: vertexShaderId (вершинный) и
         * fragmentShaderId (фрагментный), создаем из них программу programId
         */
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);

        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        /*
         * методом glUseProgram сообщаем системе, что эту программу надо использовать для построения изображения
         */
        glUseProgram(programId);
        bindData();
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
    }

    /**
     * мы подготавливаем данные для передачи их в шейдеры. СНачала создаем массив из 6-ти элементов.
     * Я в коде разделил эти 6 элементов на три строки для наглядности, т.к.
     * на самом деле это координаты трех точек: (-0.5, -0.2), (0, 0.2) и (0.5, -0.2).
     * Эти три точки – вершины треугольника, который мы собираемся нарисовать.
     *  Почему такие маленькие значения? Особенно по сравнению с канвой, где мы использовали координаты от 0 до 1000.
     * Потому что OpenGL свою область рисования (т.е. экран) приведет к диапазону [-1, 1] по ширине и высоте.
     */
    private void prepareData() {

        float[] vertices = {
                -0.5f, -0.2f,
                0.0f, 0.2f,
                0.5f, -0.2f,
        };
        //Далее нам придется сконвертить float[] массив в буфер FloatBuffer, т.к. это необходимо для передачи данных в шейдеры.
        vertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertices);
    }

    /**
     * Здесь мы будем передавать данные в шейдер.
     */
    private void bindData() {
        /**
         * мы в переменную uColorLocation получаем положение в шейдере нашей uniform переменной
         */
        uColorLocation = glGetUniformLocation(programId, "u_Color");
        /**
         * передаем в uColorLocation 4 float значения,
         * которые являются RGBA компонентами синего цвета (0,0,1,1).
         * Эти данные пойдут в шейдер в переменную u_Color.
         */
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        /**
         *  в переменную aPositionLocation получаем положение attribute переменной a_Position
         */
        aPositionLocation = glGetAttribLocation(programId, "a_Position");

        /**
         * Методом position сообщаем системе, что данные из vertexData надо будет читать начиная с элемента с индексом 0, т.е. с самого начала.
         */
        vertexData.position(0);
        /**
         * мы сообщаем системе, что шейдеру для своего атрибута a_Position необходимо читать данные из массива vertexData.
         * А параметры этого метода позволяют подробно задать правила чтения.
         * Рассмотрим какие параметры идут на вход этому методу
         * *int indx – переменная указывающая на положение атрибута в шейдере.
         * *Тут все понятно, используем ранее полученную aPositionLocation, которая знает где сидит a_Position.
         * *указывает системе, сколько элементов буфера vertexData брать для заполнения атрибута a_Position.
         * *передаем GL_FLOAT, т.к. у нас float значения
         * *этот флаг нам пока неактуален, ставим false
         * *используется при передаче более чем одного атрибута в массиве.
         * *Мы пока передаем данные только для одного атрибута, поэтому пока ставим 0.
         * *Но в последующих уроках мы еще используем этот параметр.
         * *буффер с данными, т.е. vertexData.
         */
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 0, vertexData);

        //И напоследок нам необходимо включить атрибут aPositionLocation
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        glClear(GL_COLOR_BUFFER_BIT);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

}
