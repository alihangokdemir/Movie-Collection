package com.cse486.HW5;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddEditMovie extends Activity {
	private long rowID;

	// EditTexts for movie information
	private EditText titleEditText;
	private EditText yearEditText;
	private EditText directorEditText;
	private EditText movie_genreEditText;
	private EditText imdb_ratingEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_movie);

		titleEditText = (EditText) findViewById(R.id.titleEditText);
		directorEditText = (EditText) findViewById(R.id.directorEditText);
		yearEditText = (EditText) findViewById(R.id.yearEditText);
		movie_genreEditText = (EditText) findViewById(R.id.movie_genreEditText);
		imdb_ratingEditText = (EditText) findViewById(R.id.imdb_ratingEditText);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			rowID = extras.getLong("row_id");
			titleEditText.setText(extras.getString("name"));
			directorEditText.setText(extras.getString("director"));
			yearEditText.setText(extras.getString("year"));
			movie_genreEditText.setText(extras.getString("movie_genre"));
			imdb_ratingEditText.setText(extras.getString("imdb_rating"));
		} // end if

		// set event listener for the Save Movie Button
		Button saveMovieButton = (Button) findViewById(R.id.saveMovieButton);
		saveMovieButton.setOnClickListener(saveMovieButtonClicked);
	} // end method onCreate

	OnClickListener saveMovieButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (titleEditText.getText().length() != 0) {
				AsyncTask<Object, Object, Object> saveMovieTask = new AsyncTask<Object, Object, Object>() {
					@Override
					protected Object doInBackground(Object... params) {
						saveMovie(); // save movie to the database
						return null;
					}

					@Override
					protected void onPostExecute(Object result) {
						finish();
					}
				};

				// save the movie to the database using a separate thread
				saveMovieTask.execute((Object[]) null);
			} // end if
			else {
				// create a new AlertDialog Builder
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AddEditMovie.this);

				// set dialog title & message, and provide Button to dismiss
				builder.setTitle(R.string.errorTitle);
				builder.setMessage(R.string.errorMessage);
				builder.setPositiveButton(R.string.errorButton, null);
				builder.show(); // display the Dialog
			} // end else
		} // end method onClick
	}; // end OnClickListener saveMovieButtonClicked

	// saves movie information to the database
	private void saveMovie() {
		// get DatabaseConnector to interact with the SQLite database
		DatabaseConnector databaseConnector = new DatabaseConnector(this);

		if (getIntent().getExtras() == null) {
			// insert the movie information into the database
			databaseConnector.insertMovie(titleEditText.getText().toString(),
					directorEditText.getText().toString(), yearEditText
							.getText().toString(), movie_genreEditText
							.getText().toString(), imdb_ratingEditText
							.getText().toString());
		} // end if
		else {
			databaseConnector.updateMovie(rowID, titleEditText.getText()
					.toString(), directorEditText.getText().toString(),
					yearEditText.getText().toString(), movie_genreEditText
							.getText().toString(), imdb_ratingEditText
							.getText().toString());
		} // end else
	} // end class saveMovie
} // end class AddEditMovie

