package com.irongroids.cameraopenglpreview.opengl;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;

public class MyCamera {

    private final static String LOG_TAG = "MyCamera";

    private Camera mCamera;
    private Camera.Parameters mCameraParams;
    private Boolean running = false;

    void start(SurfaceTexture surface)
    {
        Log.v(LOG_TAG, "Starting Camera");

        mCamera = Camera.open(0);
        mCameraParams = mCamera.getParameters();
        Log.v(LOG_TAG, mCameraParams.getPreviewSize().width + " x " + mCameraParams.getPreviewSize().height);

        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
            running = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        if (running) {
            Log.v(LOG_TAG, "Stopping Camera");
            mCamera.stopPreview();
            mCamera.release();
            running = false;
        }
    }
}
