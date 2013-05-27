package con.example.myband.DB;

import android.provider.BaseColumns;

public final class DBReference {

	DBReference() {
	}

	public static abstract class DBTexts implements BaseColumns {

		public static final String TABLE_NAME = "LIVES";
		public static final String COLUMN_ID = "ID";
		public static final String COLUMN_NAME = "FILE_NAME";
		public static final String COLUMN_AUTHOR = "AUTHOR";

	}

	public static abstract class DBLives implements BaseColumns {

		public static final String TABLE_NAME = "LIVES";
		public static final String COLUMN_ID = "ID";
		public static final String COLUMN_CONCERT_NAME = "CONCERT_NAME";
		public static final String COLUMN_BAND_NAME = "BAND_NAME";
		public static final String COLUMN_PLACE_NAME = "PLACE_NAME";
		public static final String COLUMN_DATE = "DATE";
		public static final String COLUMN_NOTES = "NOTES";
		public static final String COLUMN_URI = "URI";

		public static final String DELETE_TABLE = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
				+ " (" + COLUMN_CONCERT_NAME + " TEXT PRIMARY KEY, "
				+ COLUMN_BAND_NAME + " TEXT NOT NULL, " + COLUMN_PLACE_NAME
				+ " TEXT, " + COLUMN_DATE + " TEXT, " + COLUMN_NOTES
				+ " TEXT, " + COLUMN_URI + " TEXT);";
	}

	public static abstract class DBPhotos implements BaseColumns {

		public static final String TABLE_NAME = "PHOTOS";
		public static final String COLUMN_ID = "ID";
		public static final String COLUMN_NAME = "PHOTO_NAME";
		public static final String COLUMN_BAND_NAME = "BAND_NAME";
		public static final String COLUMN_CONCERT_NAME = "CONCERT_NAME";
		public static final String COLUMN_URI = "URI";

		public static final String DELETE_TABLE = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
				+ " (" + COLUMN_NAME + " TEXT PRIMARY KEY, " + COLUMN_BAND_NAME
				+ " TEXT, " + COLUMN_CONCERT_NAME
				+ " TEXT REFERENCES " + DBLives.TABLE_NAME + ", " + COLUMN_URI
				+ " TEXT NOT NULL);";
	}

}
