package com.example.cooked.hnotes2.UI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cooked.hnotes2.Database.RecordNoteBook;
import com.example.cooked.hnotes2.MainActivity;
import com.example.cooked.hnotes2.R;
import com.example.cooked.hnotes2.Utils.MyBitmap;

import static com.example.cooked.hnotes2.Utils.ImageUtils.imageUtils;

public class NoteBookAdapter extends RecyclerView.Adapter<NoteBookAdapter.ViewHolder>
{
    private RecordNoteBook[] mDataset;
    public OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordNoteBook obj);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView mName;
        public ImageView mImageView;

        public ViewHolder(View v)
        {
            super(v);
            mName = (TextView) v.findViewById(R.id.txtBookName);
            mImageView = (ImageView) v.findViewById(R.id.imageView2);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NoteBookAdapter(RecordNoteBook[] myDataset)
    {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NoteBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_book, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordNoteBook c = mDataset[position];
        holder.mName.setText(mDataset[position].getName());

        if(c.cover.length() > 0) {
            MyBitmap myBitmap = new MyBitmap();

            Boolean lRetCode = imageUtils().ScaleBitmapFromFile(c.cover, MainActivity.getInstance().getContentResolver(), myBitmap);
            if (!lRetCode)
                return;

            // assign new bitmap and set scale type
            holder.mImageView.setImageBitmap(myBitmap.Value);
        }

        holder.mImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onItemClick(view, c);
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