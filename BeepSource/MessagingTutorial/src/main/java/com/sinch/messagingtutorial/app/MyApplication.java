package com.sinch.messagingtutorial.app;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class MyApplication extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        //Parse.initialize(this, "app-id", "client-key");
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "PIMI3CC3gw8UfOFXKxqRgPaxbfeKArNZSuic64bF","1yfvMjGpUJObIeqoIvmgV6UWaSEdm0nHI3v8lmhs");
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}
