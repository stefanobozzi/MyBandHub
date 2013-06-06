package con.example.myband;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import con.example.myband.DB.DBManager;
import con.example.myband.DB.DBReference;


public class ViewGallery extends Activity {
	String[] imgUriStrings;
	Uri[] uris;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_gallery);
		
		
		//ottengo in un array di stringhe tutti gli uri corrispondenti alle foto che mi servono
		DBManager manager = new DBManager(getApplicationContext());
		SQLiteDatabase db = manager.getReadableDatabase();

		String concert = getIntent().getStringExtra(MyGallery.CONCERT_SELECTED_KEY);
		String where = DBReference.DBPhotos.COLUMN_CONCERT_NAME + " = ?";
		String [] arg = {concert};
		String [] select = {DBReference.DBPhotos.COLUMN_URI};
		
		Cursor c = null;
		
		c = db.query(DBReference.DBPhotos.TABLE_NAME, select, where, arg, null, null, null);
		
		
	
		int i = 0;
		Toast toast;
		
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					imgUriStrings[i++] = c.getString(c.getColumnIndex(DBReference.DBPhotos.COLUMN_URI));
				} while (c.moveToNext());
			}
			c.close();
		}
		else {
			toast = Toast.makeText(getApplicationContext(), "Non ho trovato entries nel DB!", Toast.LENGTH_LONG);
			toast.show();
		}
		
		/*
		String toast_text = "Uri Strings: \n";
		uris = null;
		for (int j = 0; j < imgUriStrings.length; j++){
			uris[j] = Uri.parse(imgUriStrings[j]);
			toast_text.concat("- "+ imgUriStrings[j]+ "\n");
		}
		
		if (uris == null){
			toast = Toast.makeText(getApplicationContext(), "L'array di URI è vuoto", Toast.LENGTH_LONG);
		}
		

		
		//resto della logica
		GridView gridView = (GridView) findViewById(R.id.gridview);
		/*gridView.setAdapter(new ImageAdapter(this));

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(getBaseContext(),
						"pic" + (position + 1) + " selected",
						Toast.LENGTH_SHORT).show();
			}
		});*/
	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;

		public ImageAdapter(Context c) {
			context = c;
		}

		// ---returns the number of images---
		public int getCount() {
			return imgUriStrings.length;
		}

		// ---returns the ID of an item---
		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		// ---returns an ImageView view---
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(context);
				imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(5, 5, 5, 5);
			} else {
				imageView = (ImageView) convertView;
			}
			//imageView.setImageResource(imageIDs[position]);
			imageView.setImageBitmap(decodeSampledBitmapFromResource(imgUriStrings[position], 185, 185));
			return imageView;
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 2;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	
	public static Bitmap decodeSampledBitmapFromResource(String resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(resId, options);
	}
}
