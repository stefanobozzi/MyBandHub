package con.example.myband;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import con.example.myband.DB.DBManager;
import con.example.myband.DB.DBReference;

/*
 * RIMANE DA 
 * -
 * -SISTEMARE IL LAYOUT
 * -AGGIUNGERE LA POSSIBILITÀ DI MODIFICARE I CAMPI DEL LIVE (TRA CUI..: occhio che modifichi il nome live
 * o nome band devi anche modificare le entry del db delle foto
 * 
 * SE ELIMINI IL LIVE DEVI ELIMINARE TUTTE LE FOTO CORRISPONDENTI PRIMA DI ELIMINARLO
 * 
 * 
 * AGGIUNGERE LA POSSIBILITÀ DI RIMUOVERE/MODIFICARE LA FOTO DEL POSTER 
 * (O METTERLA COME BACKGROUND AL POSTER)) 
 * - RISOLVERE IL FATTO CHE SALVA LA FOTO ANCHE NELL'ALBUM FOTOCAMERA (RISOLVI IL FATTO DEL PUTEXTRA)
 */
public class ViewPoster extends Activity {

	public final static int TAKE_PHOTO_ACTION_CODE = 2345;
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	public final static String CONCERT_WITHOUT_PHOTO = "egololi";
	String concert;
	String band;
	String path;
	String uri_string;
	SQLiteDatabase db;
	File file;
	Button uri_btn;
	String fname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_poster);

		Intent i = getIntent();
		String item_selected = i.getStringExtra(ViewLives.GET_SELECTED_ITEM);

		DBManager dbManager = new DBManager(getApplicationContext());
		db = dbManager.getWritableDatabase();

		String[] SELECT = { DBReference.DBLives.COLUMN_CONCERT_NAME,
				DBReference.DBLives.COLUMN_BAND_NAME,
				DBReference.DBLives.COLUMN_DATE,
				DBReference.DBLives.COLUMN_PLACE_NAME,
				DBReference.DBLives.COLUMN_NOTES,
				DBReference.DBLives.COLUMN_URI };

		String[] selection = { item_selected };
		Cursor c = db.query(DBReference.DBLives.TABLE_NAME, SELECT,
				DBReference.DBLives.COLUMN_CONCERT_NAME + " = ?", selection,
				null, null, null);

		concert = null;
		band = null;
		String place = null;
		String date = null;
		String notes = null;
		String uri = null;

		// RICORDA DI METTERE L'IMMAGINE AL POSTO DEL VUOTO SE L'URI È PRESENTE

		uri_btn = (Button) findViewById(R.id.uri_btn);
		uri_btn.setVisibility(View.INVISIBLE);

		if (c != null) {
			if (c.moveToFirst()) {

				// DATI DEL LIVE IN QUESTIONE

				concert = c
						.getString(c
								.getColumnIndex(DBReference.DBLives.COLUMN_CONCERT_NAME));
				band = c.getString(c
						.getColumnIndex(DBReference.DBLives.COLUMN_BAND_NAME));
				place = c.getString(c
						.getColumnIndex(DBReference.DBLives.COLUMN_PLACE_NAME));
				date = c.getString(c
						.getColumnIndex(DBReference.DBLives.COLUMN_DATE));
				notes = c.getString(c
						.getColumnIndex(DBReference.DBLives.COLUMN_NOTES));
				uri = c.getString(c
						.getColumnIndex(DBReference.DBLives.COLUMN_URI));
			}
		}

		c.close();

		// ora assegna a ogni textview quel valore

		TextView concert_obtained = (TextView) findViewById(R.id.concert_obtained);
		TextView band_obtained = (TextView) findViewById(R.id.band_obtained);
		TextView place_obtained = (TextView) findViewById(R.id.place_obtained);
		TextView date_obtained = (TextView) findViewById(R.id.date_obtained);
		TextView notes_obtained = (TextView) findViewById(R.id.notes_obtained);

		concert_obtained.setText(concert);
		if (band != null) {
			band_obtained.setText(band);
		} else {
			TextView band_tv = (TextView) findViewById(R.id.band_name_poster);
			band_tv.setVisibility(View.INVISIBLE);
		}
		if (place != null) {
			place_obtained.setText(place);
		} else {
			TextView place_tv = (TextView) findViewById(R.id.place_name_poster);
			place_tv.setVisibility(View.INVISIBLE);
		}
		if (date != null) {
			date_obtained.setText(date);
		} else {
			TextView date_tv = (TextView) findViewById(R.id.date_poster);
			date_tv.setVisibility(View.INVISIBLE);
		}
		if (notes != null) {
			notes_obtained.setText(notes);
		} else {
			TextView notes_tv = (TextView) findViewById(R.id.notes_poster);
			notes_tv.setVisibility(View.INVISIBLE);
		}

		if (uri != null) {
			// mostra la foto mettendo l'uri che prendi dal db
			Bitmap bitmap = null;
			Uri img_uri = Uri.parse(uri);
			Toast t = Toast.makeText(getApplicationContext(), uri,
					Toast.LENGTH_LONG);
			t.show();
			try {
				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(img_uri));
				ImageView imgView = (ImageView) findViewById(R.id.imgView);
				imgView.setImageBitmap(bitmap);

			} catch (FileNotFoundException e) {
				CharSequence text = "FILENOTFOUNDEXCEPTION";
				int duration = Toast.LENGTH_LONG;

				Toast toast = Toast.makeText(getApplicationContext(), text,
						duration);
				toast.show();
				e.printStackTrace();
			} catch (IOException e) {
				CharSequence text = "IOEXCEPTION";
				int duration = Toast.LENGTH_LONG;

				Toast toast = Toast.makeText(getApplicationContext(), text,
						duration);
				toast.show();
				e.printStackTrace();
			}

		} else {
			uri_btn.setVisibility(View.VISIBLE);
			uri_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
/*
					String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
							.format(new Date());
					fname = JPEG_FILE_PREFIX + timeStamp + "_"
							+ JPEG_FILE_SUFFIX;

					path = Environment.getExternalStorageDirectory()
							.getAbsolutePath();
					path += "/MyBand/" + concert + "/";
					String pathComplete = path.concat(fname);

					File file = new File(pathComplete);

					file.mkdirs();

					Uri u = Uri.fromFile(file);
					uri_string = u.toString();*/
					Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					/*i.putExtra(MediaStore.EXTRA_OUTPUT, u);
					Toast toast = Toast.makeText(getApplicationContext(),
							"fname = " + fname + "\n path = " + path
									+ "\n completePath = " + pathComplete
									+ "\n Uristring = " + uri_string,
							Toast.LENGTH_LONG);
					toast.show();
*/
					startActivityForResult(i, TAKE_PHOTO_ACTION_CODE);

				}
			});
		}

		Button delete_btn = (Button) findViewById(R.id.delete_btn);
		delete_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String[] args = { concert };
				int res2 = db
						.delete(DBReference.DBPhotos.TABLE_NAME,
								DBReference.DBPhotos.COLUMN_CONCERT_NAME
										+ " = ?", args);
				int res = db
						.delete(DBReference.DBLives.TABLE_NAME,
								DBReference.DBLives.COLUMN_CONCERT_NAME
										+ " = ? ", args);

				if (res == 0) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"LIVE già eliminato", Toast.LENGTH_LONG);
					toast.show();
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"eliminato " + concert, Toast.LENGTH_LONG);
					toast.show();
				}
				finish();
			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == TAKE_PHOTO_ACTION_CODE) {
			if (resultCode == RESULT_OK) {

				Bundle datas = data.getExtras();
				Bitmap myPhoto = (Bitmap) datas.get("data");
				ImageView imgView = (ImageView) findViewById(R.id.imgView);
				imgView.setImageBitmap(myPhoto);

				String album = Environment.getExternalStorageDirectory()
						.toString() + "/MyBand/" + concert;

				saveImage(myPhoto, album);

				uri_btn.setVisibility(View.GONE);

				Uri uri = Uri.fromFile(file);
				String uri_string = uri.toString();
				int rows = 100;
				try {
					ContentValues v = new ContentValues();
					String where = DBReference.DBLives.COLUMN_CONCERT_NAME
							+ " = ?";
					String[] args = { concert };

					v.put(DBReference.DBLives.COLUMN_URI, uri_string);
					rows = db.update(DBReference.DBLives.TABLE_NAME, v, where,
							args);

					v.clear();
					v.put(DBReference.DBPhotos.COLUMN_BAND_NAME, band);
					v.put(DBReference.DBPhotos.COLUMN_CONCERT_NAME, concert);
					v.put(DBReference.DBPhotos.COLUMN_NAME, fname);
					v.put(DBReference.DBPhotos.COLUMN_URI, uri_string);

					long ris = db.insert(DBReference.DBPhotos.TABLE_NAME,
							"null", v);
					if (ris == -1) {
						Toast toast = Toast
								.makeText(getApplicationContext(),
										"ERRORE IN SCRITTURA PHOTOS",
										Toast.LENGTH_LONG);
						toast.show();
					} else {
						Toast toast = Toast.makeText(getApplicationContext(),
								"ID RIGA PHOTOS: " + ris + "\n Name :" + fname
										+ "\n Concert name:" + concert
										+ "\n Band name: " + band + "\n Uri: "
										+ uri_string, Toast.LENGTH_LONG);
						toast.show();
					}

				} catch (SQLException e) {
					Toast toast = Toast
							.makeText(
									getApplicationContext(),
									"Errore di scrittura nel DB, probabilmente in salvataggio in PHOTOS",
									Toast.LENGTH_LONG);
				}

			}
		}

	}

	private void saveImage(Bitmap finalBitmap, String album) {

		File myDir = new File(album);
		myDir.mkdirs();

		// genera nome file
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());

		fname = JPEG_FILE_PREFIX + timeStamp + JPEG_FILE_SUFFIX;
		file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_MOUNTED,
				Uri.parse("file://" + Environment.getExternalStorageDirectory())));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_poster, menu);
		return true;
	}

}

/*
 * 
 * 
 * protected void onActivityResult(int requestCode, int resultCode, Intent data)
 * { super.onActivityResult(requestCode, resultCode, data);
 * 
 * if (requestCode == TAKE_PHOTO_ACTION_CODE) { if (resultCode == RESULT_OK) {
 * 
 * Bundle datas = data.getExtras(); Bitmap myPhoto = (Bitmap) datas.get("data");
 * ImageView imgView = (ImageView) findViewById(R.id.imgView);
 * imgView.setImageBitmap(myPhoto);
 * 
 * String album = Environment.getExternalStorageDirectory() .toString() +
 * "/MyBand/" + concert;
 * 
 * saveImage(myPhoto, album);
 * 
 * uri_btn.setVisibility(View.GONE);
 * 
 * Uri uri = Uri.fromFile(file); String uri_string = uri.toString(); int rows =
 * 100; try { ContentValues v = new ContentValues(); String where =
 * DBReference.DBLives.COLUMN_CONCERT_NAME + " = ?"; String[] args = { concert
 * };
 * 
 * v.put(DBReference.DBLives.COLUMN_URI, uri_string); rows =
 * db.update(DBReference.DBLives.TABLE_NAME, v, where, args);
 * 
 * v.clear(); v.put(DBReference.DBPhotos.COLUMN_BAND_NAME, band);
 * v.put(DBReference.DBPhotos.COLUMN_CONCERT_NAME, concert);
 * v.put(DBReference.DBPhotos.COLUMN_NAME, fname);
 * v.put(DBReference.DBPhotos.COLUMN_URI, uri_string);
 * 
 * long ris = db.insert(DBReference.DBPhotos.TABLE_NAME, "null", v); if (ris ==
 * -1) { Toast toast = Toast .makeText(getApplicationContext(),
 * "ERRORE IN SCRITTURA PHOTOS", Toast.LENGTH_LONG); toast.show(); } else {
 * Toast toast = Toast.makeText(getApplicationContext(), "ID RIGA PHOTOS: " +
 * ris + "\n Name :" + fname + "\n Concert name:" + concert + "\n Band name: " +
 * band + "\n Uri: " + uri_string, Toast.LENGTH_LONG); toast.show(); }
 * 
 * } catch (SQLException e) { Toast toast = Toast .makeText(
 * getApplicationContext(),
 * "Errore di scrittura nel DB, probabilmente in salvataggio in PHOTOS",
 * Toast.LENGTH_LONG); }
 * 
 * } }
 * 
 * } private void saveImage(Bitmap finalBitmap, String album) {
 * 
 * File myDir = new File(album); myDir.mkdirs();
 * 
 * // genera nome file String timeStamp = new
 * SimpleDateFormat("yyyyMMdd_HHmmss") .format(new Date());
 * 
 * fname = JPEG_FILE_PREFIX + timeStamp + JPEG_FILE_SUFFIX; file = new
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
