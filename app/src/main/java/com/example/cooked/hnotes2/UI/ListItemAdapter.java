package com.example.cooked.hnotes2.UI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cooked.hnotes2.Database.RecordListItem;
import com.example.cooked.hnotes2.R;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder>
{
    private RecordListItem[] mDataset;
    public OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordListItem obj);
    }

    public void setOnItemClickListener(final ListItemAdapter.OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView mItemSummary;
        public LinearLayout mCellFull;

        public ViewHolder(View v)
        {
            super(v);
            mItemSummary = (TextView) v.findViewById(R.id.cellItemSummary);
            mCellFull = v.findViewById(R.id.cell_full);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListItemAdapter(RecordListItem[] myDataset)
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
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mItemSummary.setText(mDataset[position].itemSummary);

        holder.mCellFull.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onItemClick(view, mDataset[position]);
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.length;
    }
}