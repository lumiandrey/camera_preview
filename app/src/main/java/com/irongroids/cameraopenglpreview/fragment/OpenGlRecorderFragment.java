package com.irongroids.cameraopenglpreview.fragment;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.EGL14;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.irongroids.cameraopenglpreview.R;
import com.irongroids.cameraopenglpreview.openglrecorder.videorecorder.recorder.camera.CameraView;
import com.irongroids.cameraopenglpreview.openglrecorder.videorecorder.recorder.mediacodec.MediaCodecRecorder;
import com.irongroids.cameraopenglpreview.openglrecorder.videorecorder.recorder.pub.Profile;

import java.io.IOException;

public class OpenGlRecorderFragment extends Fragment implements CameraView.CameraSurfaceListener {

    private static final String TAG = "RecordingActivity";

    public static final String TEST_VIDEO_RECORDER_OUTPUT = "/sdcard/videorecorder_%d.mp4";
    private static final int PREVIEW_WIDTH = 1920;
    private static final int PREVIEW_HEIGHT = 1080;
    private static final int VIDEO_WIDTH = 1920;
    private static final int VIDEO_HEIGHT = 1080;
    private static final int VIDEO_BIT_RATE = 1024 * 1024;
    private static final int VIDEO_I_FRAME_INTERVAL = 3;

    private Camera mCamera;
    private CameraView mCameraView;
    private CheckBox mRecordingControl;

    private MediaCodecRecorder mVideoRecorder;

    private boolean isSurfaceReady = false;

    private int count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recording, null, false);


        mCameraView = rootView.findViewById(R.id.surfaceView);
        mCameraView.setRatio(1.0f);
        mCameraView.setCameraSurfaceListener(this);
        mRecordingControl = rootView.findViewById(R.id.recording_control);
        mRecordingControl.setOnCheckedChangeListener(mControlCheck);
        mVideoRecorder = new MediaCodecRecorder();

        return rootView;
    }

    private void startRecording() {
        Log.d(TAG, "startRecording");
        if (isSurfaceReady) {
            mVideoRecorder.setOutputFile(String.format(TEST_VIDEO_RECORDER_OUTPUT, count));
            Profile.Builder builder = new Profile.Builder();
            builder.setVideoSize(VIDEO_WIDTH, VIDEO_HEIGHT);
            builder.setVideoBitRate(VIDEO_BIT_RATE);
            builder.setVideoIFrameInterval(VIDEO_I_FRAME_INTERVAL);
            mVideoRecorder.setProfile(builder.build());
            mVideoRecorder.prepare();
            mCamera.unlock();
            mVideoRecorder.start();
        }
    }

    private void stopRecording() {
        Log.d(TAG, "stopRecording");
        mVideoRecorder.stop();
        mCamera.lock();
        count++;
    }

    private CheckBox.OnCheckedChangeListener mControlCheck = new CheckBox.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                startRecording();
            } else {
                stopRecording();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        this.mCameraView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mCameraView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoRecorder.release();
    }

    @Override
    public void onCameraSurfaceCreate(SurfaceTexture surfaceTexture) {
        Log.d(TAG, "onCameraSurfaceCreate");
        mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        mVideoRecorder.createInputSurfaceWindow(EGL14.eglGetCurrentContext());
        try {
            parameters.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
            mCameraView.setPreviewSize(PREVIEW_HEIGHT, PREVIEW_WIDTH);
            mVideoRecorder.setPreviewSize(PREVIEW_HEIGHT, PREVIEW_WIDTH);
            mCamera.setParameters(parameters);
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.setDisplayOrientation(Profile.ORIENTATION_90);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

        isSurfaceReady = true;
    }

    @Override
    public void onCameraSurfaceChanged(SurfaceTexture surfaceTexture, int width, int height) {
        Log.d(TAG, "onCameraSurfaceChanged");
    }

    @Override
    public void onCameraSurfaceDestroy(SurfaceTexture surfaceTexture) {
        Log.d(TAG, "onCameraSurfaceDestroy");
        isSurfaceReady = false;
        mCamera.stopPreview();
        mCamera.release();
        if (mVideoRecorder.isRecording()) {
            mVideoRecorder.stop();
        }
    }

    @Override
    public void onCameraSurfaceUpdate(SurfaceTexture surfaceTexture, int textureId) {
        mVideoRecorder.updateInputSurfaceWindow(textureId, surfaceTexture);
    }

    public static OpenGlRecorderFragment newInstance() {
        
        Bundle args = new Bundle();
        
        OpenGlRecorderFragment fragment = new OpenGlRecorderFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
