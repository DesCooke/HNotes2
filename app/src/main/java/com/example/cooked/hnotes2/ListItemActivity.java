package com.example.cooked.hnotes2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    public int itemId;
    public RecordListItem recordListItem;

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

                    Intent intent = new Intent();
                    intent.putExtra("ACTION", action);
                    intent.putExtra("ITEMID", item.itemId);
                    setResult(RESULT_OK, intent);

                    finish();
                }
                if(action.compareTo("edit")==0)
                {
                    recordListItem.itemSummary = tilItemSummary.getEditText().getText().toString();
                    Database.MyDatabase().updateListItem(recordListItem);

                    Intent intent = new Intent();
                    intent.putExtra("ACTION", action);
                    intent.putExtra("ITEMID", recordListItem.itemId);
                    setResult(RESULT_OK, intent);

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

            if(action.compareTo("add")==0)
            {
                setTitle("Adding an item to '" + recordNoteBook.getName() + "' list");
            }
            if(action.compareTo("edit")==0)
            {
                itemId=extras.getInt("ITEMID", 0);
                recordListItem = Database.MyDatabase().getListItem(itemId);
                tilItemSummary.getEditText().setText(recordListItem.itemSummary);
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_listitem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.mnuDelete:
                DialogInterface.OnClickListener dialogClickListener2 =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Database.MyDatabase().deleteListItem(recordListItem);
                                        Intent intent = new Intent();
                                        intent.putExtra("ACTION", "delete");
                                        intent.putExtra("ITEMID", recordListItem.itemId);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("Really delete this item?").setPositiveButton("Yes", dialogClickListener2)
                        .setNegativeButton("No", dialogClickListener2).show();

                break;
            default:
                break;
        }

        return true;
    }
}
