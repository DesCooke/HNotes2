package com.example.cooked.hnotes2.UI;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.helper.DragItemTouchHelper;  // replace with your own package path
import com.example.cooked.hnotes2.Database.RecordListItem;
import com.example.cooked.hnotes2.R;

import java.util.ArrayList;
import java.util.Collections;

public class ListItemAdapter
        extends RecyclerView.Adapter<ListItemAdapter.ViewHolder>
        implements DragItemTouchHelper.MoveHelperAdapter
{
    private ArrayList<RecordListItem> mDataset;
    public OnItemClickListener mOnItemClickListener;
    public OnStartDragListener mDragStartListener = null;

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }
    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordListItem obj);
    }

    public void setDragListener(OnStartDragListener dragStartListener) {
        try
        {
            this.mDragStartListener = dragStartListener;
        } catch (Exception e)
        {
        }
    }
    public void setOnItemClickListener(final ListItemAdapter.OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements DragItemTouchHelper.TouchViewHolder
    {
        // each data item is just a string in this case
        public TextView mItemSummary;
        public LinearLayout mCellFull;
        public ImageView mAcMove;

        @Override
        public void onItemSelected()
        {
        }

        @Override
        public void onItemClear()
        {
        }
        public ViewHolder(View v)
        {
            super(v);
            mItemSummary = (TextView) v.findViewById(R.id.cellItemSummary);
            mCellFull = v.findViewById(R.id.cell_full);
            mAcMove = v.findViewById(R.id.ac_move);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListItemAdapter(ArrayList<RecordListItem> myDataset)
    {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_listitem, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mItemSummary.setText(mDataset.get(position).itemSummary);

        holder.mCellFull.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onItemClick(view, mDataset.get(position));
                }
            }
        });

        holder.mAcMove.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN && mDragStartListener != null)
                {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition)
    {
        try
        {
            Collections.swap(mDataset, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);

            RecordListItem fromItem = mDataset.get(fromPosition);
            RecordListItem toItem = mDataset.get(toPosition);
            notifyItemChanged(fromPosition, toItem);
            notifyItemChanged(toPosition, fromItem);

            Database.MyDatabase().subListResequence(mDataset);
        } catch (Exception e)
        {
        }

    }

}