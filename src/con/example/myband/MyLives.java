package con.example.myband;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import con.example.myband.classes.Live;

public class MyLives extends Activity {

	public static final int REQUEST_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_lives);

		ListView list = (ListView) findViewById(R.id.my_lives_list);
		String[] oggetti = { "Add Live", "View Lives (poster)", "Record Live" };
		ArrayList<String> oggetti_list = new ArrayList<String>();

		for (int i = 0; i < oggetti.length; i++) {
			oggetti_list.add(i, oggetti[i]);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, oggetti_list);

		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i;
				switch (position) {
				case 0:
					i = new Intent(MyLives.this, AddLive.class);
					startActivityForResult(i, REQUEST_CODE);
					break;
				case 1:
					i = new Intent(MyLives.this, ViewLives.class);
					startActivity(i);
					break;
				case 2: i = new Intent(MyLives.this, RecordLive.class);
				startActivity(i);
					break;

				}

			}

		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_lives, menu);
		return true;
	}

}
