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
import tw.raymondsryang.life.utils.Utils;

public class StoryLocalDataSource implements StoryDataSource{

    private static String mFileName = "story";

    public StoryLocalDataSource() {
    }

    @Override
    public void loadStory(final Context context, final String id, final LoadStoryCallback callback) {
        loadStories(context, new LoadStoriesCallback() {
            @Override
            public void onStoriesLoad(List<Story> stories) {
                for (Story item : stories) {
                    if (item.getId().equals(id)) {
                        callback.onStoryLoad(item);
                        return;
                    }
                }
                callback.onFailed(new Error("story with id: "+id+" not found!"));
            }

            @Override
            public void onFailed(Error error) {
                callback.onFailed(error);
            }
        });
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
    public void updateStory(final Context context, final String id, final Story story, final UpdateStoryCallback callback) {
        loadStories(context, new LoadStoriesCallback() {
            @Override
            public void onStoriesLoad(List<Story> stories) {
                /*
                此處避免使用list.get()，由於list之get方法時間複雜度為O(n)
                若於迴圈中使用該方法，將導致搜尋時間複雜度上升至O(n^2)
                 */
                int index = 0;
                for (Story item: stories) {
                    if (item.getId().equals(id)){
                        /*Update story will refresh its id*/
                        story.refreshId();

                        stories.remove(index);
                        stories.add(index, story);
                        break;
                    }
                    ++index;
                }



                final String newContent = new Gson().toJson(stories);
                try {
                    FileOutputStream outputStream = context.openFileOutput(mFileName, Context.MODE_PRIVATE);
                    outputStream.write(newContent.getBytes());
                    outputStream.close();
                    callback.onStoryUpdate(story);
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
                /*同時刪除story的image*/
                for (Story item:toBeDelete) {
                    File file = new File(Utils.getStoryImagePath(context, item.getId()));
                    if (file.exists()){
                        //noinspection ResultOfMethodCallIgnored
                        file.delete();
                    }
                }

                try {
                    FileOutputStream outputStream = context.openFileOutput(mFileName, Context.MODE_PRIVATE);
                    outputStream.write(new Gson().toJson(stories).getBytes());
                    outputStream.close();
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
        insertStory(context, 0, story, callback);
    }

    @Override
    public void insertStory(final Context context, final int index, final Story story, final InsertStoryCallback callback) {
        loadStories(context, new LoadStoriesCallback() {
            @Override
            public void onStoriesLoad(List<Story> stories) {
                try {
                    stories.add(index, story);
                    String newContent = new Gson().toJson(stories);
                    FileOutputStream outputStream = context.openFileOutput(mFileName, Context.MODE_PRIVATE);
                    outputStream.write(newContent.getBytes());
                    outputStream.close();
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
