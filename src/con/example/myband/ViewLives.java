package con.example.myband;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import con.example.myband.DB.DBManager;
import con.example.myband.DB.DBReference;

public class ViewLives extends Activity {

	public static final String GET_SELECTED_ITEM = "selected_item";
	ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_lives);

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

		list = (ListView) findViewById(R.id.list_viewlives);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.list_view_layout, arList);

		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String item_selected = (String) parent
						.getItemAtPosition(position);
				Intent i = new Intent(getApplicationContext(), ViewPoster.class);
				i.putExtra(GET_SELECTED_ITEM, item_selected);
				startActivity(i);

			}
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				
				return false;
			}
		});

	}
	
	protected void onResume(){
		super.onResume();
		list.invalidateViews();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_lives, menu);
		return true;
	}

}

