package com.example.cooked.hnotes2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.Database.RecordNoteBook;
import com.example.cooked.hnotes2.Database.RecordPage;

public class PageActivity extends AppCompatActivity {

    public int noteBookId;
    public String action;
    public RecordNoteBook recordNoteBook;
    public RecordPage recordPage;
    public EditText edtPageText;
    public RecordPage[] recordPageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        edtPageText = findViewById(R.id.edtPageText);
        setSupportActionBar(toolbar);

        noteBookId = 0;
        action = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            action = extras.getString("ACTION", "");
            noteBookId = extras.getInt("NOTEBOOKID", 0);
            recordNoteBook = Database.MyDatabase().getNoteBook(noteBookId);
            recordPageList = Database.MyDatabase().getPageList(noteBookId);
            setTitle("Action " + action + ", NoteBookId " + noteBookId + ", PageCount " + recordNoteBook.PageCount);
            edtPageText.setText(recordPageList[0].getContent());
        }
    }

}
