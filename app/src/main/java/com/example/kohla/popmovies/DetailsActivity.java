package com.example.kohla.popmovies;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by kohla on 2015-08-24.
 */
public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private TextView titleTextView;
    private TextView releaseDateView;
    private TextView overViewView;
    private TextView voteAverageView;
    private ImageView imageView;
    private String movieId;
    private String API_KEY = "/videos?api_key=312666f1f4baba38887f90e4f338af17";
    private String FEED_URL = "http://api.themoviedb.org/3/movie/";
    private ArrayList<ListItem> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        String title = getIntent().getStringExtra("title");
        String image = getIntent().getStringExtra("image");
        String overView = getIntent().getStringExtra("overView");
        String releaseDate = getIntent().getStringExtra("releaseDate");
        String voteAverage = getIntent().getStringExtra("voteAverage");

        titleTextView = (TextView) findViewById(R.id.title);
        releaseDateView = (TextView) findViewById(R.id.releaseDate);
        overViewView = (TextView) findViewById(R.id.overView);
        voteAverageView = (TextView) findViewById(R.id.voteAverage);
        imageView = (ImageView) findViewById(R.id.grid_item_image);
        movieId = getIntent().getStringExtra("movieId");

        loadTrailer();

        titleTextView.setText(Html.fromHtml(title));
        releaseDateView.setText(Html.fromHtml(releaseDate));
        overViewView.setText(Html.fromHtml(overView));
        voteAverageView.setText(Html.fromHtml(voteAverage));
        Picasso.with(this).load(image).into(imageView);

    /*    ArrayAdapter<ListItem> listAdapter = new ArrayAdapter<ListItem>(this, android.R.layout.simple_list_item_1,mListData);
        ListView trailerList = (ListView)findViewById(R.id.trailerListView);
        trailerList.setAdapter(listAdapter);        */
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

    private void parseResult(String result) {
        try {

            mListData.clear();

            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");

            ListItem item;

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String trailerName = post.optString("name");
                String youtubeKey = post.optString("key");

                item = new ListItem();
                item.setTrailerName(trailerName);
                item.setYoutubeKey(youtubeKey);

                mListData.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadTrailer() {
        AsyncHttpTask listTrailer = new AsyncHttpTask();
        listTrailer.execute(getTargetUrl());
    }


    private String getTargetUrl(){
        return FEED_URL+movieId+API_KEY;
    }
}
