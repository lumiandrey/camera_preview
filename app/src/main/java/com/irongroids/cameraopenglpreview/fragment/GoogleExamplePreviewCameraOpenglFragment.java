package com.irongroids.cameraopenglpreview.fragment;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.irongroids.cameraopenglpreview.opengl.CamGLSurfaceView;

public class GoogleExamplePreviewCameraOpenglFragment extends Fragment {

    private GLSurfaceView mGLView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        /*getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/
        mGLView = new CamGLSurfaceView(getContext());

        return mGLView;
    }

    public static GoogleExamplePreviewCameraOpenglFragment newInstance() {
        
        Bundle args = new Bundle();
        
        GoogleExamplePreviewCameraOpenglFragment fragment = new GoogleExamplePreviewCameraOpenglFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGLView.onResume();
    }
}

