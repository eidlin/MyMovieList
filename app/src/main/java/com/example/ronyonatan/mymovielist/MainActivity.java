package com.example.ronyonatan.mymovielist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LinearLayout mainActivityLL ;

    ListView moviesLV;
    ImageButton addBTN;
    int currentPosition = 0;
    MyMovieSQLHelper MyMovieSQLHelper;
    Context context;
    Cursor cursor;
    SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addBTN = (ImageButton) findViewById(R.id.addBTN);

        moviesLV = (ListView) findViewById(R.id.moviesLV);
        MyMovieSQLHelper = new MyMovieSQLHelper(MainActivity.this);
        mainActivityLL=(LinearLayout)findViewById(R.id.mainActivityLL);
        mainActivityLL.setBackgroundResource(R.drawable.moviebackground);
        cursor = MyMovieSQLHelper.getReadableDatabase().query(DBConstants.tableName, null, null, null, null, null, null);
        final String[] fromColums = new String[]{DBConstants.subjectColumn};//, DBConstants.bodyColum , DBConstants.urlColum};
        int[] toTV1 = new int[]{R.id.movieNameTV};
        adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.activity_custm_layout, cursor, fromColums, toTV1);
        moviesLV.setAdapter(adapter);
        registerForContextMenu(moviesLV);

        //  this.adapter.notifyDataSetChanged();
        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("hi");
                builder.setMessage("Whice way you want to add movie ? ");

                builder.setPositiveButton("manually", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //  Do something but close the dialog and open the AddEditActivity
                        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                        startActivity(intent);

                        MainActivity.this.adapter.notifyDataSetChanged();


                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("INTERNET", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // open the intenetActivty
                        Intent intent = new Intent(MainActivity.this, InternetActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor = MyMovieSQLHelper.getReadableDatabase().query(DBConstants.tableName, null, null, null, null, null, null);
        MainActivity.this.adapter.notifyDataSetChanged();
        //cursor.requery();
        adapter.swapCursor(cursor);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        currentPosition = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        getMenuInflater().inflate(R.menu.menu_onitem, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.deleteAll) {






            Toast.makeText(context, " all deleted !  ", Toast.LENGTH_SHORT).show();



        } else if (item.getItemId() == R.id.exitItem) {

            finish();

        }


        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.editItem) {

            cursor.moveToPosition(currentPosition);
            String movieName = cursor.getString(cursor.getColumnIndex(DBConstants.subjectColumn));
            String movieBody = cursor.getString(cursor.getColumnIndex(DBConstants.bodyColum));
            String movieURL = cursor.getString(cursor.getColumnIndex(DBConstants.urlColum));
            int movieId = cursor.getInt(cursor.getColumnIndex(DBConstants.idColumn));

            Intent intent = new Intent(this, AddEditActivity.class);
            intent.putExtra("name", movieName);
            intent.putExtra("body", movieBody);
            intent.putExtra("url", movieURL);
            intent.putExtra("id", movieId);


            startActivity(intent);


        } else if (item.getItemId() == R.id.deleteItem) {

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            currentPosition = (int) info.id;

            MyMovieSQLHelper.removeRecord(String.valueOf(currentPosition));
        //    adapter.remove(adapter.getItem(currentPosition));
         //   ((BaseAdapter)adapter).notifyDataSetChanged();
           // notifyDataSetChanged();
            // this is in the end, when you want to refresh the LV
            this.adapter.notifyDataSetChanged();
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            MainActivity.this.adapter.notifyDataSetChanged();
            adapter.swapCursor(cursor);

        }

        return true;
    }


}