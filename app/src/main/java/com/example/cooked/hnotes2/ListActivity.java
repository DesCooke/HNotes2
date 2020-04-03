package com.example.cooked.hnotes2;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.Database.RecordListItem;
import com.example.cooked.hnotes2.Database.RecordNoteBook;
import com.example.cooked.hnotes2.UI.ListItemAdapter;

import java.util.List;

public class ListActivity extends AppCompatActivity
{
    FloatingActionButton mFabAdd;
    public int noteBookId;
    public RecordNoteBook recordNoteBook;
    public RecordListItem mDataset[];
    public RecyclerView mItemList;
    public RecyclerView.LayoutManager mLayoutManager;
    public ListItemAdapter mListItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            noteBookId = extras.getInt("NOTEBOOKID", 0);
            recordNoteBook = Database.MyDatabase().getNoteBook(noteBookId);
            setTitle(recordNoteBook.getName());
        }

        mDataset = Database.MyDatabase().getListItems(noteBookId, 0);

        mItemList = (RecyclerView) findViewById(R.id.itemList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mItemList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mItemList.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mListItemAdapter = new ListItemAdapter(mDataset);
        mItemList.setAdapter(mListItemAdapter);

        mFabAdd = findViewById(R.id.fabAdd);
        mFabAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), ListItemActivity.class);
                intent.putExtra("ACTION", "add");
                intent.putExtra("NOTEBOOKID", noteBookId);
                startActivityForResult(intent, getResources().getInteger(R.integer.add_list_item_response));
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == getResources().getInteger(R.integer.add_list_item_response)) {
            if(resultCode == RESULT_OK) {
                int itemId = data.getIntExtra("ITEMID",0);
                RecordListItem rec=Database.MyDatabase().getListItem(itemId);
                RecordListItem[] list;
                list = new RecordListItem[mDataset.length+1];
                for(int i=0;i<mDataset.length;i++)
                {
                    list[i]=mDataset[i];
                }
                list[mDataset.length]=rec;
                mDataset = list;

                mListItemAdapter = new ListItemAdapter(mDataset);
                mItemList.setAdapter(mListItemAdapter);
                mListItemAdapter.notifyDataSetChanged();
            }
        }
    }
}
