package com.apk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MyGallary extends Activity{
	String path ="/mnt/sdcard/DCIM/Camera";
	List<String> items;
	Bitmap bitmap;
	int index;
	
	ImageView imageview;
	Button frontBtn;
	Button nextBtn;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		index = 0;
		frontBtn = (Button)findViewById(R.id.front);
		frontBtn.setOnClickListener(l);
		nextBtn = (Button)findViewById(R.id.next);
		nextBtn.setOnClickListener(l);
		imageview = (ImageView)findViewById(R.id.imageview);
		frontBtn.setEnabled(false);
		nextBtn.setEnabled(false);
	}
	@Override
	protected void onResume() {
		getFiles();
		super.onResume();
	}
	void setImage(String pathName){
		bitmap = BitmapFactory.decodeFile(pathName);
		//bitmap = Bitmap.createScaledBitmap(bitmap, W, H, true);
		imageview.setImageBitmap(bitmap);
	}
	void getFiles(){
		File file = new File(path);
		File[] files = file.listFiles();
		int length = files.length;

		items = new ArrayList<String>();
		for(int i = 0; i < length; i++){
			String str = files[i].getPath();
			String sub = str.substring(str.length()-4);
			if(sub.equals(new String(".jpg")))
				items.add(str);
		}
		if(items.size()!=0){
			setImage(items.get(0));
		}
		if(items.size()>1){
			nextBtn.setEnabled(true);
		}
	}
	OnClickListener l = new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.front:
				index--;
				setImage(items.get(index));
				if(index==0)
					frontBtn.setEnabled(false);
				nextBtn.setEnabled(true);
				break;
			case R.id.next:
				index++;
				setImage(items.get(index));
				if(index == items.size()-1)
					nextBtn.setEnabled(false);
				frontBtn.setEnabled(true);
				break;
			}
		}
		
	};
	@Override
	protected void onPause(){
		items.clear();
		index = 0;
		super.onPause();
		
	}
}
