package com.example.ronyonatan.mymovielist;

import android.content.Intent;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class InternetActivity extends AppCompatActivity {
    Button searchBTN;
    Button cancelBTN;
    EditText nameET;
    ListView internetLV;
    ArrayList<MovieObject> allmovies;
    ArrayAdapter<MovieObject> adapter;
    int currentPosition = 0;
LinearLayout activity_internet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        activity_internet=(LinearLayout)findViewById(R.id.activity_internet);
        activity_internet.setBackgroundResource(R.drawable.film_background);
        allmovies = new ArrayList();
        adapter = new ArrayAdapter<MovieObject>(this, android.R.layout.simple_list_item_1, allmovies);
        cancelBTN = (Button) findViewById(R.id.cancelBTN);
        internetLV = (ListView) findViewById(R.id.internetLV);
        searchBTN = (Button) findViewById(R.id.searchBTN);
        registerForContextMenu(internetLV);
        internetLV.setAdapter(adapter);
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(InternetActivity.this, "you canceld", Toast.LENGTH_SHORT).show();
            }
        });





        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameET = (EditText) findViewById(R.id.nameET);
                DownloadWebsite downloadWebsite = new DownloadWebsite();
                downloadWebsite.execute("http://omdbapi.com/?s=" + nameET.getText().toString());
                Log.i("YonatanError", "Pressed on Search");

               /* for (int i=0; i<allmovies.size();i++)
                {            MovieObject theName= allmovies.get(i);
                    theName.toString();
                    InternetActivity.this.adapter.notifyDataSetChanged();


                }*/
            }
        });

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        currentPosition = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        getMenuInflater().inflate(R.menu.internet_edit, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.InternetItemEdit)
        {
            nameET = (EditText) findViewById(R.id.nameET);
            DownloadWebsite2 downloadWebsite = new DownloadWebsite2();
            MovieObject imdb=allmovies.get(currentPosition);
            downloadWebsite.execute("http://omdbapi.com/?i="+imdb.imdbID.toString());



//            String movieName=imdb.title;
//            String movieSummary=imdb.plot;
//            String moviePoster=imdb.poster;
//
//
//
//            Intent intent = new Intent(this, AddEditActivity.class);
//            intent.putExtra("internetname", movieName);
//            intent.putExtra("summary", movieSummary);
//            intent.putExtra("poster", moviePoster);
//
//
//            startActivity(intent);




        }



        return true;
    }


    public class DownloadWebsite extends AsyncTask<String, Long, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder response = null;

            try {
                URL website = new URL(params[0]);
                URLConnection connection = website.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()));
                response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
                in.close();

            } catch (Exception ee) {

                ee.printStackTrace();
            }

            if (response != null) {
                Log.i("YonatanError", "found results returnning");
                return response.toString();
            } else {
                {//Toast.makeText(InternetActivity.this, "no internet...", Toast.LENGTH_SHORT).show();
                    Log.e("YonatanError", "null response");
                }

                return "no result";
            }
        }

        @Override
        protected void onPostExecute(String resultJSON) {
            //recpeTV.setText(resultJSON);
            if (resultJSON.equals("no result")) {
                Log.e("YonatanError", "no results from site");
                return;
            }


            try {
                JSONObject mainObject = new JSONObject(resultJSON);

                JSONArray resultsArray = mainObject.getJSONArray("Search");
                Log.d("dsd", "dsds");
                allmovies.clear();

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject currentObject = resultsArray.getJSONObject(i);
                    String title = currentObject.getString("Title");
                    String imdbid = currentObject.getString("imdbID");

                    MovieObject movie = new MovieObject(title, imdbid);
                    allmovies.add(movie);

                }

                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
    }




    public class DownloadWebsite2 extends AsyncTask<String, Long, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder response = null;

            try {
                URL website = new URL(params[0]);
                URLConnection connection = website.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()));
                response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
                in.close();

            } catch (Exception ee) {

                ee.printStackTrace();
            }

            if (response != null) {
                Log.i("YonatanError", "found results returnning");
                return response.toString();
            } else {
                {
                    //Toast.makeText(InternetActivity.this, "no internet...", Toast.LENGTH_SHORT).show();
                    Log.e("YonatanError", "null response");
                }
                return "no result";
            }
        }

        @Override
            protected void onPostExecute(String resultJSON) {
                String title = null;
                String plot = null;
                String  poster = null;

                if (resultJSON.equals("no result")) {
                    Log.e("YonatanError", "no results from site");
                    return;
                }


            try {
                JSONObject mainObject = new JSONObject(resultJSON);

               //JSONObject resultsArray = mainObject.getJSONObject("JSON");
                Log.d("dsd", "dsds");
                allmovies.clear();


                    JSONObject currentObject = mainObject;
                      title = currentObject.getString("Title");
                      plot  = currentObject.getString("Plot");
                      poster= currentObject.getString("Poster");


              //  JSONObject currentObject = resultsArray.getJSONObject(i);
               // String title = currentObject.getString("Title");
               // String imdbid = currentObject.getString("imdbID");

               // MovieObject movie = new MovieObject(title, imdbid);


                    MovieObject movie = new MovieObject(title,plot,poster );
                    allmovies.add(movie);



                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }





            Intent intent = new Intent(getApplicationContext(), AddEditActivity.class);
            intent.putExtra("name", title);
            intent.putExtra("body", plot);
            intent.putExtra("url", poster);


            startActivity(intent);




        }
    }


}
