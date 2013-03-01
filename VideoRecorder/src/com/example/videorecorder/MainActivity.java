package com.example.videorecorder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public final class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fullscreen, menu);
        return true;
    }
    
    public void recordWithPreview(View view) {
    	Intent intent = new Intent(this, RecordActivity.class);
    	intent.putExtra(RecordActivity.WITH_PREVIEW_EXTRA_STRING, true);
    	startActivity(intent);
    }
    
    public void recordWithoutPreview(View view) {
    	Intent intent = new Intent(this, RecordActivity.class);
    	intent.putExtra(RecordActivity.WITH_PREVIEW_EXTRA_STRING, false);
    	startActivity(intent);
    }
}
