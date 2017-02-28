package tw.raymondsryang.life.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.aprilapps.easyphotopicker.EasyImage;
import tw.raymondsryang.life.R;
import tw.raymondsryang.life.config.Config;
import tw.raymondsryang.life.data.Error;
import tw.raymondsryang.life.data.Story;
import tw.raymondsryang.life.data.StoryDataRepo;
import tw.raymondsryang.life.data.StoryDataSource;
import tw.raymondsryang.life.utils.Utils;

import static tw.raymondsryang.life.R.id.photo;

public class EditStoryActivity extends AppCompatActivity {

    public static final int MODE_NEW = 0x01;
    public static final int MODE_EDIT = 0x02;

    private int mMode;

    private ImageView mPhotoView;
    private EditText mTitle, mContent;

    private Story mStory;
    private File mTmpPhoto = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);

        mMode = getIntent().getIntExtra("mode", MODE_NEW);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPhotoView = (ImageView) findViewById(photo);
        mTitle = (EditText) findViewById(R.id.title);
        mContent = (EditText) findViewById(R.id.content);

        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(EditStoryActivity.this, "Pick source", 0);
            }
        });

        if (savedInstanceState==null) {
            if (mMode == MODE_NEW) {

                mStory = new Story();

            } else if (mMode == MODE_EDIT) {

                mStory = (Story) getIntent().getSerializableExtra("story");
                if (mStory==null) throw new IllegalArgumentException(getClass().getName()+" need argument 'story' in edit mode");
                fillDataIntoScreen(mStory);

            }
        } else {
            mStory = (Story) savedInstanceState.getSerializable("story");
        }



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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("story", mStory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {

            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (imageFiles.size() > 0) {

                    Picasso.with(getApplicationContext())
                            .load(imageFiles.get(0))
                            .fit()
                            .centerInside()
                            .into(mPhotoView);

                    mTmpPhoto = imageFiles.get(0);
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }

    private void saveStory(){

        mStory.setTitle(mTitle.getText().toString());
        mStory.setContent(mContent.getText().toString());

        if(mMode == MODE_NEW) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            mStory.setDate(cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + Calendar.DAY_OF_MONTH);

            StoryDataRepo.getInstance().insertStory(getApplicationContext(), mStory, new StoryDataSource.InsertStoryCallback() {
                @Override
                public void onInsertCallback(Story story) {
                    saveTmpPhotoIntoFile(story.getId());
                    finish();
                }

                @Override
                public void onFailed(Error error) {
                    Toast.makeText(EditStoryActivity.this, "儲存失敗", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else if (mMode == MODE_EDIT) {
            StoryDataRepo.getInstance().updateStory(getApplicationContext(), mStory.getId(), mStory, new StoryDataSource.UpdateStoryCallback() {
                @Override
                public void onStoryUpdate(Story story) {
                    saveTmpPhotoIntoFile(story.getId());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("story", story);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }

                @Override
                public void onFailed(Error error) {
                    Toast.makeText(EditStoryActivity.this, "儲存失敗", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void fillDataIntoScreen(Story story){
        mContent.setText(story.getContent());
        mTitle.setText(story.getTitle());
        Picasso.with(getApplicationContext())
                .load(new File(Utils.getStoryImagePath(getApplicationContext(), story.getId())))
                .fit()
                .centerInside()
                .into(mPhotoView);
    }

    private void saveTmpPhotoIntoFile(final String storyId){
        if (mTmpPhoto==null)
            return;

        String newFilePath = getFilesDir().getPath()+"/"+Config.STORY_PHOTO_DIR;
        File file = new File(newFilePath);
        if(!file.exists() || !file.isDirectory()){
            file.mkdir();
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(new File(Utils.getStoryImagePath(getApplicationContext(), storyId)), false);
            FileInputStream inputStream = new FileInputStream(mTmpPhoto);

            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }

            inputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            //TODO exception handling
        }
    }

    public int getMode() {
        return mMode;
    }
}
