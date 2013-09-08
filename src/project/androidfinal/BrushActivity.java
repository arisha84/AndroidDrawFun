package project.androidfinal;


import project.androidfinal.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class BrushActivity extends Activity {
	private int savedID;
	public static final int BRUSH_OK = 101;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brush_grid);
        
        GridView gv = (GridView) findViewById(R.id.gridView);
        gv.setAdapter(new BrushAdapter(this));
        
        gv.setOnItemClickListener(new OnItemClickListener()
        {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				savedID = v.getId();
				Intent data  = new Intent();
				data.putExtra("brushShape", savedID);
				setResult(RESULT_OK, data);
				finish();
			}
		});
    }

}
