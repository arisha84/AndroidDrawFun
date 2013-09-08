package project.androidfinal;

import project.androidfinal.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class BrushAdapter extends BaseAdapter {

	private Context mContext;
	
	//c'tor:
	public BrushAdapter(Context c) {
		mContext  = c;
	}

	static private int[] thumbIds = { R.drawable.shape_1, R.drawable.shape_2,
							   R.drawable.shape_3, R.drawable.shape_4,
							   R.drawable.shape_5, R.drawable.shape_6 };
	
	static public int getThembIdFromPosition(int pos)
	{
		return thumbIds[pos];
	}
	
	public int getCount() {
		return  thumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if(convertView == null)
		{
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(100,100));
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setPadding(3, 3, 3, 3);		
		}
		else
		{
			imageView = (ImageView) convertView;
		}
		
		imageView.setImageResource(thumbIds[position]);
		imageView.setId(position); //0..5
		
		return imageView;
	}
}
