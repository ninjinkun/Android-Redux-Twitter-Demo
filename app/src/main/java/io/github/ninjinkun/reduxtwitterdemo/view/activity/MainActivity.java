package io.github.ninjinkun.reduxtwitterdemo.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;

import io.github.ninjinkun.reduxtwitterdemo.R;
import io.github.ninjinkun.reduxtwitterdemo.databinding.ActivityMainBinding;
import io.github.ninjinkun.reduxtwitterdemo.redux.StoreCreator;
import io.github.ninjinkun.reduxtwitterdemo.view.fragment.TimelineFragment;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        binding.pager.setAdapter(sectionsPagerAdapter);

        binding.tabs.setupWithViewPager(binding.pager);

        if (StoreCreator.store.getState().getSession().getSession() == null) {
            startActivityForResult(new Intent(getBaseContext(), LoginActivity.class), 0);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TimelineFragment.newInstance(Arrays.asList(TimelineFragment.Type.Timeline, TimelineFragment.Type.LikedTweets).get(position));
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Timeline";
                case 1:
                    return "Likes";
            }
            return null;
        }
    }
}
