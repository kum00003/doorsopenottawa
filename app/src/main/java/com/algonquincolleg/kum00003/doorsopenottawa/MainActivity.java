package com.algonquincolleg.kum00003.doorsopenottawa;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.algonquincolleg.kum00003.doorsopenottawa.Parsers.BuildingJSONParser;

import java.util.ArrayList;
import java.util.List;

import model.Building;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {
    // URL to my RESTful API Service hosted on my Bluemix account.
    public static final String IMAGES_BASE_URL = "https://doors-open-ottawa-hurdleg.mybluemix.net/";
    public static final String REST_URI = "https://doors-open-ottawa-hurdleg.mybluemix.net/buildings";

    private ProgressBar pb;
    private List<MyTask> tasks;

    private List<Building> buildingList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setOnItemClickListener(this);

        if (isOnline()) {
            requestData( REST_URI );
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Building theSelectedBuilding = buildingList.get(position);

        Toast.makeText(MainActivity.this, theSelectedBuilding.getName(), Toast.LENGTH_LONG).show();
        //Starting a new Intent
        Intent nextScreen = new Intent(getApplicationContext(), DetailActivity.class);

        //Sending data to another Activity
        nextScreen.putExtra( "name", theSelectedBuilding.getName());
        nextScreen.putExtra( "address", theSelectedBuilding.getAddress());
        nextScreen.putExtra( "description", theSelectedBuilding.getDescription());
        //nextScreen.putExtra( "hours", theSelectedBuilding.getName());

        startActivity(nextScreen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        return false;
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay() {
        //Use PlanetAdapter to display data
        BuildingAdapter adapter = new BuildingAdapter(this, R.layout.item_buldings, buildingList);
        setListAdapter(adapter);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            buildingList = BuildingJSONParser.parseFeed(content);

            return content;
        }

        @Override
        protected void onPostExecute(String result) {

            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }

            if (result == null) {
                Toast.makeText(MainActivity.this, "Web service not available", Toast.LENGTH_LONG).show();
                return;
            }

            buildingList = BuildingJSONParser.parseFeed(result);
            updateDisplay();
        }

    }
}