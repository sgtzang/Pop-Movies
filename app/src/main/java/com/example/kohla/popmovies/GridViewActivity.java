package com.example.kohla.popmovies;

/**
 * Created by kohla on 2015-08-24.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.content.Context;

public class GridViewActivity extends ActionBarActivity {
    private static final String TAG = GridViewActivity.class.getSimpleName();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String FEED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=";
    private String SORT_TYPE = ".desc&";
    private String API_KEY = "api_key=312666f1f4baba38887f90e4f338af17";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view); //mod_by_me

        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(GridViewActivity.this, DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());
                intent.putExtra("releaseDate",item.getReleaseDate());
                intent.putExtra("overView",item.getOverView());
                intent.putExtra("voteAverage",item.getVoteAverage());

                //Start details activity
                startActivity(intent);
            }
        });

        //Start download
        mProgressBar.setVisibility(View.VISIBLE);
        updatePoster();

    }

    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(GridViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

        String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    /**
     * Parsing the feed results and get the list
     * @param result
     */
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");

            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("original_title");
                String posterPath = "http://image.tmdb.org/t/p/w185/"+post.optString("poster_path");
                String releaseDate = post.optString("release_date");
                String overView = post.optString("overview");
                String voteAverage = post.optString("vote_average");

                item = new GridItem();

                item.setTitle(title) ;
                item.setImage(posterPath);
                item.setReleaseDate(releaseDate);
                item.setOverView(overView);
                item.setVoteAverage(voteAverage);

                mGridData.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updatePoster() {
        AsyncHttpTask reloadPoster = new AsyncHttpTask();
        reloadPoster.execute(getTargetUrl());
    }


    private String getTargetUrl(){
        return FEED_URL+getUnitsKey()+SORT_TYPE+API_KEY;
    }

    private String getUnitsKey(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String key = prefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_popularity));
        return key;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            updatePoster();
        }
    }

}