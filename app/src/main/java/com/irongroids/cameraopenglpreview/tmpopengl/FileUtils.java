package com.irongroids.cameraopenglpreview.tmpopengl;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
    /**
     * В этом классе только один метод readTextFromRaw, который по id прочтет raw-ресурс и вернет его содержимое в виде строки.
     * Т.е. он будет читать содержимое файлов-шейдеров и возвращать нам это содержимое в текстовом виде.
     *
     * @param context - контекст приложения
     * @param resourceId - путь к ресурсу
     * @return содержимое файлов-шейдеров
     */
    public static String readTextFromRaw(Context context, int resourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = null;
            try {
                InputStream inputStream = context.getResources().openRawResource(resourceId);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\r\n");
                }
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Resources.NotFoundException nfex) {
            nfex.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
