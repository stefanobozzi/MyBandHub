package con.example.myband;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import con.example.myband.DB.DBManager;
import con.example.myband.DB.DBReference.DBLives;
import con.example.myband.classes.Live;

public class AddLive extends Activity {

	// ELIMINIAMO L'ORA??
	EditText concertNameInput;
	EditText bandNameInput;
	CheckBox placeCheckBox;
	EditText placeInput;
	CheckBox dateCheckBox;
	EditText dateInput;
	CheckBox hourCheckBox;
	EditText hourInput;
	CheckBox notesCheckBox;
	EditText notesInput;
	Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_live);

		submit = (Button) findViewById(R.id.submit_btn);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				concertNameInput = (EditText) findViewById(R.id.concert_name_input);
				bandNameInput = (EditText) findViewById(R.id.band_name_input);
				placeCheckBox = (CheckBox) findViewById(R.id.place_checkbox);
				placeInput = (EditText) findViewById(R.id.place_name_input);
				dateCheckBox = (CheckBox) findViewById(R.id.date_checkbox);
				dateInput = (EditText) findViewById(R.id.date_input);
				hourCheckBox = (CheckBox) findViewById(R.id.hour_checkbox);
				hourInput = (EditText) findViewById(R.id.hour_input);
				notesCheckBox = (CheckBox) findViewById(R.id.notes_checkbox);
				notesInput = (EditText) findViewById(R.id.notes_input);
	
				// crea l'oggetto Live con tutte le info che servono
				Live live = new Live();
				live.setLive_name(concertNameInput.getText().toString());
				live.setBand_name(bandNameInput.getText().toString());
				if (placeCheckBox.isChecked()) {
					live.setPlace(placeInput.getText().toString());
				}

				if (dateCheckBox.isChecked()) {
					live.setDate(dateInput.getText().toString());
				}
				
				if (notesCheckBox.isChecked()) {
					live.setNotes(notesInput.getText().toString());
				}

				// il parametro è l'oggetto Live

				new WriteToDB().execute(live);
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_live, menu);
		return true;
	}

	// CAMBIAMENTO: PASSARE L'OGGETTO LIVE DURANTE .execute
	private class WriteToDB extends AsyncTask<Live, Integer, Long> {

		Long primary_key;

		@Override
		protected Long doInBackground(Live... params) {
			// APRI DB E SALVACI DENTRO LE ROBE.............

			// apro quello che mi consente di accedere al db
			DBManager dbManager = new DBManager(AddLive.this);

			// ottengo l'oggetto db su cui piazzare le query (se non c'è lo
			// crea)
			SQLiteDatabase db = dbManager.getWritableDatabase();

			// creo il contenitore dei valori
			ContentValues values = new ContentValues();
			// per ogni colonna
			values.put(DBLives.COLUMN_CONCERT_NAME, params[0].getLive_name());
			values.put(DBLives.COLUMN_BAND_NAME, params[0].getBand_name());
			values.put(DBLives.COLUMN_DATE, params[0].getDate());
			values.put(DBLives.COLUMN_PLACE_NAME, params[0].getPlace());
			values.put(DBLives.COLUMN_NOTES, params[0].getNotes());

			primary_key = db.insert(DBLives.TABLE_NAME, "null", values);

			return null;
		}

		protected void onPostExecute(Long result) {
			Context context = getApplicationContext();
			CharSequence text;
			if (primary_key == -1) {
				text = "POVERO ILLUSO HAI CANNATO";
			} else {
				text = "ID riga inserita: " + primary_key;
			}
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

	}
}
