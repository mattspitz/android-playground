package com.example.videorecorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.R.id;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public final class RecordActivity extends Activity {
	private static final String TAG = "RecordActivity";

	public static final String WITH_PREVIEW_EXTRA_STRING = "com.example.videorecorder.RecordActivity.WithPreview";

	//a variable to store a reference to the Surface View at the main.xml file
	private SurfaceView surfaceView;

	//a variable to store a reference to the Image View at the main.xml file
	private ImageView imageView;

	//Camera variables
	//a variable to control the camera
	private Camera mCamera;
	private int mCameraId;
	private MediaRecorder mMediaRecorder;
	private boolean isRecording = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		// Show the Up button in the action bar.
		setupActionBar();

		imageView = (ImageView) findViewById(R.id.imageView);
		imageView.setVisibility(View.INVISIBLE);

		//get the Surface View at the main.xml file
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

		//add the callback interface methods defined below as the Surface View callbacks
		surfaceView.getHolder().addCallback(new PreviewSurface());

		Intent intent = getIntent();
		boolean withPreview = (Boolean)intent.getExtras().get(WITH_PREVIEW_EXTRA_STRING);
		if (withPreview) {
			setTitle("Record (with preview!)");
		} else {
			setTitle("Record (without preview!)");
			surfaceView.setLayoutParams(new LayoutParams(1, 1));
		}

		// Add a listener to the Capture button
		final Button captureButton = (Button) findViewById(R.id.recordButton);
		captureButton.setOnClickListener(
		    new View.OnClickListener() {
		    	
		    	private void setCaptureButtonText(String text) {
		    		captureButton.setText(text);
		    	}
		    	
		        @Override
		        public void onClick(View v) {
		            if (isRecording) {
		                // stop recording and release camera
		                mMediaRecorder.stop();  // stop the recording
		                releaseMediaRecorder(); // release the MediaRecorder object
		                mCamera.lock();         // take camera access back from MediaRecorder

		                // inform the user that recording has stopped
		                setCaptureButtonText("Capture");
		                isRecording = false;
		            } else {
		                // initialize video camera
		                if (prepareVideoRecorder()) {
		                    // Camera is available and unlocked, MediaRecorder is prepared,
		                    // now you can start recording
		                    mMediaRecorder.start();

		                    // inform the user that recording has started
		                    setCaptureButtonText("Stop");
		                    isRecording = true;
		                } else {
		                    // prepare didn't work, release the camera
		                    releaseMediaRecorder();
		                    // TODO
		                    Log.e(TAG, "Couldn't prepare video recorder!");
		                    // inform user
		                }
		            }
		        }
		    }
		);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private boolean prepareVideoRecorder() {
		Log.i(TAG, "Preparing video recorder!");
		
		mMediaRecorder = new MediaRecorder();

		// Step 1: Unlock and set camera to MediaRecorder
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);

		// Step 2: Set sources
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
		mMediaRecorder.setProfile(CamcorderProfile.get(mCameraId, CamcorderProfile.QUALITY_HIGH));

		// Step 4: Set output file
		mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

		// Step 5: Set the preview output
		mMediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());

		// Step 6: Prepare configured MediaRecorder
		try {
			mMediaRecorder.prepare();
			Log.i(TAG, "mMediaRecorder prepared!!");
		} catch (IllegalStateException e) {
			Log.e(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			Log.e(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaRecorder();       // if you are using MediaRecorder, release it first
		releaseCamera();              // release the camera immediately on pause event
	}

	private void releaseMediaRecorder(){
		if (mMediaRecorder != null) {
			mMediaRecorder.reset();   // clear recorder configuration
			mMediaRecorder.release(); // release the recorder object
			mMediaRecorder = null;
			mCamera.lock();           // lock camera for later use
		}
	}

	private void releaseCamera(){
		if (mCamera != null){
			mCamera.release();        // release the camera for other applications
			mCamera = null;
		}
	}

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
		// TODO
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "Shenanigans");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"IMG_"+ timeStamp + ".jpg");
		} else if(type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"VID_"+ timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	private class PreviewSurface implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
		{
			Log.e(TAG, "Surface changed!");

			//get camera parameters
			Parameters parameters = mCamera.getParameters();
			// (doing nothing with parameters for now)
			//set camera parameters
			mCamera.setParameters(parameters);

			mCamera.startPreview();
			Log.i(TAG, "Started preview display for camera!");
		}

		private void openFrontFacingCamera() {
			int cameraCount = 0;
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			cameraCount = Camera.getNumberOfCameras();
			for (int idx = 0; idx < cameraCount; idx++) {
				Camera.getCameraInfo(idx, cameraInfo);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					try {
						mCamera = Camera.open(idx);
						mCameraId = idx;
						return;
					} catch (RuntimeException ex) {
						Log.e(TAG, "Camera failed to open: " + ex.getLocalizedMessage());
					}
				}
			}
			throw new RuntimeException("Couldn't find front-facing camera!");
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder)
		{
			Log.e(TAG, "Surface created!");

			// The Surface has been created, acquire the camera and tell it where
			// to draw the preview.
			openFrontFacingCamera();

			try {
				mCamera.setPreviewDisplay(holder);
				Log.i(TAG, "Set preview display for camera!");
			} catch (IOException exception) {
				Log.e(TAG, "Couldn't set preview display", exception);
				mCamera.release();
				mCamera = null;
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder)
		{
			Log.e(TAG, "Surface destroyed!");
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_without_preview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
