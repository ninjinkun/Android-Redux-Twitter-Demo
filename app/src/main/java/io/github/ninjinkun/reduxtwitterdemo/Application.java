package io.github.ninjinkun.reduxtwitterdemo;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import io.fabric.sdk.android.Fabric;
import io.github.ninjinkun.reduxtwitterdemo.redux.StoreCreator;
import io.github.ninjinkun.reduxtwitterdemo.redux.action.AuthSessionAction;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        Iconify.with(new FontAwesomeModule());

        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            StoreCreator.store.dispatch(new AuthSessionAction.LoadAuthSessionAction(session));
        }
    }
}
