package com.irongroids.cameraopenglpreview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.irongroids.cameraopenglpreview.R;
import com.irongroids.cameraopenglpreview.fragment.OpenGlCameraPreviewFragment;

public class FragmentOpenGlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_open_gl);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, OpenGlCameraPreviewFragment.newInstance())
                .commit();

    }
}
