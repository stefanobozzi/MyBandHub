package con.example.myband;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

/*
 * IN HOME SCEGLI SE VEDERE PER CONCERTO O PER BAND
 * AGGIUNGI CONTROLLI A MANETTA CHE IL NOME DELLA BAND SIA STATO INSERITO NEL DB
 * AGGIUNGI UN ALTRO DATABASE PER LE FOTO CONTENENTE L'URI ETC ETC IN MODO DA POTER FAR
 * VEDERE LE FOTO PER ALBUM O PER BAND
 * 
 */

public class MyGallery extends Activity {

	public final static String CONCERT_SELECTED_KEY = "albumkey";
	
	RadioButton view_all;
	RadioButton view_album;
	ArrayList<String> albumNames;
	String root; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_gallery);

		
		//legge i nomi delle cartelle presenti e riempie la lista, sul click dell'utente
		//l'app rimanda a ViewGallery.java a cui passa il PATH COMPLETO della directory scelta)
		
		
		albumNames = new ArrayList<String>();

		root = Environment.getExternalStorageDirectory().toString()
				+ "/MyBand/";
		Toast toast = Toast.makeText(getApplicationContext(), root, Toast.LENGTH_LONG);
		toast.show();

		ListView lv = (ListView) findViewById(R.id.gallery_lv);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.list_view_layout,
				albumNames);

		File home = new File(root);

		File[] files = home.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				albumNames.add(file.getName());
			}

		}
		
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				String concert_selected = (String)arg0.getItemAtPosition(position);
				
				/*String complete_path = root + item_selected + "/";
				File f = new File(complete_path);*/
				
				
				Intent i = new Intent(MyGallery.this, ViewGallery.class);
				i.putExtra(CONCERT_SELECTED_KEY, concert_selected);
				startActivity(i);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_gallery, menu);
		return true;
	}

}

/*
 * private List<File> getListFiles(File parentDir) { ArrayList<File> inFiles =
 * new ArrayList<File>(); File[] files = parentDir.listFiles(); for (File file :
 * files) { if (file.isDirectory()) { inFiles.addAll(getListFiles(file)); } else
 * { if (file.getName().endsWith(".csv")) { inFiles.add(file); } } } return
 * inFiles; }
 */





















/*
 * Gallery g = (Gallery) findViewById(R.id.gallery);
// request only the image ID to be returned
String[] projection = {MediaStore.Images.Media._ID};
// Create the cursor pointing to the SDCard
cursor = managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection, 
        MediaStore.Images.Media.DATA + " like ? ",
        new String[] {"%myimagesfolder%"},  
        null);
// Get the column index of the image ID
columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
g.setAdapter(new ImageAdapter(this));

Then, in the ImageAdapter for the Gallery, obtain the thumbnail to display:

public View getView(int position, View convertView, ViewGroup parent) {
    ImageView i = new ImageView(context);
    // Move cursor to current position
    cursor.moveToPosition(position);
    // Get the current value for the requested column
    int imageID = cursor.getInt(columnIndex);
    // obtain the image URI
    Uri uri = Uri.withAppendedPath( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(imageID) );
    String url = uri.toString();
    // Set the content of the image based on the image URI
    int originalImageId = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1, url.length()));
    Bitmap b = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(),
                    originalImageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
    i.setImageBitmap(b);
    i.setLayoutParams(new Gallery.LayoutParams(150, 100));
    i.setScaleType(ImageView.ScaleType.FIT_XY);
    i.setBackgroundResource(mGalleryItemBackground);
    return i;
}
*/