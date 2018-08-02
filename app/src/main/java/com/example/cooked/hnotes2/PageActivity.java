package com.example.cooked.hnotes2;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.Database.RecordNoteBook;
import com.example.cooked.hnotes2.Database.RecordPage;
import com.example.cooked.hnotes2.UI.PageAdapter;

public class PageActivity extends AppCompatActivity {

    public int noteBookId;
    public String action;
    public RecordNoteBook recordNoteBook;
    public RecordPage recordPage;
    public RecordPage[] recordPageList;
    public int currentPage;
    public int lastPage;
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPage=0;
        lastPage=-1;
        setContentView(R.layout.activity_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

    public void refreshBook()
    {
        int lPage;
        recordNoteBook = Database.MyDatabase().getNoteBook(noteBookId);
        recordPageList = Database.MyDatabase().getPageList(noteBookId);
        setTitle(recordNoteBook.getName() + ", 1 of " + recordNoteBook.PageCount);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        lPage = viewPager.getCurrentItem();
        viewPager.setAdapter(new PageAdapter(this, noteBookId, recordPageList));
        viewPager.setCurrentItem(lPage);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                lastPage=currentPage;

                setTitle(recordNoteBook.getName() + ", " + (position+1) + " of " + recordNoteBook.PageCount);

                View lview = viewPager.getChildAt(lastPage);
                if(lview!=null)
                {
                    EditText edtPageText = lview.findViewById(R.id.edtPageText);
                    TextView txtPageIndex = lview.findViewById(R.id.txtPageIndex);
                    int pageIndex = Integer.parseInt(txtPageIndex.getText().toString());
                    recordPageList[pageIndex].setContent(edtPageText.getText().toString());
                    Database.MyDatabase().updatePage(recordPageList[pageIndex]);
                }

                currentPage=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuAddPageToEnd:
                RecordPage recp = new RecordPage();
                recp.setNoteBookId(noteBookId);
                recp.setId(Database.MyDatabase().getNextPageId());
                recp.setPageNo(Database.MyDatabase().getNextPageNo(noteBookId));
                recp.setContent("<empty>");
                Database.MyDatabase().addPage(recp);
                refreshBook();

                break;
            case R.id.mnuAddPageAfter:
                break;
            case R.id.mnuAddPageBefore:
                break;
            case R.id.mnuDeletePage:
                break;
            default:
                break;
        }

        return true;
    }

}
