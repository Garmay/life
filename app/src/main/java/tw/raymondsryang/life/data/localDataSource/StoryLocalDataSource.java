package tw.raymondsryang.life.data.localDataSource;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tw.raymondsryang.life.data.Error;
import tw.raymondsryang.life.data.Story;
import tw.raymondsryang.life.data.StoryDataSource;

public class StoryLocalDataSource implements StoryDataSource{

    private static String mFileName = "story";

    public StoryLocalDataSource() {
    }

    @Override
    public void loadStory(Context context, long id, LoadStoryCallback callback) {

    }

    @Override
    public void loadStories(Context context, LoadStoriesCallback callback) {
        try {
            Gson gson = new Gson();
            File file = new File(context.getFilesDir(), mFileName);
            if (file.exists()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                Story[] stories = gson.fromJson(stringBuilder.toString(), Story[].class);
                callback.onStoriesLoad(new ArrayList<Story>(Arrays.asList(stories)));
            } else {
                callback.onStoriesLoad(new ArrayList<Story>());
            }

        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailed(new Error(e.getMessage()));
        }

    }

    @Override
    public void updateStory(Context context, long id, Story story, UpdateStoryCallback callback) {

    }

    @Override
    public void deleteStories(Context context, long[] id, DeleteStoriesCallback callback) {

    }

    @Override
    public void insertStory(final Context context, final Story story, final InsertStoryCallback callback) {

        loadStories(context, new LoadStoriesCallback() {
            @Override
            public void onStoriesLoad(List<Story> stories) {
                try {
                    stories.add(story);
                    String newContent = new Gson().toJson(stories);
                    FileOutputStream outputStream = context.openFileOutput(mFileName, Context.MODE_PRIVATE);
                    outputStream.write(newContent.getBytes());
                    callback.onInsertCallback(story);
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onFailed(new Error(e.getMessage()));
                }
            }

            @Override
            public void onFailed(Error error) {
                callback.onFailed(error);
            }
        });

    }
}
