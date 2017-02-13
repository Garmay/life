package tw.raymondsryang.life.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import tw.raymondsryang.life.R;
import tw.raymondsryang.life.data.Error;
import tw.raymondsryang.life.data.Story;
import tw.raymondsryang.life.data.StoryDataRepo;
import tw.raymondsryang.life.data.StoryDataSource;

public class AddStoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveStory();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveStory(){
        StoryDataRepo.getInstance().insertStory(getApplicationContext(), new Story(), new StoryDataSource.InsertStoryCallback() {
            @Override
            public void onInsertCallback(Story story) {
                Toast.makeText(AddStoryActivity.this, "complete", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(Error error) {
                Toast.makeText(AddStoryActivity.this, error.msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
