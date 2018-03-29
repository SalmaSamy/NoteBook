package com.salmasamy.notebook;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    private ImageButton icon ;
    private Note.Category savedButtenCategory;
    private AlertDialog categoryDialogObject, confirmDialogObject;
    private EditText title, msg;
    private long noteID = 0;

    private boolean newNote = false;

    private static final String modifiedCategory = "Modified Category";

    public NoteEditFragment() {
        // Required empty public constructor
    }

    //if we change the orientation then it will be executed again
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get the bundle that is true only if we're creating a note
        Bundle bundle = this.getArguments();

        if(bundle != null){
            newNote = bundle.getBoolean(NoteDetailActivity.NEW_NOTE, false);
        }
        if(savedInstanceState != null){
            savedButtenCategory = (Note.Category) savedInstanceState.get(modifiedCategory);
        }

        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);

        title = (EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        msg = (EditText) fragmentLayout.findViewById(R.id.editNoteBody);
        icon = (ImageButton) fragmentLayout.findViewById(R.id.editNoteImg);

        Button savedButton = (Button) fragmentLayout.findViewById(R.id.saveNote);

        Intent intent = getActivity().getIntent();
        String newTitle = intent.getExtras().getString(MainActivity.NOTE_TITLE, "");
        String body = intent.getExtras().getString(MainActivity.NOTE_BODY, "");
        noteID = intent.getExtras().getLong(MainActivity.NOTE_ID, 0);

        title.setText(newTitle);
        msg.setText(body);

        //if we grabbed something from out bundle then orientation has been changed
        // and there is some changes that we want to get
        if(savedInstanceState != null){
            icon.setImageResource(Note.categoryToDrawable(savedButtenCategory));
        }
        else if(!newNote) {
            Note.Category noteCat = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY);
            savedButtenCategory = noteCat;
            icon.setImageResource(Note.categoryToDrawable(noteCat));
        }

        buildCategoryDialog();
        icon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                categoryDialogObject.show();
            }
        });
        buildConfirmDialog();
        savedButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                confirmDialogObject.show();
            }
        });

        // Inflate the layout for this fragment
        return fragmentLayout;
    }

    // saving some info to use if orientation changes
    @Override
    public  void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        //key we can later restore
        savedInstanceState.putSerializable(modifiedCategory , savedButtenCategory);
    }

    private void buildCategoryDialog(){

        final String[] categories = new String[]{"Personal", "Technical", "Quote", "Finance"} ;
        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(getActivity());

        categoryBuilder.setTitle("Choose Note Type");

        //creating the options menu, giving it the choices , the default value, onClickListener
        categoryBuilder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener(){

            //onClickListener to know what to do when clicking one choice
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                // dismisses out dialog window after choosing one option
                categoryDialogObject.cancel();
                switch(item){
                    case 0:
                        savedButtenCategory = Note.Category.PERSONAL;
                        icon.setImageResource(R.drawable.p);
                        break;
                    case 1:
                        savedButtenCategory = Note.Category.TECHNICAL;
                        icon.setImageResource(R.drawable.t);
                        break;
                    case 2:
                        savedButtenCategory = Note.Category.QUOTE;
                        icon.setImageResource(R.drawable.q);
                        break;
                    case 3:
                        savedButtenCategory = Note.Category.FINANCE;
                        icon.setImageResource(R.drawable.f);
                        break;
                }
            }
        });

        categoryDialogObject = categoryBuilder.create();
    }

    private void buildConfirmDialog(){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Are you sure?");
        confirmBuilder.setMessage("Are you sure you want to save the note?");


        confirmBuilder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //saving our note in the DB
                NoteDbAdapter noteDbAdapter = new NoteDbAdapter(getActivity().getBaseContext());
                noteDbAdapter.open();
                if(newNote){
                    // in CREATE mode so save it
                    noteDbAdapter.createNote(title.getText()+"", msg.getText()+"",
                            (savedButtenCategory == null)? Note.Category.PERSONAL : savedButtenCategory);

                }else
                {
                    // in EDIT mode so update it
                    noteDbAdapter.updateNote(noteID, title.getText()+"", msg.getText()+"", savedButtenCategory);

                }
                noteDbAdapter.close();
                // back to main Activity
                Intent intent = new Intent(getActivity() , MainActivity.class);
                startActivity(intent);
            }
        });

        confirmBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        confirmDialogObject = confirmBuilder.create();

    }

}
