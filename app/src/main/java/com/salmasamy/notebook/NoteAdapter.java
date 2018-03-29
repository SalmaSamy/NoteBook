package com.salmasamy.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.ContentHandler;
import java.util.ArrayList;

/**
 * Created by Salma-Pc on 10/1/2017.
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    //for optimization
    public static class ViewHolder{
        TextView title;
        TextView body;
        ImageView icon;
    }

    public NoteAdapter(Context context, ArrayList<Note> notes){

        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView , ViewGroup parent) {

        //get the data for this position
        Note note = getItem(position);

        ViewHolder viewHolder;

        //Check if an existing view is being reused, otherwise inflate a new view from custom row layout
        if(convertView == null){

            //if there is no view create a new view in the viewHolder
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row,parent,false);

            //get the text from the view to change it with the data

            viewHolder.title = (TextView) convertView.findViewById(R.id.itemTitle);
            viewHolder.body = (TextView) convertView.findViewById(R.id.itemBody);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.itemImg);

            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();


        // fill it with our data
        viewHolder.title.setText(note.getTitle());
        viewHolder.body.setText(note.getMessage());
        viewHolder.icon.setImageResource(note.getAssociatedDrawable());

        //return to display
        return convertView;

    }
}
