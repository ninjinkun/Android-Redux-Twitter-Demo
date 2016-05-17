package io.github.ninjinkun.reduxtwitterdemo.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.github.ninjinkun.redux.StateType;
import io.github.ninjinkun.redux.StoreSubscriber;
import io.github.ninjinkun.redux.StoreType;
import io.github.ninjinkun.reduxtwitterdemo.R;
import io.github.ninjinkun.reduxtwitterdemo.databinding.FragmentTimelineBinding;
import io.github.ninjinkun.reduxtwitterdemo.redux.StoreCreator;
import io.github.ninjinkun.reduxtwitterdemo.redux.action.LikedTweetsAction;
import io.github.ninjinkun.reduxtwitterdemo.redux.action.TimelineAction;
import io.github.ninjinkun.reduxtwitterdemo.redux.state.AppState;
import io.github.ninjinkun.reduxtwitterdemo.redux.state.FetchStatus;
import io.github.ninjinkun.reduxtwitterdemo.view.adapter.TweetsRecyclerViewAdapter;

public class TimelineFragment extends Fragment implements StoreSubscriber<AppState> {
    private static final String ARG_TYPE_KEY = "type";

    public enum Type {
        Timeline, LikedTweets
    }

    @NonNull
    private Type type = Type.Timeline;
    private TweetsRecyclerViewAdapter adapter;
    private FragmentTimelineBinding binding;
    private LinearLayoutManager recyclerLayoutManager;

    public static TimelineFragment newInstance(Type type) {
        Bundle args = new Bundle();
        TimelineFragment fragment = new TimelineFragment();
        args.putInt(ARG_TYPE_KEY, type.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false);

        recyclerLayoutManager = new LinearLayoutManager(getContext());
        binding.recycler.setLayoutManager(recyclerLayoutManager);
        binding.recycler.setAdapter(adapter);
        binding.recycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        binding.recycler.addOnScrollListener(recyclerScrollListener);

        binding.swipeRefresh.setOnRefreshListener(swipeRefreshListener);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = Type.values()[getArguments().getInt(ARG_TYPE_KEY)];
        List<Tweet> tweets = StoreCreator.store.getState().getTimeline().getTweets();
        adapter = new TweetsRecyclerViewAdapter(getContext());
        adapter.setTweets(tweets != null ? tweets : new ArrayList<Tweet>());
    }

    @Override
    public void onResume() {
        super.onResume();
        StoreCreator.store.subscribe(this);

        if (isInitial(StoreCreator.store.getState(), type)) {
            fetchRecentTweets();
        }
    }

    private void fetchRecentTweets() {
        switch (type) {
            case Timeline: {
                Long sinceId = StoreCreator.store.getState().getTimeline().getSinceId();
                StoreCreator.store.dispatch(TimelineAction.fetchTimeline(sinceId, null), fetchCallback);
                break;
            }
            case LikedTweets: {
                Long sinceId = StoreCreator.store.getState().getLikedTweets().getSinceId();
                StoreCreator.store.dispatch(LikedTweetsAction.fetchLikedTweets(sinceId, null), fetchCallback);
                break;
            }
        }
    }

    private void fetchPastTweets() {
        switch (type) {
            case Timeline: {
                Long maxId = StoreCreator.store.getState().getTimeline().getMaxId();
                StoreCreator.store.dispatch(TimelineAction.fetchTimeline(null, maxId), fetchCallback);
                break;
            }
            case LikedTweets: {
                Long maxId = StoreCreator.store.getState().getLikedTweets().getMaxId();
                StoreCreator.store.dispatch(LikedTweetsAction.fetchLikedTweets(null, maxId), fetchCallback);
                break;
            }
        }
    }

    private FetchStatus fetchStatus(AppState state, Type type) {
        switch (type) {
            case Timeline: {
                return state.getTimeline().getFetchStatus();
            }
            case LikedTweets: {
                return state.getLikedTweets().getFetchStatus();
            }
            default:
                return null;
        }
    }

    private boolean isInitial(AppState state, Type type) {
        return fetchStatus(state, type) == FetchStatus.Initial;
    }

    private boolean isFetching(AppState state, Type type) {
        return fetchStatus(state, type) == FetchStatus.Fetching;
    }

    @Override
    public void onPause() {
        super.onPause();
        StoreCreator.store.unsubscribe(this);
    }

    @Override
    public void newState(AppState state) {
        List<Tweet> tweets = null;
        switch (type) {
            case Timeline:
                tweets = StoreCreator.store.getState().getTimeline().getTweets();
                break;
            case LikedTweets:
                tweets = StoreCreator.store.getState().getLikedTweets().getTweets();
                break;
        }
        adapter.setTweets(tweets != null ? tweets : new ArrayList<Tweet>());
        // In production, you should calculate diff and update each items.
        adapter.notifyDataSetChanged();

        if (!isFetching(StoreCreator.store.getState(), type)) {
            binding.swipeRefresh.setRefreshing(false);
        }
    }

    private OnRefreshListener swipeRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (!isFetching(StoreCreator.store.getState(), type)) {
                fetchRecentTweets();
            }
        }
    };

    private RecyclerView.OnScrollListener recyclerScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (dy > 0) {
                int visibleItemCount = recyclerLayoutManager.getChildCount();
                int totalItemCount = recyclerLayoutManager.getItemCount();
                int pastVisiblesItems = recyclerLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && !isFetching(StoreCreator.store.getState(), type)) {
                    fetchPastTweets();
                }
            }
        }
    };

    private StoreType.DispatchCallback fetchCallback = new StoreType.DispatchCallback() {
        @Override
        public void call(StateType stateType) {
            if (binding != null) {
                binding.swipeRefresh.setRefreshing(false);
            }
        }
    };
}
