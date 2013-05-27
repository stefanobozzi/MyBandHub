package con.example.myband;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        
        
        Button my_lives_btn = (Button)findViewById(R.id.my_lives_btn);
        Button my_gallery_btn = (Button)findViewById(R.id.my_gallery_btn);
        Button my_texts_btn = (Button)findViewById(R.id.my_texts_btn);
        Button my_beats_btn = (Button)findViewById(R.id.my_beats_btn);
        
        
        
        my_lives_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, MyLives.class);
				startActivity(i);
				
			}       	
        });
        my_gallery_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, MyGallery.class);
				startActivity(i);
				
			}       	
        });
        my_texts_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, MyTexts.class);
				startActivity(i);
				
			}       	
        });
        my_beats_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, MyBeats.class);
				startActivity(i);
				
			}       	
        });
    
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
