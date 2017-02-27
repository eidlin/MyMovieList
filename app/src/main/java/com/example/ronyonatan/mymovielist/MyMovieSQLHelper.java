package com.example.ronyonatan.mymovielist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ronYonatan on 2/12/2017.
 */

public class MyMovieSQLHelper extends SQLiteOpenHelper {
    Context context;
    public MyMovieSQLHelper(Context context) {
        super(context, "movies.db", null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQLCreate="CREATE TABLE "+DBConstants.tableName +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+DBConstants.subjectColumn +" TEXT,  "+DBConstants.bodyColum+" TEXT, "+DBConstants.urlColum+" TEXT )";
        Log.d("sql", SQLCreate);

        sqLiteDatabase.execSQL(SQLCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void removeRecord(String positionInString) {

        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.delete(DBConstants.tableName, "_id = ?", new String[] { positionInString });
            Log.d("sql", "Deleted Record in position " + positionInString);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }

    }
}
