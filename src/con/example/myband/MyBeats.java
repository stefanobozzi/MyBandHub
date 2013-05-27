package con.example.myband;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MyBeats extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_beats);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_beats, menu);
		return true;
	}

}
