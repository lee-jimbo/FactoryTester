
package com.mediatek.factorymode.camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;

public class SubCamera extends Activity implements SurfaceHolder.Callback{  
    
    private static String imgPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/CAMERA"  ;
      
    private SurfaceView surfaceView;   
    private SurfaceHolder surfaceHolder;   
    private Button takePicView;  
	private Button succesButton ;
	private Button failButton ;  
    private Camera mCamera;   
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
          
        super.onCreate(savedInstanceState);  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.camera);

      
        takePicView = (Button)this.findViewById(R.id.camera_take);  
        takePicView.setOnClickListener(TakePicListener);  
       
        succesButton = (Button)this.findViewById(R.id.camera_btok);
		failButton = (Button)this.findViewById(R.id.camera_btfailed);
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SubCamera.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SubCamera.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
        surfaceView = (SurfaceView)this.findViewById(R.id.camera_view);  
        surfaceHolder = surfaceView.getHolder();  
        surfaceHolder.addCallback(this);  
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
          
        checkSoftStage(); 
    }  
      

    private void checkSoftStage(){  
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){ 
            File file = new File(imgPath);  
            if(!file.exists()){  
                file.mkdir();  
            }  
        }else{  
        	 new AlertDialog.Builder(this).setMessage(getString(R.string.sdcard_tips_failed))  
             .setPositiveButton(getString(R.string.okok), new DialogInterface.OnClickListener() {  
                 @Override  
                 public void onClick(DialogInterface dialog, int which) {  
                     finish();  
                 }  
             }).show();  
        }  
    }  
      
  
    private final OnClickListener TakePicListener = new OnClickListener(){  
        @Override  
        public void onClick(View v) {  
           // mCamera.autoFocus(new AutoFoucus());  
 	    mCamera.takePicture(mShutterCallback, null, mPictureCallback);  
            takePicView.setEnabled(false);
        }  
    };  
      

    private final class AutoFoucus implements AutoFocusCallback{  
        @Override  
        public void onAutoFocus(boolean success, Camera camera) {  
            if(success && mCamera!=null){  
                mCamera.takePicture(mShutterCallback, null, mPictureCallback);  
            }  
        }  
    }  
    
    private final PictureCallback mPictureCallback = new PictureCallback() {  
        @Override  
        public void onPictureTaken(byte[] data, Camera camera) {  
            try {  
                String fileName = System.currentTimeMillis()+".jpg";  
                File file = new File(imgPath,fileName);  
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);  
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));  
                bm.compress(Bitmap.CompressFormat.JPEG, 60, bos);  
                bos.flush();  
                bos.close();  
                takePicView.setEnabled(false);
                //Intent intent = new Intent(CameraTest.this,PictureViewAct.class);  
                //intent.putExtra("imagePath", file.getPath());  
                //startActivity(intent);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    };  
       
    private final ShutterCallback mShutterCallback = new ShutterCallback() {    
        public void onShutter() {    
            Log.d("ShutterCallback", "...onShutter...");    
        }    
    };  
      
    @Override  
     
    public void surfaceChanged(SurfaceHolder holder, int format, int width,  
            int height) {  
        Camera.Parameters param = mCamera.getParameters();  
       
        param.setPictureFormat(PixelFormat.JPEG);  
         
        //param.setPreviewSize(320, 240);  
              param.setPictureSize(640, 480);  
        mCamera.setParameters(param);  
        
        mCamera.startPreview();  
    }  
    @Override  
      
    public void surfaceCreated(SurfaceHolder holder) {  
        try {  
            mCamera = Camera.open(1); 
            mCamera.setPreviewDisplay(holder);  
        } catch (IOException e) {  
            mCamera.release();  
            mCamera = null;  
        }  
    }  
      
    @Override  
   
    public void surfaceDestroyed(SurfaceHolder holder) {  
        mCamera.stopPreview();  
        if(mCamera!=null) mCamera.release();  
        mCamera = null;  
    }  
      
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if(keyCode == KeyEvent.KEYCODE_CAMERA){  
            mCamera.autoFocus(new AutoFoucus());  
            return true;  
        }else{  
            return false;  
        }  
    }  
}  
