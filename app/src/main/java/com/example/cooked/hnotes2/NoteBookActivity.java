package com.example.cooked.hnotes2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.Database.RecordNoteBook;

public class NoteBookActivity extends AppCompatActivity
{
    public Button btnOk;
    public int noteBookId;
    public String action;
    public TextInputLayout edtName;
    public TextInputLayout edtShortDescription;
    public RecordNoteBook rec;
    public final int SELECT_DEVICE_PHOTO=1;
    public final int SELECT_INTERNAL_PHOTO=2;
    public RadioButton radNotebook;
    public RadioButton radList;
    public RadioGroup grpNoteBookTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtName = findViewById(R.id.edtName);
        edtShortDescription = findViewById(R.id.edtShortDescription);
        radNotebook = findViewById(R.id.radNotebook);
        radList = findViewById(R.id.radList);
        grpNoteBookTypes = findViewById(R.id.grpNoteBookTypes);

        btnOk = findViewById(R.id.btnOk);

        noteBookId = 0;
        action="";
        Bundle extras=getIntent().getExtras();
        if(extras != null)
        {
            action = extras.getString("ACTION", "");

            grpNoteBookTypes.setEnabled(true);
            radList.setEnabled(true);
            radNotebook.setEnabled(true);
            radList.setChecked(false);
            radNotebook.setChecked(false);
            if (action.compareTo("add") == 0)
            {
                toolbar.setTitle("Add a new Note Book");
                grpNoteBookTypes.setEnabled(true);
                edtName.getEditText().setText("");
                edtShortDescription.getEditText().setText("");
                rec = new RecordNoteBook();
            }

            if (action.compareTo("modify") == 0) {
                grpNoteBookTypes.setEnabled(false);
                radList.setEnabled(false);
                radNotebook.setEnabled(false);
                noteBookId = extras.getInt("NOTEBOOKID", 0);

                rec = Database.MyDatabase().getNoteBook(noteBookId);

                toolbar.setTitle("Modify Note Book");

                edtName.getEditText().setText(rec.getName());
                edtShortDescription.getEditText().setText(rec.getShortDescription());

                if(rec.BookType==getResources().getInteger(R.integer.list))
                    radList.setChecked(true);
                if(rec.BookType==getResources().getInteger(R.integer.notebook))
                    radNotebook.setChecked(true);

            }
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(action.compareTo("add")==0) {
                    rec.setId(Database.MyDatabase().getNextNoteBookId());
                    rec.setName(edtName.getEditText().getText().toString());
                    rec.setShortDescription(edtShortDescription.getEditText().getText().toString());
                    if(radList.isChecked())
                        rec.BookType=getResources().getInteger(R.integer.list);
                    Database.MyDatabase().addNoteBook(rec);
                }
                if(action.compareTo("modify")==0) {
                    rec.setName(edtName.getEditText().getText().toString());
                    rec.setShortDescription(edtShortDescription.getEditText().getText().toString());
                    Database.MyDatabase().updateNoteBook(rec);
                }
                finish();
            }
        });
    }

}
