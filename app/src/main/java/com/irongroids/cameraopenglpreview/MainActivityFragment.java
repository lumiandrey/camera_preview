package com.irongroids.cameraopenglpreview;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irongroids.cameraopenglpreview.opengl.MyGL20Renderer;
import com.irongroids.cameraopenglpreview.opengl.MyGLSurfaceView;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SurfaceTexture.OnFrameAvailableListener {

    private MyGLSurfaceView glSurfaceView;
    private SurfaceTexture surface;
    MyGL20Renderer renderer;
    private Camera mCamera;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        glSurfaceView = new MyGLSurfaceView(this);
        renderer = glSurfaceView.getRenderer();
        return glSurfaceView;
    }

    public void startCamera(int texture)
    {
        surface = new SurfaceTexture(texture);
        surface.setOnFrameAvailableListener(this);
        renderer.setSurface(surface);

        mCamera = Camera.open();

        try
        {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        }
        catch (IOException ioe)
        {
            Log.w("MainActivitySurface","CAM LAUNCH FAILED");
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        glSurfaceView.requestRender();
    }

    @Override
    public void onPause() {
        super.onPause();

        mCamera.stopPreview();
        mCamera.release();
        System.exit(0);
    }

}
