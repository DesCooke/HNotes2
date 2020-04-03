package com.example.cooked.hnotes2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cooked on 14/06/2017.
 */

public class TableListItem extends TableBase
{
    public TableListItem()
    {
    }

    public void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql =
                "DROP TABLE IF EXISTS ListItem";

        db.execSQL(lSql);
    }

    public void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);

        String lSql =
                "CREATE TABLE ListItem " +
                        " (" +
                        "   ItemId INTEGER PRIMARY KEY, " +
                        "   NoteBookId INTEGER, " +
                        "   ParentItemId INTEGER, " +
                        "   ItemNumber INTEGER, " +
                        "   ItemSeqNo INTEGER, " +
                        "   ItemSummary TEXT " +
                        ") ";

        db.execSQL(lSql);
    }

    public void addItem(SQLiteOpenHelper helper, RecordListItem item)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String lOrigContent=item.itemSummary;

        String lNewContent = HandleSingleQuotes(lOrigContent);

        item.itemId = getNextItemId(helper);
        item.itemNumber = getNextItemNumber(helper, item.noteBookId, item.parentItemId);
        item.itemSeqNo = getNextItemSeqNo(helper, item.noteBookId, item.parentItemId);

        String lSql =
                "INSERT INTO ListItem " +
                        "(ItemId, NoteBookId, ParentItemId, ItemNumber, ItemSeqNo, ItemSummary) " +
                        "VALUES " +
                        "( " + item.itemId + ", " +
                        "  " + item.noteBookId + ", " +
                        "  " + item.parentItemId + ", " +
                        "  " + item.itemNumber + ", " +
                        "  " + item.itemSeqNo + ", " +
                        "  '" + lNewContent + "' " +
                        ") ";

        db.execSQL(lSql);
    }

    public RecordListItem getItem(SQLiteOpenHelper helper, int id)
    {
        SQLiteDatabase db = helper.getReadableDatabase();

        String lSql =
                "select ItemId, NoteBookId, ParentItemId, ItemNumber, ItemSeqNo, ItemSummary " +
                        "FROM ListItem " +
                        "WHERE ItemId = " + String.valueOf(id);
        Cursor cursor = db.rawQuery(lSql, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            RecordListItem listItem = new RecordListItem();
            listItem.itemId = Integer.parseInt(cursor.getString(0));
            listItem.noteBookId = Integer.parseInt(cursor.getString(1));
            listItem.parentItemId = Integer.parseInt(cursor.getString(2));
            listItem.itemNumber = Integer.parseInt(cursor.getString(3));
            listItem.itemSeqNo = Integer.parseInt(cursor.getString(4));
            listItem.itemSummary = cursor.getString(5);
            return listItem;
        }
        return null;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion == 7 && newVersion==8)
        {
            onCreate(db);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public int getNextItemId(SQLiteOpenHelper helper)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String lSql = "select IFNULL(MAX(itemId),0) FROM ListItem ";
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

    public int getNextItemNumber(SQLiteOpenHelper helper, int noteBookId, int parentItemId)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String lSql = "select IFNULL(MAX(ItemNumber),0) FROM ListItem " +
                "WHERE noteBookId = " + noteBookId + " " +
                "AND parentItemId = " + parentItemId;
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

    public int getNextItemSeqNo(SQLiteOpenHelper helper, int noteBookId, int parentItemId)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String lSql = "select IFNULL(MAX(ItemSeqNo),0) FROM ListItem " +
                "WHERE noteBookId = " + noteBookId + " " +
                "AND parentItemId = " + parentItemId;
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

    public RecordListItem[] getList(SQLiteOpenHelper helper, int noteBookId, int parentItemId)
    {
        SQLiteDatabase db = helper.getReadableDatabase();

        String lSql =
                "select ItemId, NoteBookId, ParentItemId, ItemNumber, ItemSeqNo, ItemSummary " +
                        "FROM ListItem " +
                        "WHERE NoteBookId = " + noteBookId + " " +
                        "AND parentItemId = " + parentItemId + " " +
                        "ORDER BY ItemSeqNo ";
        Cursor cursor = db.rawQuery(lSql, null);

        RecordListItem[] list;
        int cnt;
        cnt = cursor.getCount();
        list = new RecordListItem[cnt];
        cnt=0;
        if (cursor != null && cursor.getCount() > 0)
        {

            cursor.moveToFirst();
            do
            {
                list[cnt] = new RecordListItem();
                list[cnt].itemId = Integer.parseInt(cursor.getString(0));
                list[cnt].noteBookId = Integer.parseInt(cursor.getString(1));
                list[cnt].parentItemId = Integer.parseInt(cursor.getString(2));
                list[cnt].itemNumber = Integer.parseInt(cursor.getString(3));
                list[cnt].itemSeqNo = Integer.parseInt(cursor.getString(4));
                list[cnt].itemSummary = cursor.getString(5);

                cnt++;
            } while(cursor.moveToNext());
        }
        return list;
    }

    public void updateItem(SQLiteOpenHelper helper, RecordListItem item)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String lOrigContent=item.itemSummary;
        String lNewContent=HandleSingleQuotes(lOrigContent);

        String lSql =
                "UPDATE ListItem " +
                        "SET ItemSummary = '" + lNewContent + "', " +
                        " ItemSeqNo = " + item.itemSeqNo + " " +
                        "WHERE ItemId = " + item.itemId;

        db.execSQL(lSql);
    }

    public void deleteItem(SQLiteOpenHelper helper, RecordListItem item)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String lSql =
                "DELETE FROM ListItem " +
                "WHERE ItemId = " + item.itemId;

        db.execSQL(lSql);
    }

    public void deleteAll(SQLiteOpenHelper helper, int noteBookId)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String lSql = "DELETE FROM ListItem WHERE noteBookId = " + noteBookId;

        db.execSQL(lSql);
    }
    public void deleteAllForSubList(SQLiteOpenHelper helper, int noteBookId, int parentItemId)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String lSql = "DELETE FROM ListItem " +
                "WHERE noteBookId = " + noteBookId + " " +
                "AND parentItemId = " + parentItemId;
        db.execSQL(lSql);
    }

}
