package tw.raymondsryang.life.activity;

import android.graphics.Point;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import tw.raymondsryang.life.R;
import tw.raymondsryang.life.data.Story;
import tw.raymondsryang.life.utils.Utils;

public class StoryDetailActivity extends AppCompatActivity {

    private Story mStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        if (savedInstanceState==null){
            mStory = (Story) getIntent().getSerializableExtra("story");
        } else {
            mStory = (Story) savedInstanceState.getSerializable("story");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mStory.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        setSupportActionBar(toolbar);

        final ImageView image = (ImageView) findViewById(R.id.image);
        image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                image.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Picasso.with(getApplicationContext())
                        .load(new File(Utils.getStoryImagePath(getApplicationContext(), mStory.getId())))
                        .fit()
                        .centerInside()
                        .into(image);
            }
        });

        TextView content = (TextView) findViewById(R.id.content);
        content.setText(mStory.getContent());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("story", mStory);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
