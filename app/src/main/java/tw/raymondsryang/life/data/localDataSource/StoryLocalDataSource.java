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
    public void loadStory(Context context, String id, LoadStoryCallback callback) {

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
    public void updateStory(Context context, String id, Story story, UpdateStoryCallback callback) {

    }

    @Override
    public void deleteStories(final Context context, final String[] id, final DeleteStoriesCallback callback) {
        loadStories(context, new LoadStoriesCallback() {
            @Override
            public void onStoriesLoad(List<Story> stories) {
                List<Story> toBeDelete = new ArrayList<Story>();
                for(int i=0; i<stories.size();++i){
                    for (String anId : id) {
                        if (stories.get(i).getId().equals(anId)) {
                            toBeDelete.add(stories.get(i));
                        }
                    }
                }

                stories.removeAll(toBeDelete);

                try {
                    FileOutputStream outputStream = context.openFileOutput(mFileName, Context.MODE_PRIVATE);
                    outputStream.write(new Gson().toJson(stories).getBytes());
                    callback.onStoriesDelete(stories);
                } catch (FileNotFoundException e) {
                    callback.onFailed(new Error(e.getMessage()));
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(Error error) {
                callback.onFailed(error);
            }
        });
    }

    @Override
    public void insertStory(final Context context, final Story story, final InsertStoryCallback callback) {

        loadStories(context, new LoadStoriesCallback() {
            @Override
            public void onStoriesLoad(List<Story> stories) {
                try {
                    stories.add(0, story);
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
