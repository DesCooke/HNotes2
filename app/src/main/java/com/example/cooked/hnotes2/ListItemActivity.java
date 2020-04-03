package com.example.cooked.hnotes2;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.Database.RecordListItem;
import com.example.cooked.hnotes2.Database.RecordNoteBook;

public class ListItemActivity extends AppCompatActivity
{
    public int noteBookId;
    public RecordNoteBook recordNoteBook;
    public Button btnOk;
    public String action;
    public TextInputLayout tilItemSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        tilItemSummary = findViewById(R.id.tilItemSummary);
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(action.compareTo("add")==0)
                {
                    RecordListItem item=new RecordListItem();
                    item.itemSummary = tilItemSummary.getEditText().getText().toString();
                    item.noteBookId = noteBookId;
                    Database.MyDatabase().addListItem(item);
                    finish();
                }
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            action = extras.getString("ACTION", "");
            noteBookId = extras.getInt("NOTEBOOKID", 0);
            recordNoteBook = Database.MyDatabase().getNoteBook(noteBookId);
            setTitle("Adding an item to '" + recordNoteBook.getName() + "' list");
        }
    }
}
