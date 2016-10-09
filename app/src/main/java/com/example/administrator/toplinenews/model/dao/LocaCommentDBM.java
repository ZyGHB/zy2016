package com.example.administrator.toplinenews.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.administrator.toplinenews.model.entity.Comments;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/29 0029.
 */
public class LocaCommentDBM {
    private static final String TAG = "LocaCommentDBM";

    /**
     * 插入新闻
     *
     * @param sqLiteOP
     * @param comments
     */
    public static long insert(LocalCommentSQL sqLiteOP, Comments comments) {
        ContentValues values = new ContentValues();
        values.put(NewsContract.LocalCommentEntry.COLUMN_NAME_ENTRY_ID, comments.getCid());
        values.put(NewsContract.LocalCommentEntry.COLUMN_NAME_CONTENT, comments.getContent());
        values.put(NewsContract.LocalCommentEntry.COLUMN_NAME_PORTRAIT, comments.getPortrait());
        values.put(NewsContract.LocalCommentEntry.COLUMN_NAME_STAMP, comments.getStamp());
        values.put(NewsContract.LocalCommentEntry.COLUMN_NAME_UID, comments.getUid());
        long count = sqLiteOP.getWritableDatabase().insert(NewsContract.LocalCommentEntry.TABLE_NAME, null, values);
        return count;
    }

   /* public static boolean isExistNews(LocalNewsSQLiteOP sqLiteOP, int nid) {
        String selection = NewsContract.LocalNewsEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";

        String[] selectionArg = new String[]{nid + ""};
        Cursor cursor = sqLiteOP.getWritableDatabase().query(
                NewsContract.LocalNewsEntry.TABLE_NAME,
                new String[]{
                        NewsContract.LocalNewsEntry.COLUMN_NAME_ENTRY_ID
                },//返回那些列
                selection,
                selectionArg,
                null,
                null,
                null
        );
        return cursor.getCount() > 0;
    }*/

    /**
     * 查询所有的已收藏的新闻数据
     * @param sqLiteOP
     * @return
     */
    public static ArrayList<Comments> query(LocalCommentSQL sqLiteOP) {
        ArrayList<Comments> data = new ArrayList<>();
        Cursor cursor = sqLiteOP.getWritableDatabase().query(
                NewsContract.LocalNewsEntry.TABLE_NAME,
                null,//返回所有的列
                null,
                null,
                null,
                null,
                null
        );
        int count = cursor.getCount();
        Log.d(TAG, "count: " + count);
        while (cursor.moveToNext()) {
            Comments n = new Comments();
            String icon = cursor.getString(
                    cursor.getColumnIndex(NewsContract.LocalCommentEntry.COLUMN_NAME_PORTRAIT));
            String content = cursor.getString(
                    cursor.getColumnIndex(NewsContract.LocalCommentEntry.COLUMN_NAME_CONTENT));
            String stamp = cursor.getString(
                    cursor.getColumnIndex(NewsContract.LocalCommentEntry.COLUMN_NAME_STAMP));
            String nid = cursor.getString(
                    cursor.getColumnIndex(NewsContract.LocalCommentEntry.COLUMN_NAME_UID));
            n.setPortrait(icon);
            n.setContent(content);
            n.setStamp(stamp);
            n.setUid(nid);
            data.add(n);
        }
        return data;
    }
}
