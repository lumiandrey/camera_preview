package com.irongroids.cameraopenglpreview.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.irongroids.cameraopenglpreview.tmpopengl.OpenGLRenderer;

public class ExampleOpenGlFragment extends Fragment {

    private GLSurfaceView mGLSurfaceView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (!supportES2()) {
            Toast.makeText(getContext(), "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            getActivity().finish();
        } else {
            //создаем GLSurfaceView
            mGLSurfaceView = new GLSurfaceView(getContext());
            //говорим, что будем использовать OpenGL ES версии 2
            mGLSurfaceView.setEGLContextClientVersion(2);
            //методом setRenderer передаем экземпляр нашего класса OpenGLRenderer.
            // Теперь этот рендер будет отвечать за то, что будет нарисовано на surface
            mGLSurfaceView.setRenderer(new OpenGLRenderer(getContext()));
        }

        return mGLSurfaceView;
    }

    public static ExampleOpenGlFragment newInstance() {

        Bundle args = new Bundle();

        ExampleOpenGlFragment fragment = new ExampleOpenGlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *  определяем, что девайс поддерживает OpenGL ES 2.0
     *
     * @return
     */
    private boolean supportES2() {
        ActivityManager activityManager =
                (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }
}
