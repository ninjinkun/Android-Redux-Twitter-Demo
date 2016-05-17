package io.github.ninjinkun.reduxtwitterdemo.redux.reducer;

import com.twitter.sdk.android.core.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TweetsReducerHelper {
    public static List<Tweet> mergeTweets(List<Tweet> prevTweets, List<Tweet> nextTweets) {
        List<Tweet> mergedTweets = prevTweets != null ? new ArrayList<>(prevTweets) : new ArrayList<Tweet>();
        if (nextTweets != null) {
            for (Tweet tweet : nextTweets) {
                if (!mergedTweets.contains(tweet)) {
                    mergedTweets.add(tweet);
                }
            }
        }
        Collections.sort(mergedTweets, new Comparator<Tweet>() {
            @Override
            public int compare(Tweet lhs, Tweet rhs) {
                try {
                    return parseTwitterDate(rhs.createdAt).compareTo(parseTwitterDate(lhs.createdAt));
                } catch (ParseException e) {
                    return 0;
                }
            }
        });
        return mergedTweets;
    }

    public static List<Tweet> updateLikedTweet(List<Tweet> prevTweets, Tweet likedTweet) {
        List<Tweet> nextTweets = prevTweets != null ? new ArrayList<>(prevTweets) : new ArrayList<Tweet>();

        int index = prevTweets.indexOf(likedTweet);
        if (index >= 0) {
            nextTweets.set(index, likedTweet);
        }
        return nextTweets;
    }

    public static List<Tweet> insertLikedTweet(List<Tweet> prevTweets, Tweet likedTweet) {
        List<Tweet> nextTweets = prevTweets != null ? new ArrayList<>(prevTweets) : new ArrayList<Tweet>();
        if (!prevTweets.contains(likedTweet)) {
            nextTweets = mergeTweets(nextTweets, Arrays.asList(likedTweet));
        }
        return nextTweets;
    }

    public static List<Tweet> removeLikedTweet(List<Tweet> prevTweets, Tweet likedTweet) {
        List<Tweet> nextTweets = prevTweets != null ? new ArrayList<>(prevTweets) : new ArrayList<Tweet>();
        nextTweets.remove(likedTweet);
        return nextTweets;
    }

    // http://stackoverflow.com/questions/4521715/twitter-date-unparseable
    private static Date parseTwitterDate(String twitterDate) throws ParseException {
        final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        sf.setLenient(true);
        return sf.parse(twitterDate);
    }
}
