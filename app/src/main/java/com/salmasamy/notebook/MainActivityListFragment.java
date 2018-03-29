package com.salmasamy.notebook;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityListFragment extends ListFragment {

    private ArrayList<Note> notes;
    private NoteAdapter noteAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        NoteDbAdapter noteDbAdapter = new NoteDbAdapter(getActivity().getBaseContext());
        noteDbAdapter.open();
        notes = noteDbAdapter.getAllNotes();
        noteDbAdapter.close();

        noteAdapter = new NoteAdapter(getActivity(), notes );
        //display
        setListAdapter(noteAdapter);
        registerForContextMenu(getListView());
    }


    private void startNoteDetailActivity(MainActivity.FragmentToLaunch noteFragment, long noteId){

        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
         for(int i=0 ; i<notes.size() ; i++){

            if(notes.get(i).getNoteId() == noteId){
                Note note = notes.get(i);
                intent.putExtra(MainActivity.NOTE_ID , note.getNoteId());
                intent.putExtra(MainActivity.NOTE_TITLE , note.getTitle());
                intent.putExtra(MainActivity.NOTE_BODY , note.getMessage());
                intent.putExtra(MainActivity.NOTE_CATEGORY , note.getCategory());
                break;
            }
         }

        switch (noteFragment){
            case VIEW:
                intent.putExtra(MainActivity.NOTE_FRAGMENT, MainActivity.FragmentToLaunch.VIEW);
                break;
            case EDIT:
                intent.putExtra(MainActivity.NOTE_FRAGMENT, MainActivity.FragmentToLaunch.EDIT);
                break;

        }

        startActivity(intent);
    }

    @Override
    public void onListItemClick(ListView l , View v, int position , long id){
        super.onListItemClick(l, v, position, id);
        Note note = (Note) getListAdapter().getItem(position);
        startNoteDetailActivity(MainActivity.FragmentToLaunch.VIEW, note.getNoteId());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //give me position of the note i pressed on
        AdapterView.AdapterContextMenuInfo info = ( AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int rowPosition = info.position ;
        Note note = (Note) getListAdapter().getItem(rowPosition);

        switch (item.getItemId()) {
            case R.id.edit:
               startNoteDetailActivity(MainActivity.FragmentToLaunch.EDIT,note.getNoteId());
                return true;
            case R.id.delete:
                NoteDbAdapter noteDbAdapter = new NoteDbAdapter(getActivity().getBaseContext());
                noteDbAdapter.open();
                noteDbAdapter.deleteNote(note.getNoteId());
                notes.clear();
                notes.addAll(noteDbAdapter.getAllNotes());
                noteAdapter.notifyDataSetChanged();
                noteDbAdapter.close();
                return true;
        }

        return super.onContextItemSelected(item);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(contextMenu,v,menuInfo);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.long_press_menu, contextMenu);
    }


}
