package io.github.ninjinkun.reduxtwitterdemo.view.adapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import io.github.ninjinkun.reduxtwitterdemo.BR;
import io.github.ninjinkun.reduxtwitterdemo.R;
import io.github.ninjinkun.reduxtwitterdemo.databinding.ItemTweetBinding;
import io.github.ninjinkun.reduxtwitterdemo.redux.StoreCreator;
import io.github.ninjinkun.reduxtwitterdemo.redux.action.LikedTweetsAction;

public class TweetsRecyclerViewAdapter extends RecyclerView.Adapter<TweetsRecyclerViewAdapter.ItemViewHolder> {
    private List<Tweet> tweets;
    private Context context;

    public TweetsRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ItemTweetBinding binding;

        public ItemViewHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public ItemTweetBinding getBinding() {
            return binding;
        }
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Tweet tweet = tweets.get(position);

        holder.getBinding().setVariable(BR.tweet, tweet);
        holder.getBinding().executePendingBindings();
        @ColorInt int colorRes = tweet.favorited ? R.color.colorAccent : android.R.color.darker_gray;
        holder.getBinding().likeButton.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_heart).sizeDp(14).colorRes(colorRes));
        holder.getBinding().likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.favorited) {
                    StoreCreator.store.dispatch(LikedTweetsAction.deleteLike(tweet.id));
                } else {
                    StoreCreator.store.dispatch(LikedTweetsAction.postLike(tweet.id));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(final SimpleDraweeView view, final String url) {
        view.setImageURI(Uri.parse(url));
    }
}