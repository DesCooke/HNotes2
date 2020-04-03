package com.example.cooked.hnotes2.Database;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// here we can have lists within lists - here is what it looks like
// in this example - our noteBookId is 100
//   First List, First Item
//     itemId: 1    (next itemId in ListItem table)
//     noteBookId: 100
//     parentItemId: 0    (cos it is the first list)
//     itemNumber: 1 (first item in this list)
//     itemSeqNo: 1 (first item in the list)
//     itemSummary: "My First Item"
//
//   First List, Second Item
//     itemId: 2    (next itemId in ListItem table)
//     noteBookId: 100
//     parentItemId: 0    (cos it is the first list)
//     itemNumber: 2 (second item in this list)
//     itemSeqNo: 2 (second item in the list)
//     itemSummary: "My Second Item"
//
//   Second item in first list has a sub-list, this is the first item of that list
//     itemId: 3    (next itemId in ListItem table)
//     noteBookId: 100
//     parentItemId: 2    (cos it is a sub-list of itemId 2)
//     itemNumber: 1 (first item in this list)
//     itemSeqNo: 1 (first item in the list)
//     itemSummary: "My First Sublist item Item"
//
//   Second item in first list has a sub-list, this is the second item of that list
//     itemId: 4    (next itemId in ListItem table)
//     noteBookId: 100
//     parentItemId: 2    (cos it is a sub-list of itemId 2)
//     itemNumber: 2 (second item in this list)
//     itemSeqNo: 2 (second item in the list)
//     itemSummary: "My Second Sublist Item"

public class RecordListItem {

    public int itemId;
    public int noteBookId;
    public int parentItemId;
    public int itemNumber;
    public int itemSeqNo;
    public String itemSummary;

    public RecordListItem()
    {
        noteBookId = 0;
        parentItemId = 0;
        itemId = 0;
        itemNumber = 0;
        itemSeqNo = 0;
        itemSummary = "";
    }
}
