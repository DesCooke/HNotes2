package com.example.cooked.hnotes2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.Database.RecordListItem;
import com.example.cooked.hnotes2.Database.RecordNoteBook;
import com.example.cooked.hnotes2.UI.ListItemAdapter;
import com.example.cooked.hnotes2.UI.ParentAdapter;
import android.support.v7.widget.helper.ItemTouchHelper;
import com.example.cooked.hnotes2.helper.DragItemTouchHelper;

import java.util.ArrayList;


public class ListItemActivity extends AppCompatActivity
{
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    public int noteBookId;
    public RecordNoteBook recordNoteBook;
    public Button btnOk;
    public String action;
    public TextInputLayout tilItemSummary;
    public int itemId;
    public RecordListItem recordListItem;
    public FloatingActionButton mFab;
    public int parentItemId;
    public RecyclerView mSubItemList;
    public RecyclerView mParentList;
    public TextView mSubItemsCaption;
    public ArrayList<RecordListItem> mDataset;
    public RecyclerView.LayoutManager mLayoutManager;
    public ListItemAdapter mListItemAdapter;
    public String mParents[];
    public RecyclerView.LayoutManager mParentLayoutManager;
    public ParentAdapter mParentListItemAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        tilItemSummary = findViewById(R.id.tilItemSummary);
        mSubItemList = findViewById(R.id.subItemList);
        mSubItemsCaption = findViewById(R.id.subitemscaption);
        mParentList = findViewById(R.id.parentList);

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), ListItemActivity.class);
                intent.putExtra("ACTION", "add");
                intent.putExtra("NOTEBOOKID", noteBookId);
                intent.putExtra("ITEMID", parentItemId);
                startActivityForResult(intent, getResources().getInteger(R.integer.add_list_item_response));
            }
        });

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
                    item.parentItemId = parentItemId;
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
            parentItemId = extras.getInt("ITEMID", 0);
            recordNoteBook = Database.MyDatabase().getNoteBook(noteBookId);

            if(action.compareTo("add")==0)
            {
                setTitle("List: " + recordNoteBook.getName() + " - Add New Item");
                mSubItemsCaption.setVisibility(View.GONE);
                mFab.setVisibility(View.GONE);
                mSubItemList.setVisibility(View.GONE);
            }
            if(action.compareTo("edit")==0)
            {
                setTitle("List: " + recordNoteBook.getName() + " - Edit Item");
                itemId=extras.getInt("ITEMID", 0);
                recordListItem = Database.MyDatabase().getListItem(itemId);
                tilItemSummary.getEditText().setText(recordListItem.itemSummary);
                mSubItemsCaption.setVisibility(View.VISIBLE);
                mFab.setVisibility(View.VISIBLE);
                mSubItemList.setVisibility(View.VISIBLE);
            }

            mDataset = Database.MyDatabase().getListItems(noteBookId, parentItemId);
            mParents = Database.MyDatabase().getParents(parentItemId);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mSubItemList.setHasFixedSize(true);
            mParentList.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mSubItemList.setLayoutManager(mLayoutManager);

            mParentLayoutManager = new LinearLayoutManager(this);
            mParentList.setLayoutManager(mParentLayoutManager);

            CreateAndAttachAdapter();

        }

    }

    public void CreateAndAttachAdapter()
    {
        // specify an adapter (see also next example)
        mListItemAdapter = new ListItemAdapter(mDataset);
        mSubItemList.setAdapter(mListItemAdapter);

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
        mListItemAdapter.setDragListener(new ListItemAdapter.OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });

        ItemTouchHelper.Callback callback = new DragItemTouchHelper(mListItemAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mSubItemList);

        // specify an adapter (see also next example)
        mParentListItemAdapter = new ParentAdapter(mParents);
        mParentList.setAdapter(mParentListItemAdapter);



/*
        mParentListItemAdapter.setOnItemClickListener(new ParentAdapter.OnItemClickListener()
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

 */
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

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == getResources().getInteger(R.integer.add_list_item_response))
        {
            if(resultCode == RESULT_OK) {
                int itemId = data.getIntExtra("ITEMID",0);
                RecordListItem rec=Database.MyDatabase().getListItem(itemId);
                ArrayList<RecordListItem> list = new ArrayList<RecordListItem>();
                for(int i=0;i<mDataset.size();i++)
                {
                    list.add(mDataset.get(i));
                }
                list.add(rec);
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
                    ArrayList<RecordListItem> list = new ArrayList<RecordListItem>();
                    for (int i = 0; i < mDataset.size(); i++)
                    {
                        if (mDataset.get(i).itemId == itemId)
                        {
                            list.add(rec);
                        } else
                        {
                            list.add(mDataset.get(i));
                        }
                    }
                    mDataset = list;

                    CreateAndAttachAdapter();
                    mListItemAdapter.notifyDataSetChanged();
                }
                if(action.compareTo("delete")==0)
                {
                    ArrayList<RecordListItem> list = new ArrayList<RecordListItem>();
                    int j=0;
                    for (int i = 0; i < mDataset.size(); i++)
                    {
                        if (mDataset.get(i).itemId != itemId)
                        {
                            list.add(mDataset.get(i));
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
        Parcelable listState = mSubItemList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //refreshTitle();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mSubItemList.getLayoutManager().onRestoreInstanceState(listState);
        }
    }


}
