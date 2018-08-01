package com.example.cooked.hnotes2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cooked on 14/06/2017.
 */

public class TablePage extends TableBase
{
    public TablePage()
    {
    }

    public void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql =
                "DROP TABLE IF EXISTS Page";

        db.execSQL(lSql);
    }

    public void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);

        String lSql =
                "CREATE TABLE Page " +
                        " (" +
                        "   Id INTEGER PRIMARY KEY, " +
                        "   NoteBookId INTEGER, " +
                        "   PageNo INTEGER, " +
                        "   Content TEXT " +
                        ") ";

        db.execSQL(lSql);
    }

    public void addItem(SQLiteOpenHelper helper, RecordPage recordPage)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String lSql =
                "INSERT INTO Page " +
                        "(Id, NoteBookId, PageId, Content) " +
                        "VALUES " +
                        "( " + recordPage.getId() + ", " +
                        "  " + recordPage.getNoteBookId() + ", " +
                        "  " + recordPage.getPageNo() + ", " +
                        " '" + recordPage.getContent() + "' " +
                        ") ";

        db.execSQL(lSql);
    }

    public RecordPage getItem(SQLiteOpenHelper helper, int id)
    {
        SQLiteDatabase db = helper.getReadableDatabase();

        String lSql =
                "select Id, NoteBookId, PageNo, Content " +
                        "FROM Page " +
                        "WHERE Id = " + String.valueOf(id);
        Cursor cursor = db.rawQuery(lSql, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            RecordPage page =
                    new RecordPage
                            (
                                    Integer.parseInt(cursor.getString(0)),
                                    Integer.parseInt(cursor.getString(1)),
                                    Integer.parseInt(cursor.getString(2)),
                                    cursor.getString(3));
            return page;
        }
        return null;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion == 1 && newVersion==2)
        {
            onCreate(db);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public int getNextId(SQLiteOpenHelper helper)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String lSql = "select IFNULL(MAX(Id),0) FROM Page ";
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

    public int getNextPageNo(SQLiteOpenHelper helper, int noteBookId)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String lSql = "select IFNULL(MAX(PageNo),0) FROM Page WHERE noteBookId = " + noteBookId;
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

    public RecordPage[] getList(SQLiteOpenHelper helper, int noteBookId)
    {
        SQLiteDatabase db = helper.getReadableDatabase();

        String lSql =
                "select Id, NoteBookId, PageNo, Content " +
                        "FROM Page " +
                        "ORDER BY PageId ";
        Cursor cursor = db.rawQuery(lSql, null);

        RecordPage[] list;
        int cnt;
        cnt = cursor.getCount();
        list = new RecordPage[cnt];
        cnt=0;
        if (cursor != null && cursor.getCount() > 0)
        {

            cursor.moveToFirst();
            do
            {
                list[cnt] =
                        new RecordPage
                                (
                                        Integer.parseInt(cursor.getString(0)),
                                        Integer.parseInt(cursor.getString(1)),
                                        Integer.parseInt(cursor.getString(2)),
                                        cursor.getString(3)
                                );
                cnt++;
            } while(cursor.moveToNext());
        }
        return list;
    }

    public void updateItem(SQLiteOpenHelper helper, RecordPage recordPage)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String lSql =
                "UPDATE Page " +
                        "SET Content = '" + recordPage.getContent() + "', " +
                        " PageId = " + recordPage.getPageNo() + " " +
                        "WHERE Id = " + recordPage.getId();

        db.execSQL(lSql);
    }

    public void deleteItem(SQLiteOpenHelper helper, RecordPage recordPage)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String lSql =
                "DELETE FROM Page " +
                        "WHERE Id = " + recordPage.getId();

        db.execSQL(lSql);
    }
    public void deleteAll(SQLiteOpenHelper helper, int noteBookId)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String lSql = "DELETE FROM Page WHERE noteBookId = " + noteBookId;

        db.execSQL(lSql);
    }
}
