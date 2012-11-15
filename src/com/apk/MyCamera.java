package com.apk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MyCamera extends Activity {
	Camera mCamera;
	String count;
	String filePath = "/storage/sdcard0/DCIM/Camera/";
	CameraPreview mPreview;
	FrameLayout layout ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		Button captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(l);
		Button watchBtn = (Button) findViewById(R.id.button_watch);
		watchBtn.setOnClickListener(l);
		layout = (FrameLayout) findViewById(R.id.camera_preview);
		
	}

	View.OnClickListener l = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_capture:
				mCamera.takePicture(null, null, mPicture);
				break;
			case R.id.button_watch:
				Intent intent = new Intent();
				intent.setClass(MyCamera.this, MyGallary.class);
				startActivity(intent);

			}

		}
	};

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	@SuppressLint("NewApi")
	public static Camera getCameraInstance() {
		Camera c = null;

		int number = Camera.getNumberOfCameras();
		CameraInfo cameraInfo = new CameraInfo();
		Camera.getCameraInfo(0, cameraInfo);
		for (int i = 0; i < number; i++) {
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK){
				c = Camera.open(i);
				break;
				}
		}

		return c; // returns null if camera is unavailable
	}

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			String TAG = "System";
			File pictureFile = getOutputMediaFile("a", ".jpg");
			if (pictureFile == null) {
				Log.d(TAG,
						"Error creating media file, check storage permissions: ");
				return;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
			Toast.makeText(getApplicationContext(), "File saved", Toast.LENGTH_SHORT).show();
			mCamera.startPreview();
		}

		private File getOutputMediaFile(String string, String format) {
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd   hh:mm:ss");
			String date = sDateFormat.format(new java.util.Date());
			File file = new File(filePath + string + date + format);

			return file;
		}
	};

	@Override
	protected void onResume() {
		mCamera = getCameraInstance();
		mPreview = new CameraPreview(this, mCamera);
		mCamera.startPreview();
		layout.addView(mPreview);
		super.onResume();
	}

	@Override
	protected void onPause() {
		mCamera.stopPreview();
		layout.removeView(mPreview);
		releaseCamera();
		super.onPause();
	}

}
