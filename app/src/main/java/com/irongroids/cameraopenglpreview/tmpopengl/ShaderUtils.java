package com.irongroids.cameraopenglpreview.tmpopengl;

import android.content.Context;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;

/**
 *
 * Здесь содержатся все методы по компиляции шейдеров и созданию из них программы.
 * В принципе, пока можно не вникать в работу этого класса.
 * Мы создаем его один раз и он не будет изменяться на протяжении нескольких уроков.
 * Здесь не будет ни вершин, ни координат, ни цветов, ни вычислений.
 * Мы просто вынесли в этот класс всю логику по подготовке шейдеров к использованию в нашем приложении.
 *  Так что пока можете просмотреть его поверхностно.
 */
public class ShaderUtils {

    /**
     * создает программу. Программа – это просто пара шейдеров:
     * вершинный + фрагментный. Эта пара шейдеров должна работать в связке,
     * т.к. первый отвечает за вершины,
     * а второй за цвета, и ни один из них по одиночке
     * не даст нам итоговой картинки. Поэтому их объединяют в программу.
     *
     * @param vertexShaderId - id вершинного
     * @param fragmentShaderId - и фрагментного шейдеров.
     *
     * @return - Если все ок, то возвращаем programId. Т.е. программа готова и у нас есть ее id.
     */
    public static int createProgram(int vertexShaderId, int fragmentShaderId) {
        final int programId = glCreateProgram();
        if (programId == 0) {
            return 0;
        }
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        glLinkProgram(programId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            glDeleteProgram(programId);
            return 0;
        }
        return programId;
    }

    /**
     * Принимает на вход контекст, тип шейдера и id raw-ресурса.
     * Читает содержимое (исходник) шейдера в строку и вызывает вторую версию метода
     *
     * @param context
     * @param type
     * @param shaderRawId
     * @return
     */
    static int createShader(Context context, int type, int shaderRawId) {
        String shaderText = FileUtils.readTextFromRaw(context, shaderRawId);
        return ShaderUtils.createShader(type, shaderText);
    }

    /**
     * Этот метод принимает на вход тип шейдера и его содержимое в виде строки,
     * и далее вызывает кучу OpenGL методов по созданию и компиляции шейдера:
     * @param type
     * @param shaderText
     * @return
     */
    static int createShader(int type, String shaderText) {

        /**
         * создает пустой объект шейдера и возвращает его id в переменную shaderId. На вход принимает тип шейдера:
         * GL_VERTEX_SHADER (вершинный) или GL_FRAGMENT_SHADER (фрагментный).
         * Вернет 0 если по каким-то причинам шейдер создать не удалось.
         */
        final int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            return 0;
        }

        /**
         * берет исходник шейдера из строки и ассоциирует его с шейдером shaderId.
         */
        glShaderSource(shaderId, shaderText);
        /**
         *  компилирует шейдер shaderId
         */
        glCompileShader(shaderId);

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            glDeleteShader(shaderId);
            return 0;
        }
        return shaderId;
    }
}
