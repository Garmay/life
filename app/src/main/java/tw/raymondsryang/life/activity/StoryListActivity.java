package tw.raymondsryang.life.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tw.raymondsryang.life.R;
import tw.raymondsryang.life.adapter.StoryAdapter;
import tw.raymondsryang.life.data.Error;
import tw.raymondsryang.life.data.Story;
import tw.raymondsryang.life.data.StoryDataRepo;
import tw.raymondsryang.life.data.StoryDataSource;

public class StoryListActivity extends AppCompatActivity {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_MULTI_OPTION = 1;

    private int mState = STATE_NORMAL;
    private List<Story> mSelected = new ArrayList<>();

    private StoryAdapter mStoryAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.story_list);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mStoryAdapter = new StoryAdapter(new ArrayList<Story>(), this);

        recyclerView.setAdapter(mStoryAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_story_btn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditStoryActivity.class);
                startActivity(intent);
                toNormalState();
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        switch (mState) {
            case STATE_NORMAL:
                getMenuInflater().inflate(R.menu.menu_story_list_normal, menu);
                break;
            case STATE_MULTI_OPTION:
                getMenuInflater().inflate(R.menu.menu_story_list_multi_chosen, menu);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (mState==STATE_MULTI_OPTION){
                    toNormalState();
                }
                break;
            case R.id.delete:
                delete();
                break;
            case R.id.about_life:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                Intent settingIntent = new Intent(this, SettingActivity.class);
                startActivity(settingIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        StoryDataRepo.getInstance().loadStories(getApplicationContext(), new StoryDataSource.LoadStoriesCallback() {
            @Override
            public void onStoriesLoad(List<Story> stories) {
                mStoryAdapter.changeData(stories);
            }

            @Override
            public void onFailed(Error error) {

            }
        });
    }

    private void delete(){
        String[] ids = new String[mSelected.size()];
        for (int i=0; i<ids.length; ++i){
            ids[i] = mSelected.get(i).getId();
        }

        StoryDataRepo.getInstance().deleteStories(
                getApplicationContext(),
                ids,
                new StoryDataSource.DeleteStoriesCallback() {
                    @Override
                    public void onStoriesDelete(List<Story> stories) {
                        mStoryAdapter.changeData(stories);
                        toNormalState();
                    }

                    @Override
                    public void onFailed(Error error) {
                        Toast.makeText(StoryListActivity.this, "刪除失敗!", Toast.LENGTH_SHORT).show();
                        toNormalState();
                    }
                }
        );
    }

    public void toNormalState(){
        mStoryAdapter.toNormalState();
        mState = STATE_NORMAL;
        mToolbar.setNavigationIcon(null);
        mToolbar.setTitle(R.string.app_name);
        mSelected.clear();
        invalidateOptionsMenu();
    }

    public void toMultiState(){
        mState = STATE_MULTI_OPTION;
        mToolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        invalidateOptionsMenu();
    }

    public void choseItem(View view, Story story){
        if (!mSelected.contains(story)) {
            view.setActivated(true);
            mSelected.add(story);
        } else {
            view.setActivated(false);
            mSelected.remove(story);
        }
        mToolbar.setTitle("已選擇" + mSelected.size() + "個項目");
    }

    public int getState() {
        return mState;
    }

    public void jumpToDetailActivity(Story story){
        Intent intent = new Intent(this, StoryDetailActivity.class);
        intent.putExtra("story", story);
        startActivity(intent);
    }
}
