package com.irongroids.cameraopenglpreview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.irongroids.cameraopenglpreview.R;
import com.irongroids.cameraopenglpreview.fragment.ExampleOpenGlFragment;
import com.irongroids.cameraopenglpreview.fragment.GoogleExamplePreviewCameraOpenglFragment;
import com.irongroids.cameraopenglpreview.fragment.OpenGlRecorderFragment;

public class FragmentOpenGlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_open_gl);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, OpenGlRecorderFragment.newInstance())
                .commit();

    }
}
