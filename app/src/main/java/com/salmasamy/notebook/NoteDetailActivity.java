package com.salmasamy.notebook;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

public class NoteDetailActivity extends AppCompatActivity {

    public static final String NEW_NOTE = "new note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        createFragment();
    }

    private void createFragment(){

        // get the intent to know witch fragment to launch
        Intent intent = getIntent();
        MainActivity.FragmentToLaunch fragmentToLaunch =
                (MainActivity.FragmentToLaunch) intent.getSerializableExtra(MainActivity.NOTE_FRAGMENT);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //choose our right fragment to load
        switch(fragmentToLaunch){
            case EDIT:
                NoteEditFragment noteEditFragment = new NoteEditFragment();
                setTitle(R.string.editFragmentTitle);
                fragmentTransaction.add(R.id.note_container , noteEditFragment , "Note_Edit_Fragment");
                break;
            case VIEW:
                NoteViewFragment noteViewFragment = new NoteViewFragment();
                setTitle(R.string.viewFragmentTitle);
                fragmentTransaction.add(R.id.note_container , noteViewFragment , "Note_View_Fragment");
                break;
            case CREATE:
                NoteEditFragment noteCreateFragment = new NoteEditFragment();
                setTitle(R.string.createFragmentTitle);
                // Bundles are for sending information to a fragment
                Bundle bundle = new Bundle();
                bundle.putBoolean(NEW_NOTE, true);
                //add them to the data sent
                noteCreateFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.note_container , noteCreateFragment , "Note_Create_Fragment");
                break;

        }
        fragmentTransaction.commit();

    }
}
