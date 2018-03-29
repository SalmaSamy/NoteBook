package com.salmasamy.notebook;


import android.content.Intent;
import android.os.Bundle;
import com.github.clans.fab.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteViewFragment extends Fragment {

    FloatingActionButton editButton;
    FloatingActionButton deleteButton;

    public NoteViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentLayout = inflater.inflate(R.layout.fragment_note_view, container, false);

        TextView title = (TextView) fragmentLayout.findViewById(R.id.viewNoteTitle);
        TextView body = (TextView) fragmentLayout.findViewById(R.id.viewNoteBody);
        ImageView icon = (ImageView) fragmentLayout.findViewById(R.id.viewNoteImg);
        editButton =(FloatingActionButton) fragmentLayout.findViewById(R.id.fab_edit);
        deleteButton =(FloatingActionButton) fragmentLayout.findViewById(R.id.fab_delete);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                NoteEditFragment noteEditFragment = new NoteEditFragment();
                fragmentTransaction.replace(R.id.note_container , noteEditFragment);
                fragmentTransaction.commit();
            }
        });

        Intent intent = getActivity().getIntent();

        final long noteID = intent.getExtras().getLong(MainActivity.NOTE_ID, 0);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NoteDbAdapter noteDbAdapter = new NoteDbAdapter(getActivity().getBaseContext());
                noteDbAdapter.open();
                noteDbAdapter.deleteNote(noteID);
                noteDbAdapter.close();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                MainActivityListFragment mainActivityListFragment = new MainActivityListFragment();
                fragmentTransaction.replace(R.id.mainActivityFrag , mainActivityListFragment);
                fragmentTransaction.commit();
            }
        });


        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE));
        body.setText(intent.getExtras().getString(MainActivity.NOTE_BODY));

        Note.Category noteCat = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY);
        icon.setImageResource(Note.categoryToDrawable(noteCat));

        // Inflate the layout for this fragment
        return fragmentLayout;
    }

}
