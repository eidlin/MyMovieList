package com.example.ronyonatan.mymovielist;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class AddEditActivity extends AppCompatActivity {
    MyMovieSQLHelper MyMovieSQLHelper;
    EditText subjectET;
    EditText bodyET;
    EditText urlET;
    Button saveBTN;
    Button cancelBTN;
    Button showBTN;
    int id;
    ImageView moviePicIV;
    LinearLayout activity_add_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        moviePicIV = (ImageView) findViewById(R.id.moviePicIV);
        showBTN = (Button) findViewById(R.id.showBTN);
        subjectET = (EditText) findViewById(R.id.subjectET);
        bodyET = (EditText) findViewById(R.id.bodyET);
        urlET = (EditText) findViewById(R.id.urlET);
        Intent receivedIntent = getIntent();
        String name = receivedIntent.getStringExtra("name");
        String body = receivedIntent.getStringExtra("body");
        final String url = receivedIntent.getStringExtra("url");
        activity_add_edit=(LinearLayout)findViewById(R.id.activity_add_edit);
        activity_add_edit.setBackgroundResource(R.drawable.film_background);
        id = receivedIntent.getIntExtra("id", -1);
        subjectET.setText(name);
        bodyET.setText(body);
        urlET.setText(url);


        if (isValid(urlET.getText().toString()) && urlET.getText().toString().contains(".jpg")) {
            AddEditActivity.DownLoadImageTask downLoadImageTask = new AddEditActivity.DownLoadImageTask();
            try {
                downLoadImageTask.execute(url);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "not valid url... ", Toast.LENGTH_SHORT).show();
            }

        }

        MyMovieSQLHelper = new MyMovieSQLHelper(this);
        showBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid(urlET.getText().toString()) && urlET.getText().toString().contains(".jpg")) {
                    String imageUrl = urlET.getText().toString();
                    try {
                        DownLoadImageTask downLoadImageTask = new DownLoadImageTask();
                        downLoadImageTask.execute(imageUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(AddEditActivity.this, "not valid url photo..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelBTN = (Button) findViewById(R.id.cancelBTN);
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(AddEditActivity.this, "you canceld", Toast.LENGTH_SHORT).show();
            }
        });

        saveBTN = (Button) findViewById(R.id.saveBTN);
        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = subjectET.getText().toString();
                String body = bodyET.getText().toString();
                String url = urlET.getText().toString();


                if (name.isEmpty()) {

                    Toast.makeText(AddEditActivity.this, "pls enter a name for the movie.. ", Toast.LENGTH_LONG).show();


                } else {


                    name = name.replaceAll("^\\s+", "");

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBConstants.subjectColumn, name);
                    contentValues.put(DBConstants.bodyColum, body);
                    contentValues.put(DBConstants.urlColum, url);
                    if (id == -1) {  /// NOT GOOD -1
                        MyMovieSQLHelper.getWritableDatabase().insert(DBConstants.tableName, null, contentValues);
                    } else {
                        //we got the id from mainActivity
                        MyMovieSQLHelper.getWritableDatabase().update(DBConstants.tableName, contentValues, "_id=?", new String[]{"" + id});  /// SAME THING
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    //  finish();

                }
            }
        });
    }

    private boolean isValid(String url) {
        boolean valid = URLUtil.isValidUrl(url);
        return valid;
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {

        //private boolean failed = false;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            //dgotlieb8@gmail.com

            try {
                dialog = new ProgressDialog(AddEditActivity.this);
                dialog.setTitle("uploading");
                dialog.setMessage("please wait .... ");
                dialog.setCancelable(true);
                dialog.show();
            } catch (Exception t) {
                t.printStackTrace();
            }


        }

        @Override
        protected void onPostExecute(Bitmap downLoadedImage) {

            if (downLoadedImage == null)
                return;

            moviePicIV.setImageBitmap(downLoadedImage);

            dialog.dismiss();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;
            URL url = null;
            try {
                url = new URL(params[0]);
            } catch (Exception e) {
                e.printStackTrace();

                // Toast.makeText(AddEditActivity.this, "not valid url", Toast.LENGTH_SHORT).show();
                return null;
            }

            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddEditActivity.this, "bad connection", Toast.LENGTH_SHORT).show();
                return null;
            }
            InputStream in = null;
            try {
                in = (InputStream) url.getContent();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddEditActivity.this, "bad getContent", Toast.LENGTH_SHORT).show();
                return null;
            }
            try {
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(AddEditActivity.this, "bad decode", Toast.LENGTH_SHORT).show();
                return null;
            }
            if (connection != null) {
                connection.disconnect();
            }
            return bitmap;


        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.deleteAll) {
            MyMovieSQLHelper.getReadableDatabase().query(DBConstants.tableName, null, null, null, null, null, null);


        }


        return true;
    }


}


