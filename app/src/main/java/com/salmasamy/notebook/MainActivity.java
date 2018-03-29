package com.salmasamy.notebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    public static final String NOTE_ID = "com.salmasamy.notebook.id";
    public static final String NOTE_TITLE = "com.salmasamy.notebook.title";
    public static final String NOTE_BODY = "com.salmasamy.notebook.body";
    public static final String NOTE_CATEGORY = "com.salmasamy.notebook.category";

    public enum FragmentToLaunch{VIEW, EDIT, CREATE};
    public static final String NOTE_FRAGMENT = "com.salmasamy.notebook.fragment_to_load";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabAction();
            }
        });

        loadPreferences();
    }

    private void fabAction(){
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra(MainActivity.NOTE_FRAGMENT, FragmentToLaunch.CREATE);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AppPreferences.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isBackgroundDark = sharedPreferences.getBoolean("background_color", false);
        if(isBackgroundDark){
            LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainActivityLayout);
            mainLayout.setBackgroundColor(Color.parseColor("#3c3f41"));
        }
        String notebookTitle = sharedPreferences.getString("title", "NoteBook");
        setTitle(notebookTitle);
    }







}
