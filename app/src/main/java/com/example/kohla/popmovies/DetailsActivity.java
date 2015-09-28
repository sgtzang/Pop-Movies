package com.example.kohla.popmovies;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by kohla on 2015-08-24.
 */
public class DetailsActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView releaseDateView;
    private TextView overViewView;
    private TextView voteAverageView;
    private ImageView imageView;

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

        titleTextView.setText(Html.fromHtml(title));
        releaseDateView.setText(Html.fromHtml(releaseDate));
        overViewView.setText(Html.fromHtml(overView));
        voteAverageView.setText(Html.fromHtml(voteAverage));
        Picasso.with(this).load(image).into(imageView);
    }
}
