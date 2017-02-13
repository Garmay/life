package tw.raymondsryang.life.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tw.raymondsryang.life.R;
import tw.raymondsryang.life.adapter.StoryAdapter;
import tw.raymondsryang.life.data.Error;
import tw.raymondsryang.life.data.Story;
import tw.raymondsryang.life.data.StoryDataRepo;
import tw.raymondsryang.life.data.StoryDataSource;

public class StoryListActivity extends AppCompatActivity {

    private StoryAdapter mStoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.story_list);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mStoryAdapter = new StoryAdapter(new ArrayList<Story>());

        recyclerView.setAdapter(mStoryAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_story_btn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddStoryActivity.class);
                startActivity(intent);
            }
        });
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
}
