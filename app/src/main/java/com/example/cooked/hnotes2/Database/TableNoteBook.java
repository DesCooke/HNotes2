package com.example.cooked.hnotes2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.cooked.hnotes2.MainActivity;

/**
 * Created by cooked on 14/06/2017.
 */

public class TableNoteBook extends TableBase
{
    public TableNoteBook()
    {
    }

    public void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql =
                "DROP TABLE IF EXISTS NoteBook";

        db.execSQL(lSql);
    }

    public void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);

        String lSql =
                "CREATE TABLE NoteBook " +
                        " (" +
                        "   Id INTEGER PRIMARY KEY, " +
                        "   Name TEXT, " +
                        "   ShortDescription TEXT " +
                        ") ";

        db.execSQL(lSql);
    }

    public void addItem(SQLiteOpenHelper helper, RecordNoteBook recordNoteBook)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String lSql =
                "INSERT INTO NoteBook " +
                        "(Id, Name, ShortDescription) " +
                        "VALUES " +
                        "( " + recordNoteBook.getId() + ", " +
                        " '" + recordNoteBook.getName() + "', " +
                        " '" + recordNoteBook.getShortDescription() + "'" +
                        ") ";

        db.execSQL(lSql);

        Toast.makeText(MainActivity.getInstance(), "Notebook " + recordNoteBook.getName() + " added", Toast.LENGTH_SHORT).show();
    }

    public RecordNoteBook getItem(SQLiteOpenHelper helper, int id)
    {
        SQLiteDatabase db = helper.getReadableDatabase();

        String lSql =
                "select Id, Name, ShortDescription " +
                        "FROM NoteBook " +
                        "WHERE Id = " + String.valueOf(id);
        Cursor cursor = db.rawQuery(lSql, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            RecordNoteBook noteBook = new RecordNoteBook(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2));
            return noteBook;
        }
        return null;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public int getNextId(SQLiteOpenHelper helper)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String lSql = "select IFNULL(MAX(Id),0) FROM NoteBook ";
        Cursor cursor = db.rawQuery(lSql, null);

        if (cursor != null)
        {
            if (cursor.getCount()>0) {
                cursor.moveToFirst();
                return (Integer.parseInt(cursor.getString(0)) + 1);
            }
        }

        return(1);
    }

    public RecordNoteBook[] getList(SQLiteOpenHelper helper)
    {
        SQLiteDatabase db = helper.getReadableDatabase();

        String lSql =
                "select Id, Name, ShortDescription " +
                        "FROM NoteBook " +
                        "ORDER BY Name";
        Cursor cursor = db.rawQuery(lSql, null);

        RecordNoteBook[] list;
        int cnt;
        cnt = cursor.getCount();
        list = new RecordNoteBook[cnt];
        cnt=0;
        if (cursor != null && cursor.getCount() > 0)
        {

            cursor.moveToFirst();
            do
            {
                list[cnt] = new RecordNoteBook(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2));
                cnt++;
            } while(cursor.moveToNext());
        }
        return list;
    }

    public void updateItem(SQLiteOpenHelper helper, RecordNoteBook recordNoteBook)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String lSql =
                "UPDATE NoteBook " +
                        "SET Name = '" + recordNoteBook.getName() + "', " +
                        "    ShortDescription = '" + recordNoteBook.getShortDescription() + "' " +
                        "WHERE Id = " + recordNoteBook.getId();

        db.execSQL(lSql);
    }

    public void deleteItem(SQLiteOpenHelper helper, RecordNoteBook recordNoteBook)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String lSql =
                "DELETE FROM NoteBook " +
                        "WHERE Id = " + recordNoteBook.getId();

        db.execSQL(lSql);
    }
    public void deleteAll(SQLiteOpenHelper helper)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String lSql = "DELETE FROM NoteBook";

        db.execSQL(lSql);
    }
}
