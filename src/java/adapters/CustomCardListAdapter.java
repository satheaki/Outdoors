package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.theoutdoors.R;

import java.util.List;

import app.VolleyController;
import model.Hikes;

/**
 * Created by Akshay on 3/8/2018.
 */

public class CustomCardListAdapter extends RecyclerView.Adapter<CustomCardListAdapter.ItemViewHolder> {

    List<Hikes> mHikeArticles;
    Context mContext;

    public CustomCardListAdapter(Context context, List<Hikes> hikeArticles) {
        this.mHikeArticles = hikeArticles;
        this.mContext =context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view,parent,false);
        return new ItemViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Hikes articleObj= mHikeArticles.get(position);
        holder.networkThumbnailImg.setImageUrl(articleObj.getThumbnailUrl(), VolleyController.getVolleyInstance(mContext).getImageLoader());
        holder.title.setText(articleObj.getName());
        holder.description.setText(articleObj.getDescription());
        holder.state.setText(articleObj.getState());
    }

    @Override
    public int getItemCount() {
        return mHikeArticles.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title,description,state;
        NetworkImageView networkThumbnailImg;

        public ItemViewHolder(View newsViewHolder) {
            super(newsViewHolder);
            networkThumbnailImg=(NetworkImageView)newsViewHolder.findViewById(R.id.n_image_view);
            title=(TextView)newsViewHolder.findViewById(R.id.tv_hike_name);
            description=(TextView)newsViewHolder.findViewById(R.id.tv_hike_description);
            state=(TextView)newsViewHolder.findViewById(R.id.tv_hike_state);
        }
    }
}
