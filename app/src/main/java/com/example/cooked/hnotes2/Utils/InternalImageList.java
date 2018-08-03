package com.example.cooked.hnotes2.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.cooked.hnotes2.R;

import java.util.ArrayList;

public class InternalImageList extends AppCompatActivity
{
    public ArrayList<InternalImageItem> internalImageList;
    public InternalImageAdapter internalImageAdapter;
    public boolean gridLayout;
    public RecyclerView recyclerView;
    public boolean allowCellMove;
    public boolean recyclerViewEnabled;
    public boolean allowCellSwipe;

    public void showForm()
    {
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Internal Images");
            actionBar.setSubtitle("please select one");
        }

            internalImageList=ImageUtils.imageUtils().listInternalImages();

            internalImageAdapter=new InternalImageAdapter(this, internalImageList);

            gridLayout=true;

            CreateRecyclerView(R.id.internalImageListView, internalImageAdapter);

            internalImageAdapter.setOnItemClickListener(new InternalImageAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, InternalImageItem obj)
                {
                    Intent resultIntent=new Intent();
                    resultIntent.putExtra("selectedfile", obj.internalImageFilename);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            });

    }

    public void CreateRecyclerView(int pView, RecyclerView.Adapter adapter)
    {
            recyclerView=(RecyclerView) findViewById(pView);
            if(gridLayout == false)
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else
            {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            }
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            recyclerView.setAdapter(adapter);

            itemTouchHelper.attachToRecyclerView(recyclerView);
            recyclerViewEnabled=true;
    }

    public void OnItemMove(int from, int to)
    {
    }

    public void SwapItems(int from, int to)
    {

    }

    public void NotifyItemMoved(int from, int to)
    {

    }

    // handle swipe to delete, and draggable
    ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
    {
        int dragFrom=-1;
        int dragTo=-1;

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
        {
            int fromPosition=viewHolder.getAdapterPosition();
            int toPosition=target.getAdapterPosition();

                if(dragFrom == -1)
                {
                    dragFrom=fromPosition;
                }
                dragTo=toPosition;

                if(fromPosition < toPosition)
                {
                    for(int i=fromPosition; i < toPosition; i++)
                    {
                        SwapItems(i, i + 1);
                    }
                } else
                {
                    for(int i=fromPosition; i > toPosition; i--)
                    {
                        SwapItems(i, i - 1);
                    }
                }
                NotifyItemMoved(fromPosition, toPosition);

            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
        {
            int dragFlags=0;
            int swipeFlags=0;

            if(allowCellMove)
                dragFlags=ItemTouchHelper.UP | ItemTouchHelper.DOWN;

            if(allowCellSwipe)
                swipeFlags=ItemTouchHelper.START | ItemTouchHelper.END;

            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction)
        {
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
        {
            super.clearView(recyclerView, viewHolder);

                if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                {
                    OnItemMove(dragFrom, dragTo);
                }

                dragFrom=dragTo=-1;
        }

    });

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_internalimage_list);

            showForm();
    }

}

