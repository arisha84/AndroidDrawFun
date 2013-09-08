package project.androidfinal;

import java.io.IOException;

import project.androidfinal.R;

import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

	private SurfaceView surface;
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private boolean previewRunning = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        int flagFullScr = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        requestWindowFeature(Window.FEATURE_NO_TITLE);	        
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setFlags(flagFullScr, flagFullScr);
        
        setContentView(R.layout.camera_main);        
  
        surface = (SurfaceView)findViewById(R.id.surface);
        surfaceHolder = surface.getHolder();
        // required on versions prior to Android 3.0:
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        surfaceHolder.addCallback(this);	
        
        LayoutInflater inflater = LayoutInflater.from(this);
        View overView = inflater.inflate(R.layout.camera_overlay, null);
        this.addContentView(overView, 
        		new LayoutParams(LayoutParams.MATCH_PARENT,
        						 LayoutParams.MATCH_PARENT));

 		ImageButton btnTakePic = (ImageButton)findViewById(R.id.takePictureBtn);
 		btnTakePic.setOnClickListener(new OnClickListener() { 	
 			public void onClick(View v) {
 				camera.takePicture(null, null, jpegCallback);				
 			}			
 		});		
    }
    
    private PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			if (data != null) {
				ReturnPicture(data);
				//savePicture(data);
				//camera.startPreview();
			}			
		}		
	};
	
	private void ReturnPicture (byte[] data)
	{
		Intent intent = getIntent();
		intent.putExtra("PictureData", data);
		 this.setResult(RESULT_OK, intent);
	     this.finish(); // close the activity
	}
	
	/*private void savePicture(byte[] data) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);		
		String url = Images.Media.insertImage(getContentResolver(),
				bitmap, "Image Name", "Image Description");		
		
		if (url != null) {
			toastMsg("Picture saved to gallery");		
		} 
		else {
			toastMsg("Picture cannot be saved!");
		}	
		bitmap.recycle(); // free the object associated with the bitmap
	}*/

    private void toastMsg(String msg)
    {
    	Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }  
    
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		toastMsg("surfaceChanged");
	   
        if (previewRunning) {
            camera.stopPreview();				
        }
		
		// Change the camera parameters according to the surface size
		Camera.Parameters params = camera.getParameters();
		params.setPreviewSize(width, height);
		camera.setParameters(params);
			
		// Start capturing and drawing preview frames to the screen
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
			previewRunning = true;			
		}
		catch (IOException e) {
			 Log.e("myApp", e.getMessage());
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		toastMsg("surfaceCreated");
        camera = Camera.open(0);			
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		toastMsg("surfaceDestroyed");
	    camera.stopPreview();
	    previewRunning = false;
	    camera.release();
	    camera = null;		
	}
}
