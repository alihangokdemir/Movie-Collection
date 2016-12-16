package com.cse486.HW5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseConnector {
	// database name
	private static final String DATABASE_NAME = "MovieCollection";
	private SQLiteDatabase database; // database object
	private DatabaseOpenHelper databaseOpenHelper; // database helper

	// public constructor for DatabaseConnector
	public DatabaseConnector(Context context) {
		// create a new DatabaseOpenHelper
		databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME,
				null, 1);
	} // end DatabaseConnector constructor

	// open the database connection
	public void open() throws SQLException {
		// create or open a database for reading/writing
		database = databaseOpenHelper.getWritableDatabase();
	} // end method open

	// close the database connection
	public void close() {
		if (database != null)
			database.close(); // close the database connection
	} // end method close

	// inserts a new Movie in the database
	public void insertMovie(String name, String director, String year,
			String movie_genre, String imdb_rating) {
		ContentValues newMovie = new ContentValues();
		newMovie.put("name", name);
		newMovie.put("director", director);
		newMovie.put("year", year);
		newMovie.put("movie_genre", movie_genre);
		newMovie.put("imdb_rating", imdb_rating);

		open(); // open the database
		database.insert("movies", null, newMovie);
		close(); // close the database
	} // end method insertMovie

	// inserts a new Movie in the database
	public void updateMovie(long id, String name, String director, String year,
			String movie_genre, String imdb_rating) {
		ContentValues editMovie = new ContentValues();
		editMovie.put("name", name);
		editMovie.put("director", director);
		editMovie.put("year", year);
		editMovie.put("movie_genre", movie_genre);
		editMovie.put("imdb_rating", imdb_rating);

		open(); // open the database
		database.update("movies", editMovie, "_id=" + id, null);
		close(); // close the database
	} // end method updateMovie

	// return a Cursor with all movie information in the database
	public Cursor getAllMovies() {
		return database.query("movies", new String[] { "_id", "name" }, null,
				null, null, null, "name");
	} // end method getAllmovies

	// get a Cursor containing all information about the movie specified
	// by the given id
	public Cursor getOneMovie(long id) {
		return database.query("movies", null, "_id=" + id, null, null, null,
				null);
	} // end method getOnMovie

	// delete the movie specified by the given String name
	public void deleteMovie(long id) {
		open(); // open the database
		database.delete("movies", "_id=" + id, null);
		close(); // close the database
	} // end method deleteMovie

	private class DatabaseOpenHelper extends SQLiteOpenHelper {
		// public constructor
		public DatabaseOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		} // end DatabaseOpenHelper constructor

		// creates the movies table when the database is created
		@Override
		public void onCreate(SQLiteDatabase db) {
			// query to create a new table named movies
			String createQuery = "CREATE TABLE movies"
					+ "(_id integer primary key autoincrement,"
					+ "name TEXT, director TEXT, year TEXT,"
					+ "movie_genre TEXT, imdb_rating TEXT);";

			db.execSQL(createQuery); // execute the query
		} // end method onCreate

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		} // end method onUpgrade
	} // end class DatabaseOpenHelper
} // end class DatabaseConnector
