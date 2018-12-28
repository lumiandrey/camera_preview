package com.irongroids.cameraopenglpreview.opengl;

import android.opengl.GLSurfaceView;

import com.irongroids.cameraopenglpreview.MainActivityFragment;

public class MyGLSurfaceView extends GLSurfaceView
{
    MyGL20Renderer renderer;
    public MyGLSurfaceView(MainActivityFragment context)
    {
        super(context.getContext());

        setEGLContextClientVersion(2);

        renderer = new MyGL20Renderer((MainActivityFragment) context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }
    public MyGL20Renderer getRenderer()
    {
        return renderer;
    }
}
