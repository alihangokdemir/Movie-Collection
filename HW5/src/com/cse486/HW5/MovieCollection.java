package com.cse486.HW5;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MovieCollection extends ListActivity {
	public static final String ROW_ID = "row_id";
	private ListView movieListView;
	private CursorAdapter movieAdapter;

	// called when the activity is first created
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); // call super's onCreate
		movieListView = getListView(); // get the built-in ListView
		movieListView.setOnItemClickListener(ViewMovieListener);

		String[] from = new String[] { "name" };
		int[] to = new int[] { R.id.movieTextView };
		movieAdapter = new SimpleCursorAdapter(MovieCollection.this,
				R.layout.movie_list_item, null, from, to);
		setListAdapter(movieAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume(); // call super's onResume method

		// create new GetMoviesTask and execute it
		new GetMoviesTask().execute((Object[]) null);
	} // end method onResume

	@Override
	protected void onStop() {
		Cursor cursor = movieAdapter.getCursor(); // get current Cursor

		if (cursor != null)
			cursor.deactivate(); // deactivate it

		movieAdapter.changeCursor(null); // adapted now has no Cursor
		super.onStop();
	} // end method onStop

	// performs database query outside GUI thread
	private class GetMoviesTask extends AsyncTask<Object, Object, Cursor> {
		DatabaseConnector databaseConnector = new DatabaseConnector(
				MovieCollection.this);

		// perform the database access
		@Override
		protected Cursor doInBackground(Object... params) {
			databaseConnector.open();

			// get a cursor containing call movies
			return databaseConnector.getAllMovies();
		} // end method doInBackground

		// use the Cursor returned from the doInBackground method
		@Override
		protected void onPostExecute(Cursor result) {
			movieAdapter.changeCursor(result); // set the adapter's Cursor
			databaseConnector.close();
		} // end method onPostExecute
	} // end class GetMoviesTask

	// create the Activity's menu from a menu resource XML file
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.moviecollection_menu, menu);
		return true;
	} // end method onCreateOptionsMenu

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// create a new Intent to launch the AddEditMovie Activity
		Intent addNewMovie = new Intent(MovieCollection.this,
				AddEditMovie.class);
		startActivity(addNewMovie); // start the AddEditMovie Activity
		return super.onOptionsItemSelected(item); // call super's method
	} // end method onOptionsItemSelected

	// event listener that responds to the user touching a movie's name
	// in the ListView
	OnItemClickListener ViewMovieListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// create an Intent to launch the ViewMovie Activity
			Intent ViewMovie = new Intent(MovieCollection.this, ViewMovie.class);

			// pass the selected movie's row ID as an extra with the Intent
			ViewMovie.putExtra(ROW_ID, arg3);
			startActivity(ViewMovie); // start the ViewMovie Activity
		} // end method onItemClick
	}; // end ViewMovieListener
} // end class MovieCollection
