package com.cse486.HW5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class ViewMovie extends Activity {
	private long rowID; // selected movie's name
	private TextView titleTextView; // displays movie's name
	private TextView yearTextView; // displays movie's year
	private TextView directorTextView; // displays movie's director
	private TextView movie_genreTextView; // displays movie's movie_genre
	private TextView imdb_ratingTextView; // displays movie's imdb_rating
	private Button trailerButton;

	// called when the activity is first created
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_movie);

		// get the EditTexts
		titleTextView = (TextView) findViewById(R.id.titleTextView);
		yearTextView = (TextView) findViewById(R.id.yearTextView);
		directorTextView = (TextView) findViewById(R.id.directorTextView);
		movie_genreTextView = (TextView) findViewById(R.id.movie_genreTextView);
		imdb_ratingTextView = (TextView) findViewById(R.id.imdb_ratingTextView);
		trailerButton = (Button) findViewById(R.id.trailerButton);
		// get the selected movie's row ID
		Bundle extras = getIntent().getExtras();
		rowID = extras.getLong(MovieCollection.ROW_ID);
		trailerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ViewMovie.this, Youtube.class);
				i.putExtra("search", titleTextView.getText().toString()
						+ " Trailer");
				startActivity(i);
			}

		});
	} // end method onCreate

	// called when the activity is first created
	@Override
	protected void onResume() {
		super.onResume();

		// create new LoadMovieTask and execute it
		new LoadMovieTask().execute(rowID);
	} // end method onResume

	// performs database query outside GUI thread
	private class LoadMovieTask extends AsyncTask<Long, Object, Cursor> {
		DatabaseConnector databaseConnector = new DatabaseConnector(
				ViewMovie.this);

		// perform the database access
		@Override
		protected Cursor doInBackground(Long... params) {
			databaseConnector.open();

			// get a cursor containing all data on given entry
			return databaseConnector.getOneMovie(params[0]);
		} // end method doInBackground

		// use the Cursor returned from the doInBackground method
		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			result.moveToFirst(); // move to the first item

			// get the column index for each data item
			int nameIndex = result.getColumnIndex("name");
			int yearIndex = result.getColumnIndex("year");
			int directorIndex = result.getColumnIndex("director");
			int movie_genreIndex = result.getColumnIndex("movie_genre");
			int imdb_ratingIndex = result.getColumnIndex("imdb_rating");

			// fill TextViews with the retrieved data
			titleTextView.setText(result.getString(nameIndex));
			yearTextView.setText(result.getString(yearIndex));
			directorTextView.setText(result.getString(directorIndex));
			movie_genreTextView.setText(result.getString(movie_genreIndex));
			imdb_ratingTextView.setText(result.getString(imdb_ratingIndex));

			result.close(); // close the result cursor
			databaseConnector.close(); // close database connection
		} // end method onPostExecute
	} // end class LoadMovieTask

	// create the Activity's menu from a menu resource XML file
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_movie_menu, menu);
		return true;
	} // end method onCreateOptionsMenu

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.editItem:
			// create an Intent to launch the AddEditMovie Activity
			Intent addEditMovie = new Intent(this, AddEditMovie.class);

			// pass the selected Movie's data as extras with the Intent
			addEditMovie.putExtra(MovieCollection.ROW_ID, rowID);
			addEditMovie.putExtra("name", titleTextView.getText());
			addEditMovie.putExtra("year", yearTextView.getText());
			addEditMovie.putExtra("director", directorTextView.getText());
			addEditMovie.putExtra("movie_genre", movie_genreTextView.getText());
			addEditMovie.putExtra("imdb_rating", imdb_ratingTextView.getText());
			startActivity(addEditMovie); // start the Activity
			return true;
		case R.id.deleteItem:
			deleteMovie(); // delete the displayed Movie
			return true;
		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

	// delete a Movie
	private void deleteMovie() {
		// create a new AlertDialog Builder
		AlertDialog.Builder builder = new AlertDialog.Builder(ViewMovie.this);

		builder.setTitle(R.string.confirmTitle); // title bar string
		builder.setMessage(R.string.confirmMessage); // message to display

		// provide an OK button that simply dismisses the dialog
		builder.setPositiveButton(R.string.button_delete,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int button) {
						final DatabaseConnector databaseConnector = new DatabaseConnector(
								ViewMovie.this);

						// create an AsyncTask that deletes the Movie in
						// another
						// thread, then calls finish after the deletion
						AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>() {
							@Override
							protected Object doInBackground(Long... params) {
								databaseConnector.deleteMovie(params[0]);
								return null;
							} // end method doInBackground

							@Override
							protected void onPostExecute(Object result) {
								finish(); // return to the MovieCollection
											// Activity
							} // end method onPostExecute
						}; // end new AsyncTask

						// execute the AsyncTask to delete Movie at rowID
						deleteTask.execute(new Long[] { rowID });
					} // end method onClick
				} // end anonymous inner class
		); // end call to method setPositiveButton

		builder.setNegativeButton(R.string.button_cancel, null);
		builder.show(); // display the Dialog
	} // end method deleteMovie

} // end class ViewMovie

