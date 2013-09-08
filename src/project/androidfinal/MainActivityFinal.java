package project.androidfinal;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;




import project.androidfinal.R;
import project.androidfinal.ColorPickerDialog.OnColorChangedListener;






import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class MainActivityFinal extends Activity implements SensorEventListener  {
	
	private static final int PREFERENCES = 0;
	private static final int BRUSH_SCREEN = 1;
	private static int RESULT_LOAD_IMAGE = 2;
	private static final int CAMERA_SCREEN = 3;
	private static final int UPLOAD_SCREEN = 4;
	private int mBrushShape = -1;
	
	DrawingView mDv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    
        sensorMgr = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        accelerSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mDv = (DrawingView) findViewById(R.id.drawingView);
        setPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_main, menu);
		//menu.add(Menu.NONE, MENU_SAVE, Menu.NONE, "Save");
		return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_preferences:
            	startActivityForResult(new Intent(this,
    					SettingsActivity.class), PREFERENCES);
            	return true;    
            	
            case R.id.menu_new:
            	  mDv.clearScreen();
            	return true;
            	
            case R.id.menu_Send:
            	
            	SendEmail();            
            	return true;
            	
            case R.id.menu_Import:
            	ImportPicture();
            	return true;
            	
            case R.id.menu_Upload:
            	UploadPicture();
            	return true;
            default:
            	return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }    
    
    public void buttonUploadClick(View v)
    {
    	UploadPicture();
    }
    
    private void UploadPicture()
    {
    	Intent launchBrushScreen = new Intent(this, UploadActivity.class);
    	String uri = saveBitmap("").toString();
    	launchBrushScreen.putExtra("picUri", uri);
        // passing information to launched activity
        //launchGame.putExtra("meaningOfLife", meaningOfLife);        
        startActivityForResult(launchBrushScreen, UPLOAD_SCREEN);		
	}

	public void buttonBrushClick (View v)
    {
    	Intent launchBrushScreen = new Intent(this, BrushActivity.class);
        // passing information to launched activity
        //launchGame.putExtra("meaningOfLife", meaningOfLife);        
        startActivityForResult(launchBrushScreen, BRUSH_SCREEN);
    }
    
   
    public void buttonSaveClick(View v)
    {
    	final Dialog commentDialog = new Dialog(this);
    	commentDialog.setContentView(R.layout.reply);
    	Button okBtn = (Button) commentDialog.findViewById(R.id.ok);
		okBtn.setOnClickListener(new View.OnClickListener() {

    	                          
    	                            public void onClick(View v) {
    	                            	EditText FileNameText = (EditText) commentDialog.findViewById(R.id.body);
    	                                String filename = FileNameText.getText().toString();
    	                                SaveClicked(filename);
    	                                commentDialog.dismiss();
    	                                    
    	                            }
    	 });
    	Button cancelBtn = (Button) commentDialog.findViewById(R.id.cancel);
    	cancelBtn.setOnClickListener(new View.OnClickListener() {

    	                         
    	                            public void onClick(View v) {

    	                                    commentDialog.dismiss();
    	                            }
    	 });
    	commentDialog.setTitle("Enter File name");
    	commentDialog.show();
    	
    	
    }
    
    void SaveClicked (String filename)
    {
    	
    	String uri = saveBitmap(filename).toString();
    	Toast.makeText(this, uri, Toast.LENGTH_LONG).show();
    }
    
    
    public void colorsButtonClicked(View v)
    {
    	ColorPickerDialog dialog = new ColorPickerDialog(this, new OnColorChangedListener() {
			
			public void colorChanged(int color) {
				mDv.SetColor(color);
				
			}
		}, mDv.getColor());
    	dialog.show();
    }
    
    private Uri saveBitmap(String name)
    {
    	String filename = String.valueOf(System.currentTimeMillis());
    	ContentValues values = new ContentValues();
    	if (name == "")
    		values.put(Images.Media.TITLE, filename);
    	else
    	    values.put(Images.Media.TITLE, name);
    	
    	values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
    	values.put(Images.Media.MIME_TYPE,"image/jpeg");
    	//values.put(Images.Media.LATITUDE,32);
    	//values.put(Images.Media.LONGITUDE,34);
    	
    	Uri uri  = getContentResolver().insert(
    			Images.Media.EXTERNAL_CONTENT_URI, values);
    	
    	try {
			OutputStream outStream = getContentResolver().openOutputStream(uri);
			
			DrawingView dv = (DrawingView) findViewById(R.id.drawingView);
			
			dv.getmBitmap().compress(Bitmap.CompressFormat.JPEG, 90, outStream);
			outStream.flush();
			outStream.close();
			
			Toast.makeText(this, "Image Saved!", Toast.LENGTH_LONG).show();
			return uri;		
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public void SendEmail()
    {
    	final Intent emailIntent = new Intent(Intent.ACTION_SEND);
    	
    	emailIntent.setType("image/png"); // for Text Email: "message/rfc822"
    	emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"youremail@yahoo.com"});
    	emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
    	emailIntent.putExtra(Intent.EXTRA_TEXT, "message");
    
    	
    //	emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("android.resource://" +
    	//			getPackageName()+"/" + R.drawable.ic_launcher));
    	Uri imageURI = saveBitmap("");
      emailIntent.putExtra(Intent.EXTRA_STREAM, imageURI);
    	
    	this.startActivity(Intent.createChooser(emailIntent, "Choose an Email Client:"));
    }
    
    public void ImportGallery()
    {
    	Intent i = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    
    private void ImportCamera() {
    	Intent launchCameraScreen = new Intent(this, CameraActivity.class);
        // passing information to launched activity
        //launchGame.putExtra("meaningOfLife", meaningOfLife);        
        startActivityForResult(launchCameraScreen, CAMERA_SCREEN);
		
	}
    
    public void ImportPicture() {
		final String CAMERA = "Camera";
		final String Gallery = "Gallery";

		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("Import Picture");
		//dialog.setMessage("Are you sure?");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, CAMERA,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						ImportCamera();
					}					
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, Gallery,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						ImportGallery();
					}
				});
		
		dialog.show();
		
    }
    
    //step 4:
  	@Override
  	protected void onActivityResult(int requestCode, int resultCode, Intent data)
  	{
         if (requestCode == BRUSH_SCREEN && resultCode == RESULT_OK)
         {
        	mBrushShape = data.getExtras().getInt("brushShape");
        	
        	int xIconId = BrushAdapter.getThembIdFromPosition(mBrushShape);
        	Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), xIconId);
            mDv.DrawIcon(largeIcon);
  		   
         }
         
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& data != null) {			
			 Uri imageUri = data.getData();
		     try {
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
				mDv.DrawPictureFromGallery(bitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if (requestCode == CAMERA_SCREEN && resultCode == RESULT_OK)
	    {
			byte[] PictureData = data.getByteArrayExtra("PictureData");
			Bitmap bitmap = BitmapFactory.decodeByteArray(PictureData, 0, PictureData.length);
			mDv.DrawPictureFromGallery(bitmap);
	    }
		
		if (requestCode == PREFERENCES)
		{
			setPreferences();
		}
  		
  		super.onActivityResult(requestCode, resultCode, data);
  	}
    

  	private void setPreferences() {
  		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
  		
  		String[]   options = this.getResources().getStringArray(
				R.array.BrushColors);
		String sort = prefs.getString("BrushColorOption", "0");
		String colorStr = options[Integer.parseInt(sort)];
		int colorInt = Color.parseColor(colorStr);		
		mDv.SetColor(colorInt);		
  	}
  	
  	
  	
  	
  	//// implementing shake ////
  	
  	 private SensorManager sensorMgr;
	 private Sensor accelerSensor;
	 private TextView tv;	 
	 
	 //data members for Shake:	 
	 
	 private static final int MIN_FORCE=10;
	 private static final int MIN_DIRECTION_CHANGE=3;
	 private static final int MAX_PAUSE_BETWEEN_DIRECTION_CHANGE=200;
	 private static final int MAX_TOTAL_DURATION_OF_SHAKE=200;
	 
	 private boolean firstSenData = true;
	 private long firstDirectionChangeTime=0;
	 private long lastDirectionChangeTime;
	 private int directionChangeCounter = 0; 

	@Override
	protected void onResume() {
		sensorMgr.registerListener(this, accelerSensor,
				SensorManager.SENSOR_DELAY_UI);
		super.onResume();
	}

	@Override
	protected void onPause() {
		sensorMgr.unregisterListener(this, accelerSensor);
		super.onPause();
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent e) {
		float x = e.values[0];
		float y = e.values[1];
		float z = e.values[2];
		
		if(firstSenData)
		{
			saveXYZ(x, y, z);
			firstSenData = false;
			return;
		}	
float totalMove  = Math.abs(x-lastX) + Math.abs(y-lastY) + Math.abs(z-lastZ);
		
		if(totalMove > MIN_FORCE)
		{
			long now = System.currentTimeMillis();
			
			if(firstDirectionChangeTime==0)
			{
				firstDirectionChangeTime=now;
				lastDirectionChangeTime=now;
			}	
			
			if((now-lastDirectionChangeTime) < MAX_PAUSE_BETWEEN_DIRECTION_CHANGE)
			{
				lastDirectionChangeTime=now;
				directionChangeCounter++;
				
				if(directionChangeCounter >=MIN_DIRECTION_CHANGE 
					&& (now-firstDirectionChangeTime) < MAX_TOTAL_DURATION_OF_SHAKE )
				{
					onShake();
					reset();
				}
			}				
		}
		else
		{
			reset();
		}
		
		saveXYZ(x, y, z);		
		
		
	}
	
	
	float lastX=0, lastY=0, lastZ=0;
	
	private void saveXYZ(float x, float y, float z) {
		lastX = x;
		lastY = y;
		lastZ = z;
	}
	
	private void reset() {
		firstDirectionChangeTime=0;
		directionChangeCounter=0;
		lastDirectionChangeTime=0;		
	}
	
	private void onShake() { 
		Vibrator vibe = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		vibe.vibrate(300);
		final String YES = "Yes";
		final String NO = "No";

		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("Clear Screen?");
		//dialog.setMessage("Are you sure?");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, YES,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mDv.clearScreen();
					}					
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, NO,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		dialog.show();
	}	

}
