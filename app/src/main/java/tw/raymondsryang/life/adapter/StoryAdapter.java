package tw.raymondsryang.life.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tw.raymondsryang.life.R;
import tw.raymondsryang.life.data.Story;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private List<Story> mStorySet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public StoryAdapter(List<Story> storySet){
        this.mStorySet = storySet;
    }

    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mStorySet==null?0:mStorySet.size();
    }

    public void changeData(List<Story> stories){
        this.mStorySet = stories;
        notifyDataSetChanged();
    }
}


