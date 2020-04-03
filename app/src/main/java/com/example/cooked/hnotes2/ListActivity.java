package com.example.cooked.hnotes2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.Database.RecordListItem;
import com.example.cooked.hnotes2.Database.RecordNoteBook;
import com.example.cooked.hnotes2.Database.RecordPage;
import com.example.cooked.hnotes2.UI.ListItemAdapter;

import java.util.List;

public class ListActivity extends AppCompatActivity
{
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    FloatingActionButton mFabAdd;
    public int noteBookId;
    public RecordNoteBook recordNoteBook;
    public RecordListItem mDataset[];
    public RecyclerView mItemList;
    public RecyclerView.LayoutManager mLayoutManager;
    public ListItemAdapter mListItemAdapter;

    protected void refreshTitle()
    {
        recordNoteBook = Database.MyDatabase().getNoteBook(noteBookId);
        setTitle(recordNoteBook.getName());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            noteBookId = extras.getInt("NOTEBOOKID", 0);
            refreshTitle();
        }

        mDataset = Database.MyDatabase().getListItems(noteBookId, 0);

        mItemList = (RecyclerView) findViewById(R.id.itemList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mItemList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mItemList.setLayoutManager(mLayoutManager);

        CreateAndAttachAdapter();

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

    public void CreateAndAttachAdapter()
    {
        // specify an adapter (see also next example)
        mListItemAdapter = new ListItemAdapter(mDataset);
        mItemList.setAdapter(mListItemAdapter);

        mListItemAdapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordListItem obj)
            {
                Intent intent = new Intent(getApplicationContext(), ListItemActivity.class);
                intent.putExtra("ACTION", "edit");
                intent.putExtra("NOTEBOOKID", obj.noteBookId);
                intent.putExtra("ITEMID", obj.itemId);
                startActivityForResult(intent, getResources().getInteger(R.integer.edit_list_item_response));
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == getResources().getInteger(R.integer.add_list_item_response))
        {
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

                CreateAndAttachAdapter();
                mListItemAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == getResources().getInteger(R.integer.edit_list_item_response))
        {
            if(resultCode == RESULT_OK)
            {
                int itemId = data.getIntExtra("ITEMID",0);
                String action = data.getStringExtra("ACTION");
                if(action.compareTo("edit")==0)
                {
                    RecordListItem rec=Database.MyDatabase().getListItem(itemId);
                    RecordListItem[] list;
                    list = new RecordListItem[mDataset.length];
                    for (int i = 0; i < mDataset.length; i++)
                    {
                        if (mDataset[i].itemId == itemId)
                        {
                            list[i] = rec;
                        } else
                        {
                            list[i] = mDataset[i];
                        }
                    }
                    mDataset = list;

                    CreateAndAttachAdapter();
                    mListItemAdapter.notifyDataSetChanged();
                }
                if(action.compareTo("delete")==0)
                {
                    RecordListItem[] list;
                    list = new RecordListItem[mDataset.length-1];
                    int j=0;
                    for (int i = 0; i < mDataset.length; i++)
                    {
                        if (mDataset[i].itemId != itemId)
                        {
                            list[j] = mDataset[i];
                            j++;
                        }
                    }
                    mDataset = list;

                    CreateAndAttachAdapter();
                    mListItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mItemList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        refreshTitle();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mItemList.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.mnuEditList:
                Intent intent = new Intent(getApplicationContext(), NoteBookActivity.class);
                intent.putExtra("ACTION", "modify");
                intent.putExtra("NOTEBOOKID", noteBookId);
                startActivity(intent);
                break;
            case R.id.mnuDeleteList:
                DialogInterface.OnClickListener dialogClickListener2 =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Database.MyDatabase().deleteNoteBook(recordNoteBook);
                                        finish();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("Really delete this list?").setPositiveButton("Yes", dialogClickListener2)
                        .setNegativeButton("No", dialogClickListener2).show();

                break;
            default:
                break;
        }

        return true;
    }


}
