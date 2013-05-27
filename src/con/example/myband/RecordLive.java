package con.example.myband;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import con.example.myband.DB.DBManager;
import con.example.myband.DB.DBReference;

/*
 * ELIMINA PARTE DI ARRIVO DA VIEW_POSTER
 * TIENI SOLO QUELLA DA HOME MYLIVES DOVE SI SCEGLIE L'ALBUM ALL'INIZIO E POI SI SCATTA
 * 
 * 
 */
public class RecordLive extends Activity {

	public final static String ITEM_SELECTED_KEY = "keyitemselected";
	
	ListView list;

	/*
	 * PRENDE I NOMI DEI LIVE PRESENTI NEL DB E LI MOSTRA NELLA LISTA, MANDANDO A TAKELIVEPHOTOS
	 * PER LA LOGICA DELLO SCATTO IN SE
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_live);

		DBManager dbManager = new DBManager(getApplicationContext());

		SQLiteDatabase db = dbManager.getReadableDatabase();

		String[] SELECT = { DBReference.DBLives.COLUMN_CONCERT_NAME };
		String order = DBReference.DBLives.COLUMN_CONCERT_NAME + " ASC";

		Cursor c = db.query(DBReference.DBLives.TABLE_NAME, SELECT, null, null,
				null, null, order);
		ArrayList<String> arList = new ArrayList<String>();

		if (c != null) {
			if (c.moveToFirst()) {
				do {
					String concertName = c
							.getString(c
									.getColumnIndex(DBReference.DBLives.COLUMN_CONCERT_NAME));

					arList.add(concertName);
				} while (c.moveToNext());
			}
		}
		c.close();

		list = (ListView) findViewById(R.id.reclive_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.list_view_layout, arList);

		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long arg3) {
				
				String item_selected = (String)parent.getItemAtPosition(position);
				Intent i = new Intent(RecordLive.this, TakeLivePhotos.class);
				i.putExtra(ITEM_SELECTED_KEY, item_selected);
				startActivity(i);
				
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_live, menu);
		return true;
	}
}




















































/*
 * 
 * private static final String JPEG_FILE_PREFIX = "IMG_"; private static final
 * String JPEG_FILE_SUFFIX = ".jpg"; public static final int
 * TAKE_PHOTO_ACTION_CODE = 101; public static final String MY_LIVES_ALBUM_NAME
 * = "MyLivesPhotos";
 * 
 * Bitmap myPhoto; ImageView photoContainer; String photo_path; File albumDir;
 * File file;
 * 
 * String concert_requested; TextView init_tv;
 * 
 * @Override protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState); setContentView(R.layout.record_live);
 * 
 * init_tv = (TextView) findViewById(R.id.init_tv);
 * init_tv.setVisibility(View.GONE); concert_requested = null; Button
 * take_photo_btn = (Button) findViewById(R.id.take_photo_btn);
 * 
 * take_photo_btn.setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View v) { Intent i = new
 * Intent(MediaStore.ACTION_IMAGE_CAPTURE); //
 * i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
 * startActivityForResult(i, TAKE_PHOTO_ACTION_CODE);
 * 
 * } });
 * 
 * Intent from = getIntent(); concert_requested = from
 * .getStringExtra(ViewPoster.CONCERT_WITHOUT_PHOTO); if (concert_requested !=
 * null) { init_tv.setVisibility(View.VISIBLE);
 * init_tv.setText("Completa l'aggiunta della foto al poster di " +
 * concert_requested); } // hasSystemFeature(PackageManager.FEATURE_CAMERA) ha
 * la fotocamera? }
 * 
 * @Override protected void onActivityResult(int requestCode, int resultCode,
 * Intent data) { super.onActivityResult(requestCode, resultCode, data);
 * 
 * if (requestCode == TAKE_PHOTO_ACTION_CODE) { if (resultCode == RESULT_OK) {
 * 
 * Bundle datas = data.getExtras(); myPhoto = (Bitmap) datas.get("data");
 * photoContainer = (ImageView) findViewById(R.id.photo_captured_imgview);
 * photoContainer.setImageBitmap(myPhoto); SaveImage(myPhoto,
 * "/MyBand/sticazzi");
 * 
 * if (concert_requested != null) { // qualcuno aveva chiamato l'app per fare
 * update del db DBManager manager = new DBManager(getApplicationContext());
 * SQLiteDatabase db = manager.getWritableDatabase();
 * 
 * Uri uri = Uri.fromFile(file); String uri_string = uri.toString(); int rows =
 * 100; try { ContentValues v = new ContentValues(); String where =
 * DBReference.DBLives.COLUMN_CONCERT_NAME + " = ?"; String[] args = {
 * concert_requested };
 * 
 * v.put(DBReference.DBLives.COLUMN_URI, uri_string); rows =
 * db.update(DBReference.DBLives.TABLE_NAME, v, where, args);
 * 
 * } catch (SQLException e) { }
 * 
 * } else { // normale esecuzione (che si fa?)
 * 
 * }
 * 
 * }
 * 
 * } }
 * 
 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
 * menu; this adds items to the action bar if it is present.
 * getMenuInflater().inflate(R.menu.record_live, menu); return true; }
 * 
 * private void SaveImage(Bitmap finalBitmap, String album) {
 * 
 * 
 * File myDir = new File(album); myDir.mkdirs();
 * 
 * // genera nome file String timeStamp = new
 * SimpleDateFormat("yyyyMMdd_HHmmss") .format(new Date());
 * 
 * String fname = JPEG_FILE_PREFIX + timeStamp + JPEG_FILE_SUFFIX; file = new
 * File(myDir, fname); if (file.exists()) file.delete(); try { FileOutputStream
 * out = new FileOutputStream(file);
 * finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out); out.flush();
 * out.close();
 * 
 * } catch (Exception e) { e.printStackTrace(); }
 * 
 * sendBroadcast(new Intent( Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +
 * Environment.getExternalStorageDirectory())));
 * 
 * }
 */