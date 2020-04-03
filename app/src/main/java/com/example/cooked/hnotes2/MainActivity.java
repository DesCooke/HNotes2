package com.example.cooked.hnotes2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.cooked.hnotes2.Database.Database;
import com.example.cooked.hnotes2.Database.RecordNoteBook;
import com.example.cooked.hnotes2.Database.RecordPage;
import com.example.cooked.hnotes2.UI.NoteBookAdapter;
import com.example.cooked.hnotes2.Utils.MyPermission;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static MainActivity instance;
    private static Database database;
    private RecordNoteBook[] mDataset;
    private RecyclerView mNoteBookList;
    private LinearLayoutManager mLayoutManager;
    private NoteBookAdapter mNoteBookAdapter;
    public int listAsInt;
    public int notebookAsInt;

    public static MainActivity getInstance()
    {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        if (!MyPermission.checkIfAlreadyhavePermission(this))
            MyPermission.requestForSpecificPermission(this);
        String homeDirectory = getString(R.string.home_directory);
        File dir = new File(homeDirectory);
        if (!dir.exists())
            dir.mkdir();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listAsInt=getResources().getInteger(R.integer.list);
        notebookAsInt=getResources().getInteger(R.integer.notebook);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), NoteBookActivity.class);
                intent.putExtra("ACTION", "add");
                intent.putExtra("NOTEBOOKID", -1);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        database = Database.MyDatabase();

        showForm();
    }

    public void showForm()
    {
        mDataset = database.getNoteBookList();

        mNoteBookList = (RecyclerView) findViewById(R.id.lstNoteBooks);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mNoteBookList.setHasFixedSize(true);

        // use a linear layout manager
        //mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);

        mNoteBookList.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mNoteBookAdapter = new NoteBookAdapter(this, mDataset);
        mNoteBookList.setAdapter(mNoteBookAdapter);


        mNoteBookAdapter.setOnItemClickListener(new NoteBookAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordNoteBook obj)
            {
                String lAction="view";
                RecordNoteBook rec=Database.MyDatabase().getNoteBook(obj.getId());
                if(rec.BookType==notebookAsInt)
                {
                    PageActivity.editMode = false;
                    if (rec.PageCount == 0)
                    {
                        RecordPage recp = new RecordPage();
                        recp.setNoteBookId(obj.getId());
                        recp.setId(Database.MyDatabase().getNextPageId());
                        recp.setPageNo(Database.MyDatabase().getNextPageNo(obj.getId()));
                        recp.setContent("");
                        Database.MyDatabase().addPage(recp);
                        lAction = "edit";
                        PageActivity.editMode = true;
                    }
                    Intent intent = new Intent(getApplicationContext(), PageActivity.class);
                    intent.putExtra("ACTION", lAction);
                    intent.putExtra("NOTEBOOKID", obj.getId());
                    startActivity(intent);
                }
                if(rec.BookType==listAsInt)
                {
                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                    intent.putExtra("NOTEBOOKID", obj.getId());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        showForm();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }
}
