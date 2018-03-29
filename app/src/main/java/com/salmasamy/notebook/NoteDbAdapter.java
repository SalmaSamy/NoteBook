package com.salmasamy.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Salma-Pc on 10/4/2017.
 */
public class NoteDbAdapter {

    private static final String DATABASE_NAME = "notebook.db";
    private static final int DATABASE_VERSION = 1;

    public static final String NOTE_TABLE = "note";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DATE = "date";

    private String[] allColumns = {COLUMN_ID,COLUMN_TITLE,COLUMN_MESSAGE,COLUMN_CATEGORY,COLUMN_DATE};

    public static final String CREATE_TABLE_NOTE = "create table " + NOTE_TABLE + " ( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_TITLE + " text not null, " +
            COLUMN_MESSAGE + " text not null, " +
            COLUMN_CATEGORY + " text not null, " +
            COLUMN_DATE + " );" ;

    private SQLiteDatabase sqlDB;
    private Context context;

    private NoteBookDbHelper noteBookDbHelper;


    public NoteDbAdapter(Context ctx){
        context = ctx;
    }

    //whenever we wanna open our DB to do any thing, this grabs our DB and sets it to this !
    // Weird? I know !
    public NoteDbAdapter open() throws android.database.SQLException{
        noteBookDbHelper = new NoteBookDbHelper(context);
        sqlDB = noteBookDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        noteBookDbHelper.close();
    }

    public Note createNote(String title, String msg, Note.Category category){

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_MESSAGE, msg);
        values.put(COLUMN_CATEGORY, category.name());
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() +"");

        Long insertID = sqlDB.insert(NOTE_TABLE, null, values);

        Cursor cursor = sqlDB.query(NOTE_TABLE, allColumns, COLUMN_ID + " = " + insertID , null,null,null,null);

        cursor.moveToFirst();
        Note newNote = cursorToNote(cursor);
        cursor.close();

        return newNote;
    }

    public long deleteNote(long idToDelete)
    {
        return sqlDB.delete(NOTE_TABLE, COLUMN_ID + " = " + idToDelete, null);
    }

    public long updateNote(long idToUpdat, String newTitile, String newMsg, Note.Category newCategory){

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newTitile);
        values.put(COLUMN_MESSAGE, newMsg);
        values.put(COLUMN_CATEGORY, newCategory.name());
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() +"");

        return sqlDB.update(NOTE_TABLE, values, COLUMN_ID + " = " + idToUpdat, null);
    }


    public ArrayList<Note> getAllNotes(){

        ArrayList<Note> notes = new ArrayList<Note>();

        Cursor cursor = sqlDB.query(NOTE_TABLE,allColumns,null,null,null,null,null);

        for(cursor.moveToLast(); !cursor.isBeforeFirst() ; cursor.moveToPrevious()){
            Note note = cursorToNote(cursor);
            notes.add(note);
        }

        cursor.close();
        return notes;
    }

    private Note cursorToNote(Cursor cursor){

        Note newNote = new Note(cursor.getString(1), cursor.getString(2), cursor.getLong(0),
                cursor.getLong(4), Note.Category.valueOf(cursor.getString(3)));

        return newNote;
    }

    // when ever we extend SQLiteOpenHelper we must override 2 methods: onCreate, onUpgrade
    private static class NoteBookDbHelper extends SQLiteOpenHelper{

        NoteBookDbHelper(Context ctx){
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //create our table on starting
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_TABLE_NOTE);
        }

        // this executes when ever we are updating our database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            // just a debug message
            Log.w(NoteBookDbHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to " +
                    newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS "+ NOTE_TABLE);
            onCreate(db);
        }
    }

}
