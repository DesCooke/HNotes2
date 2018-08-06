package com.example.cooked.hnotes2.UI;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.Database.RecordPage;
import com.example.cooked.hnotes2.PageActivity;
import com.example.cooked.hnotes2.R;

public class PageAdapter extends PagerAdapter {

    private Context mContext;
    public int noteBookId;
    public TextView txtPageIndex;
    public RecordPage recordPageList[];
    public EditText edtPageText;
    public TextView txtPageText;

    public PageAdapter(Context context, int noteBookId, RecordPage[] recArray) {
        mContext = context;
        this.noteBookId = noteBookId;
        recordPageList = recArray;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        RecordPage rec = recordPageList[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.cell_page, collection, false);
        edtPageText = layout.findViewById(R.id.edtPageText);
        edtPageText.setText(recordPageList[position].getContent());
        txtPageText = layout.findViewById(R.id.txtPageText);
        txtPageText.setText(recordPageList[position].getContent());
        txtPageText.setMovementMethod(new ScrollingMovementMethod());
        txtPageIndex = layout.findViewById(R.id.txtPageIndex);
        txtPageIndex.setText(String.valueOf(position));

        if(PageActivity.editMode)
        {
            txtPageText.setVisibility(View.INVISIBLE);
            edtPageText.setVisibility(View.VISIBLE);
        }
        else
        {
            txtPageText.setVisibility(View.VISIBLE);
            edtPageText.setVisibility(View.INVISIBLE);
        }
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return recordPageList.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        RecordPage rec = recordPageList[position];
        return("Page " + position+1 + " of " + recordPageList.length);
    }


}