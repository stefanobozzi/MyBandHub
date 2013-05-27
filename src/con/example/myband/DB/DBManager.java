package con.example.myband.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import con.example.myband.DB.DBReference.DBLives;
import con.example.myband.DB.DBReference.DBPhotos;

//PER ORA FUNZIONA SOLO PER I LIVE!!!!!!!!!!!!!!!!!!!!!!(vedi oncreate e ontutto)

public class DBManager extends SQLiteOpenHelper{

	public static final String DATABASE_NAME = "BandDB.db";
	public static final int DATABASE_VERSION = 1;

	
	public DBManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBLives.CREATE_TABLE);
		//db.execSQL(DBTexts.CREATE_TABLE);
		db.execSQL(DBPhotos.CREATE_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//query per upgrade modifica la struttura del db
		
	}

}
