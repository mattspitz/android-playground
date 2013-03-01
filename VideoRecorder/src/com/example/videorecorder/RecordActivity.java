package com.example.videorecorder;

import java.io.IOException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public final class RecordActivity extends Activity {
	private static final String TAG = "RecordActivity";

	public static final String WITH_PREVIEW_EXTRA_STRING = "com.example.videorecorder.RecordActivity.WithPreview";

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	//a variable to store a reference to the Surface View at the main.xml file
	private SurfaceView surfaceView;

	//a surface holder
	private SurfaceHolder surfaceHolder;

	//a variable to store a reference to the Image View at the main.xml file
	private ImageView imageView;

	//Camera variables
	//a variable to control the camera
	private Camera mCamera;
	private boolean cameraReady = false;

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

		//Get a surface
		surfaceHolder = surfaceView.getHolder();

		//add the callback interface methods defined below as the Surface View callbacks
		surfaceHolder.addCallback(new PreviewSurface());
		
		Intent intent = getIntent();
		boolean withPreview = (Boolean)intent.getExtras().get(WITH_PREVIEW_EXTRA_STRING);
		if (withPreview) {
			setTitle("Record (with preview!)");
		} else {
			setTitle("Record (without preview!)");
			surfaceView.setLayoutParams(new LayoutParams(1, 1));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void recordButtonPressed(View view) {
		//sets what code should be executed after the picture is taken

		if (cameraReady) {
			Camera.PictureCallback mCall = new Camera.PictureCallback()
			{
				@Override
				public void onPictureTaken(byte[] data, Camera camera)
				{
					//decode the data obtained by the camera into a Bitmap
					Log.e(TAG, String.format("CAPTURED! %d bytes of data", data.length));
					Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

					imageView.setImageBitmap(bmp);
					imageView.setVisibility(View.VISIBLE);
					surfaceView.setVisibility(View.GONE);
					cleanupCamera();
				}
			};

			mCamera.takePicture(null, null, mCall);
		}
	}

	private void cleanupCamera() {
		if (mCamera != null) {
			//stop the preview
			mCamera.stopPreview();
			//release the camera
			mCamera.release();
			//unbind the camera from this object
			mCamera = null;

			cameraReady = false;
		}
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

			cameraReady = true;
		}

		private Camera getFrontFacingCamera() {
			int cameraCount = 0;
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			cameraCount = Camera.getNumberOfCameras();
			for (int idx = 0; idx < cameraCount; idx++) {
				Camera.getCameraInfo(idx, cameraInfo);
				Log.e(TAG, "Checking camera " + idx + " info: " + cameraInfo.facing);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					try {
						return Camera.open(idx);
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
			mCamera = getFrontFacingCamera();
			Log.e(TAG, "Got front-facing camera: " + mCamera);

			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException exception) {
				mCamera.release();
				mCamera = null;
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder)
		{
			Log.e(TAG, "Surface destroyed!");

			cleanupCamera();
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
