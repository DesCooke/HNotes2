package com.example.cooked.hnotes2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.Database.RecordNoteBook;
import com.example.cooked.hnotes2.Utils.InternalImageList;

public class NoteBookActivity extends AppCompatActivity  implements View.OnClickListener
{
    public ImageView imageBook;
    public Button btnOk;
    public Button btnCancel;
    public int noteBookId;
    public String action;
    public TextInputLayout edtName;
    public TextInputLayout edtShortDescription;
    public RecordNoteBook rec;
    public final int SELECT_DEVICE_PHOTO=1;
    public final int SELECT_INTERNAL_PHOTO=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageBook = findViewById(R.id.imageBook);
        edtName = findViewById(R.id.edtName);
        edtShortDescription = findViewById(R.id.edtShortDescription);

        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        imageBook.setOnClickListener(this);

        noteBookId = 0;
        action="";
        Bundle extras=getIntent().getExtras();
        if(extras != null)
        {
            action = extras.getString("ACTION", "");

            if (action.compareTo("add") == 0)
            {
                toolbar.setTitle("Add a new Note Book");

                edtName.getEditText().setText("");
                edtShortDescription.getEditText().setText("");
            }

            if (action.compareTo("modify") == 0) {
                noteBookId = extras.getInt("NOTEBOOKID", 0);

                rec = Database.MyDatabase().getNoteBook(noteBookId);

                toolbar.setTitle("Modify Note Book");

                edtName.getEditText().setText(rec.getName());
                edtShortDescription.getEditText().setText(rec.getShortDescription());
            }
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(action.compareTo("add")==0) {
                    RecordNoteBook rec = new RecordNoteBook();
                    rec.setId(Database.MyDatabase().getNextNoteBookId());
                    rec.setName(edtName.getEditText().getText().toString());
                    rec.setShortDescription(edtShortDescription.getEditText().getText().toString());
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
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.imageBook:
                pickImage(view);
                break;
        }
    }

    public void pickImage(View view)
    {
        Intent intent=new Intent(getApplicationContext(), InternalImageList.class);
        startActivityForResult(intent, SELECT_INTERNAL_PHOTO);
    }


}
