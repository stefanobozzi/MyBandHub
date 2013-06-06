package con.example.myband;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import con.example.myband.DB.DBManager;
import con.example.myband.DB.DBReference;

public class TakeLivePhotos extends Activity {

	public final static int TAKE_PHOTO_ACTION_CODE = 5678;
	Button import_btn;
	Button take_btn;
	String concert;
	Button take_another;
	TextView added_success;
	File file;
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	String fname;
	SQLiteDatabase db;
	String path;
	File albumDir;
	String currentPhotoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_live_photos);

		TextView welcome = (TextView) findViewById(R.id.takephotos_welcome);

		// ottengo nome live scelto
		Intent i = getIntent();
		concert = i.getStringExtra(RecordLive.ITEM_SELECTED_KEY);

		DBManager manager = new DBManager(getApplicationContext());
		db = manager.getWritableDatabase();
		welcome.setText(concert + " Photos");

		import_btn = (Button) findViewById(R.id.import_btn);
		take_btn = (Button) findViewById(R.id.take_btn);

		// al click su take photos chiamo la fotocamera
		take_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				

				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(new Date());
				fname = JPEG_FILE_PREFIX + timeStamp + "_"
						+ JPEG_FILE_SUFFIX;

				path = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				path += "/MyBand/" + concert + "/";
				String pathComplete = path.concat(fname);

				File directory = new File(path);
				directory.mkdirs();

				File photo = new File(pathComplete); 
				Uri u = Uri.fromFile(photo);
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				i.putExtra(MediaStore.EXTRA_OUTPUT, u);
				startActivityForResult(i, TAKE_PHOTO_ACTION_CODE);

				// update db SBAGLIATOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO (L'UTENTE POTREBBE NON VOLER
				//SALVARE LA FOTO)

				String[] select = { DBReference.DBLives.COLUMN_BAND_NAME };
				String[] args = { concert };
				Cursor c = db.query(DBReference.DBLives.TABLE_NAME, select,
						DBReference.DBLives.COLUMN_CONCERT_NAME + " = ?", args,
						null, null, null);

				String band = null;

				if (c != null) { // per forza
					if (c.moveToFirst()) {
						band = c.getString(c
								.getColumnIndex(DBReference.DBLives.COLUMN_BAND_NAME));
					}
				}

				c.close();

				try {
					ContentValues values = new ContentValues();

					values.put(DBReference.DBPhotos.COLUMN_NAME, fname);
					values.put(DBReference.DBPhotos.COLUMN_CONCERT_NAME,
							concert);
					values.put(DBReference.DBPhotos.COLUMN_URI, u.toString());
					values.put(DBReference.DBPhotos.COLUMN_BAND_NAME, band);

					long riga = db.insert(DBReference.DBPhotos.TABLE_NAME,
							"null", values);
					Toast toast = Toast.makeText(
							getApplicationContext(),
							"inserito riga" + riga + "\n file name: " + fname
									+ "\n concert: " + concert + "\n uri: "
									+ u.toString() + " \n band name: " + band,
							Toast.LENGTH_LONG);
					toast.show();
				} catch (SQLException e) {
				}

			}
		});
	}

	/*
	 * al ritorno dalla foto: - faccio sparire i vecchi bottoni e comparire i
	 * nuovi - metto un listener su "take another" che è uguale al listener di
	 * "take photo" nell'oncreate - salvo nel db PHOTOS le info della fotografia
	 * INUTILE IL DBPHOTOS?????????????????
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == TAKE_PHOTO_ACTION_CODE && resultCode == RESULT_OK) {

			// nascondo i bottoni e mostro i nascosti

			sendBroadcast (
				    new Intent(Intent.ACTION_MEDIA_MOUNTED, 
				            Uri.parse("file://" + Environment.getExternalStorageDirectory())));
			
			import_btn.setVisibility(View.GONE);
			take_btn.setVisibility(View.GONE);

			take_another = (Button) findViewById(R.id.take_another);
			take_another.setVisibility(View.VISIBLE);

			take_another.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					//creo un nuovo filename + path etc 
					String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());
					fname = JPEG_FILE_PREFIX + timeStamp + "_"
					+ JPEG_FILE_SUFFIX; 
					
					String pathComplete = path.concat(fname);
					
					File file = new File(pathComplete);

					file.mkdirs();

					Uri u = Uri.fromFile(file);
					Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					i.putExtra(MediaStore.EXTRA_OUTPUT, u);
					startActivityForResult(i, TAKE_PHOTO_ACTION_CODE);


				}
			});

			added_success = (TextView) findViewById(R.id.added_success);
			added_success.setVisibility(View.VISIBLE);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_live_photos, menu);
		return true;
	}

}



