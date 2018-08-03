package com.example.cooked.hnotes2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.Database.RecordNoteBook;
import com.example.cooked.hnotes2.Database.RecordPage;
import com.example.cooked.hnotes2.UI.PageAdapter;

public class PageActivity extends AppCompatActivity {

    public static boolean editMode;
    public static int currPageIndex;
    public static int lastPageIndex;
    public int noteBookId;
    public String action;
    public RecordNoteBook recordNoteBook;
    public RecordPage recordPage;
    public RecordPage[] recordPageList;
    public int currentPage;
    public int lastPage;
    public ViewPager viewPager;
    public ImageView iconEdit;
    public int nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPage=0;
        currPageIndex=0;
        lastPageIndex=0;
        lastPage=-1;
        setContentView(R.layout.activity_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nextPage = -1;
        noteBookId = 0;
        action = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            action = extras.getString("ACTION", "");
            noteBookId = extras.getInt("NOTEBOOKID", 0);
            refreshBook();
        }
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        savePage();
    }

    public View getCurrentView()
    {
        for(int i=0;i<2;i++)
        {
            View myView = viewPager.getChildAt(i);
            TextView txtPageIndex = myView.findViewById(R.id.txtPageIndex);
            int pageIndex = Integer.parseInt(txtPageIndex.getText().toString());
            if(pageIndex==currPageIndex)
                return(myView);
        }
        return(null);
    }

    public View getPreviousView()
    {
        for(int i=0;i<2;i++)
        {
            View myView = viewPager.getChildAt(i);
            TextView txtPageIndex = myView.findViewById(R.id.txtPageIndex);
            int pageIndex = Integer.parseInt(txtPageIndex.getText().toString());
            if(pageIndex==lastPageIndex)
                return(myView);
        }
        return(null);
    }

    public void refreshBook()
    {
        int lPage;
        recordNoteBook = Database.MyDatabase().getNoteBook(noteBookId);
        recordPageList = Database.MyDatabase().getPageList(noteBookId);
        setTitle(recordNoteBook.getName() + ", 1 of " + recordNoteBook.PageCount);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        iconEdit = (ImageView) findViewById(R.id.iconEdit);
        iconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PageActivity.editMode)
                {
                    View lview = getCurrentView();
                    if(lview!=null) {
                        EditText edtPageText = lview.findViewById(R.id.edtPageText);
                        TextView txtPageIndex = lview.findViewById(R.id.txtPageIndex);
                        TextView txtPageText = lview.findViewById(R.id.txtPageText);
                        int pageIndex = Integer.parseInt(txtPageIndex.getText().toString());
                        String lText = edtPageText.getText().toString();
                        recordPageList[pageIndex].setContent(lText);
                        Database.MyDatabase().updatePage(recordPageList[pageIndex]);
                        txtPageText.setText(lText);
                    }
                }
                PageActivity.editMode = !PageActivity.editMode;
                refreshBook();
            }
        });
        lPage = viewPager.getCurrentItem();
        viewPager.setAdapter(new PageAdapter(this, noteBookId, recordPageList));
        if(nextPage != -1)
        {
            viewPager.setCurrentItem(nextPage);
            nextPage = -1;
        }
        else {
            viewPager.setCurrentItem(lPage);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                PageActivity.lastPageIndex = PageActivity.currPageIndex;
                lastPage=currentPage;
                PageActivity.currPageIndex = position;

                setTitle(recordNoteBook.getName() + ", " + (position+1) + " of " + recordNoteBook.PageCount);

                SavePreviousPage();

                currentPage=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void SavePreviousPage()
    {
        View lview = getPreviousView();
        if(lview!=null)
        {
            EditText edtPageText = lview.findViewById(R.id.edtPageText);
            TextView txtPageIndex = lview.findViewById(R.id.txtPageIndex);
            TextView txtPageText = lview.findViewById(R.id.txtPageText);
            int pageIndex = Integer.parseInt(txtPageIndex.getText().toString());
            recordPageList[pageIndex].setContent(edtPageText.getText().toString());
            txtPageText.setText(edtPageText.getText().toString());
            Database.MyDatabase().updatePage(recordPageList[pageIndex]);
        }
    }

    public void SaveCurrentPage()
    {
        View lview = getCurrentView();
        if(lview!=null)
        {
            EditText edtPageText = lview.findViewById(R.id.edtPageText);
            TextView txtPageIndex = lview.findViewById(R.id.txtPageIndex);
            TextView txtPageText = lview.findViewById(R.id.txtPageText);
            int pageIndex = Integer.parseInt(txtPageIndex.getText().toString());
            recordPageList[pageIndex].setContent(edtPageText.getText().toString());
            txtPageText.setText(edtPageText.getText().toString());
            Database.MyDatabase().updatePage(recordPageList[pageIndex]);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        savePage();
    }
    public void savePage()
    {
        for(int i=0;i<recordPageList.length;i++) {
            View lview = viewPager.getChildAt(i);
            if (lview != null) {
                EditText edtPageText = lview.findViewById(R.id.edtPageText);
                TextView txtPageIndex = lview.findViewById(R.id.txtPageIndex);
                int pageIndex = Integer.parseInt(txtPageIndex.getText().toString());
                recordPageList[pageIndex].setContent(edtPageText.getText().toString());
                Database.MyDatabase().updatePage(recordPageList[pageIndex]);
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        refreshBook();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SaveCurrentPage();
        switch (item.getItemId()) {
            case R.id.mnuAddPageToEnd:
                RecordPage recp = new RecordPage();
                recp.setNoteBookId(noteBookId);
                recp.setId(Database.MyDatabase().getNextPageId());
                recp.setPageNo(Database.MyDatabase().getNextPageNo(noteBookId));
                recp.setContent("<empty>");
                Database.MyDatabase().addPage(recp);
                PageActivity.editMode = true;
                nextPage = Database.MyDatabase().getPageCount(noteBookId)-1;
                refreshBook();

                break;
            case R.id.mnuAddPageAfter:
                RecordPage currPage2 = recordPageList[PageActivity.currPageIndex];
                RecordPage recp2 = new RecordPage();
                recp2.setNoteBookId(noteBookId);
                recp2.setId(Database.MyDatabase().getNextPageId());
                recp2.setPageNo(Database.MyDatabase().getNextPageNo(noteBookId));
                recp2.setContent("<empty>");
                Database.MyDatabase().addAfterPage(currPage2, recp2);
                PageActivity.editMode = true;
                nextPage = PageActivity.currPageIndex+1;
                refreshBook();
                break;
            case R.id.mnuAddPageBefore:
                RecordPage currPage3 = recordPageList[PageActivity.currPageIndex];
                RecordPage recp3 = new RecordPage();
                recp3.setNoteBookId(noteBookId);
                recp3.setId(Database.MyDatabase().getNextPageId());
                recp3.setPageNo(Database.MyDatabase().getNextPageNo(noteBookId));
                recp3.setContent("<empty>");
                Database.MyDatabase().addBeforePage(currPage3, recp3);
                PageActivity.editMode = true;
                nextPage = PageActivity.currPageIndex;
                refreshBook();
                break;
            case R.id.mnuDeletePage:
                Database.MyDatabase().deletePage(recordPageList[PageActivity.currPageIndex]);
                refreshBook();

                break;
            case R.id.mnuEditBook:
                Intent intent = new Intent(getApplicationContext(), NoteBookActivity.class);
                intent.putExtra("ACTION", "modify");
                intent.putExtra("NOTEBOOKID", noteBookId);
                startActivity(intent);
                break;
            case R.id.mnuDeleteBook:
                Database.MyDatabase().deleteNoteBook(recordNoteBook);
                finish();
                break;
            default:
                break;
        }

        return true;
    }

}
