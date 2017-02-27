package tw.raymondsryang.life.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import tw.raymondsryang.life.R;
import tw.raymondsryang.life.activity.StoryListActivity;
import tw.raymondsryang.life.config.Config;
import tw.raymondsryang.life.data.Story;

import static com.squareup.picasso.Picasso.with;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private List<Story> mStorySet;
    private StoryListActivity mStoryListActivity;

    private boolean[] mSelected;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView title, content, date;
        public ImageView photo;


        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            photo = (ImageView) itemView.findViewById(R.id.photo);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
            date = (TextView) itemView.findViewById(R.id.date);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mStoryListActivity.getState()==StoryListActivity.STATE_NORMAL) {
                        mStoryListActivity.toMultiState();
                    }

                    return false;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mStoryListActivity.getState()==StoryListActivity.STATE_MULTI_OPTION) {
                        mStoryListActivity.choseItem(view, mStorySet.get(getAdapterPosition()));
                        mSelected[getAdapterPosition()] = view.isActivated();
                    }
                }
            });
        }
    }

    public StoryAdapter(List<Story> storySet, StoryListActivity storyListActivity){
        this.mStorySet = storySet;
        this.mStoryListActivity = storyListActivity;
        mSelected = new boolean[mStorySet.size()];
    }

    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.rootView.setActivated(mSelected[position]);
        holder.title.setText(mStorySet.get(position).getTitle());
        holder.content.setText(mStorySet.get(position).getContent());
        holder.date.setText(mStorySet.get(position).getDate());

        String path = holder.rootView.getContext().getFilesDir()+"/"+Config.STORY_PHOTO_DIR+"/"+mStorySet.get(position).getId();
        File file = new File(path);
        boolean exist = file.exists();

        Picasso
                .with(holder.rootView.getContext())
                .load(file)
                .into(holder.photo);

    }

    @Override
    public int getItemCount() {
        return mStorySet==null?0:mStorySet.size();
    }

    public void changeData(List<Story> stories){
        this.mStorySet = stories;
        mSelected = new boolean[mStorySet.size()];
        notifyDataSetChanged();
    }

    public void toNormalState(){
        for(int i=0;i<mSelected.length;++i){
            mSelected[i] = false;
        }
        notifyDataSetChanged();
    }

}


