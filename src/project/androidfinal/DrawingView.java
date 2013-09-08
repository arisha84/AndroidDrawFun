package project.androidfinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawingView extends View implements OnTouchListener {

    private static final float TOUCH_TOLERANCE = 4;
    
	private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas; 
    private Path mPath;
    private float x, y, lastX, lastY;
    private Bitmap Icon;
    private boolean ShouldDrawIcon = false;
    private int Background = Color.BLACK;
    
	
	public DrawingView(Context context) {
		super(context);
		initDrawingView();
	}
	
	public Bitmap getmBitmap() {
		return mBitmap;
	}
	
	public void SetColor (int color)
	{
		mPaint.setColor(color);
	}
	
	public void DrawIcon(Bitmap largeIcon)
	{
		ShouldDrawIcon = true;
		Icon = largeIcon;
		//mCanvas.drawBitmap(largeIcon, lastX, lastY, mPaint);
		//Trying to scale, didn't work...
		//RectF rd = new RectF(lastX, lastY, lastX + 200, lastY - 200);
		//mCanvas.drawBitmap(largeIcon,null, rd, mPaint);   	
	}
	
	public int getColor(){
		return mPaint.getColor();
	}
	
	public void clearScreen(){
		 mPath.reset();	
		 mBitmap.eraseColor(Background);		 
		 invalidate();
	}
	
	public void DrawPictureFromGallery (Bitmap picture)
	{
		clearScreen();
		mCanvas.drawBitmap(picture, 0, 0, null); 
		invalidate();
	}
	


	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDrawingView();
	}
	
    private void initDrawingView() {       
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);  
        mPaint.setStyle(Paint.Style.STROKE);       
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);        
        
        mPath = new Path();        
        this.setOnTouchListener(this);
    }
	
	@Override
	protected void onDraw(Canvas canvas) {		
        canvas.drawBitmap(mBitmap, 0, 0, null);       
        canvas.drawPath(mPath, mPaint);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mBitmap ==null){			
        	mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        	mBitmap.eraseColor(Background);	
		}
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
	}

    public boolean onTouch(View view, MotionEvent event) {
        x = event.getX();
        y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	if (ShouldDrawIcon)
            	{
            		ShouldDrawIcon = false;
            		mCanvas.drawBitmap(Icon, x - 80, y - 80, mPaint);            		
            	}
                touchStart();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true; 
    }

    private void touchStart() {
        mPath.reset();
        mPath.moveTo(x, y);
        lastX = x;
        lastY = y;
    }
    
    private void touchMove() {
        float dx = Math.abs(x - lastX);
        float dy = Math.abs(y - lastY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {           
            //mPath.lineTo(x, y);
            mPath.quadTo(lastX, lastY, (lastX + x) / 2, (lastY + y) / 2);
            lastX = x;
            lastY = y;
        }
    }
    private void touchUp() {
        mPath.lineTo(lastX, lastY);
        
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
    } 
    
    @Override
    protected Parcelable onSaveInstanceState() {
    	Bundle bundle = new Bundle();
    	bundle.putParcelable("instanceState", super.onSaveInstanceState());
    	bundle.putParcelable("bitmap", mBitmap);
		return bundle;    	
    }
    
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
    	Bundle bundle = (Bundle) state;
    	mBitmap = bundle.getParcelable("bitmap");
    	super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
    }
}
