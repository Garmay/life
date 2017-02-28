package tw.raymondsryang.life.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import tw.raymondsryang.life.R;
import tw.raymondsryang.life.data.Story;
import tw.raymondsryang.life.utils.Utils;

import static tw.raymondsryang.life.R.id.fill;
import static tw.raymondsryang.life.R.id.toolbar;

public class StoryDetailActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_UPDATE_STORY = 0x01;

    private Story mStory;
    private ImageView mImage;
    private TextView mContent;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        if (savedInstanceState==null){
            mStory = (Story) getIntent().getSerializableExtra("story");
        } else {
            mStory = (Story) savedInstanceState.getSerializable("story");
        }

        mToolbar = (Toolbar) findViewById(toolbar);
        mImage = (ImageView) findViewById(R.id.image);
        mContent = (TextView) findViewById(R.id.content);

        mToolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        setSupportActionBar(mToolbar);
        fillDataIntoScreen(mStory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_story_detail, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.edit:
                Intent intent = new Intent(this, EditStoryActivity.class);
                intent.putExtra("mode", EditStoryActivity.MODE_EDIT);
                intent.putExtra("story", mStory);
                startActivityForResult(intent, REQUEST_CODE_UPDATE_STORY);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_UPDATE_STORY && resultCode == Activity.RESULT_OK){
            mStory = (Story) data.getSerializableExtra("story");
            fillDataIntoScreen(mStory);
        }
    }

    private void fillDataIntoScreen(Story story){
        Picasso.with(getApplicationContext())
                .load(new File(Utils.getStoryImagePath(getApplicationContext(), story.getId())))
                .fit()
                .centerInside()
                .into(mImage);
        mToolbar.setTitle(story.getTitle());
        mContent.setText(story.getContent());

    }
}
