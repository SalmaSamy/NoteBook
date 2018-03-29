package com.salmasamy.notebook;

/**
 * Created by Salma-Pc on 10/1/2017.
 */
public class Note {

    private String title;
    private String message;
    private long noteId;
    private long dateCreated;
    private Category category;

    public enum Category{PERSONAL, TECHNICAL, QUOTE, FINANCE}

    public Note(String title, String message, Category category) {
        this.title = title;
        this.message = message;
        this.noteId = 0;
        this.dateCreated = 0;
        this.category = category;
    }

    public Note(String title, String message, long noteId, long dateCreated, Category category) {
        this.title = title;
        this.message = message;
        this.noteId = noteId;
        this.dateCreated = dateCreated;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Note{" + "title='" + title + '\'' + ", message='" + message + '\'' +
                ", noteId=" + noteId + ", dateCreated=" + dateCreated +
                ", category=" + category + '}';
    }

    public int getAssociatedDrawable(){ return categoryToDrawable(category); }

    public static int categoryToDrawable(Category noteCategory){

        switch (noteCategory){
            case PERSONAL:
                return R.drawable.p ;
            case TECHNICAL:
                return R.drawable.t ;
            case FINANCE:
                return R.drawable.f ;
            case QUOTE:
                return R.drawable.q ;
        }

            return R.drawable.p;
    }

}
