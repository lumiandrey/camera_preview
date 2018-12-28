package com.irongroids.cameraopenglpreview.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irongroids.cameraopenglpreview.opengl.MyCamera;
import com.irongroids.cameraopenglpreview.opengl.MyGLSurfaceView;

public class OpenGlCameraPreviewFragment extends Fragment {

    private MyGLSurfaceView glSurfaceView;
    private MyCamera mCamera;


    @Override
    public void onPause()
    {
        super.onPause();
        mCamera.stop();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mCamera = new MyCamera();

        glSurfaceView = new MyGLSurfaceView(getContext(), mCamera);

        return glSurfaceView;
    }

    public static OpenGlCameraPreviewFragment newInstance() {

        Bundle args = new Bundle();

        OpenGlCameraPreviewFragment fragment = new OpenGlCameraPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
