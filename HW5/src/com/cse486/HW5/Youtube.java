package com.cse486.HW5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Youtube extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youtube_browser);
		Bundle extras = getIntent().getExtras();
		String search = extras.getString("search");
		WebView webview = (WebView) findViewById(R.id.webView1);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl("http://www.youtube.com/results?search_query=" + search);
	}

}
