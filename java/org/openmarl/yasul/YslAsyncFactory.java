/*
 * yasul: Yet another Android SU library. 
 *
 * t0kt0ckus
 * (C) 2014,2015
 * 
 * License LGPLv2, GPLv3
 * 
 */
package org.openmarl.yasul;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class YslAsyncFactory extends AsyncTask<Void,Void,YslSession> {

    private final Context mAppCtx;
    private final YslContext mYslContext;
    private final YslObserver mClient;
    private final int mCtlFlags;

    YslAsyncFactory(Context appCtx, YslContext yslContext, YslObserver client, int flags) {
        mAppCtx = appCtx;
        mYslContext =yslContext;
        mClient = client;
        mCtlFlags = flags;
    }

    @Override
    protected YslSession doInBackground(Void... params) {
        YslPort port = Libyasul.open(mCtlFlags);
        if (port != null) {
            YslSession session = new YslSession(mAppCtx, port.pid, port.ID, port.stdout,
                    port.stderr);
            Log.i(TAG,
                    String.format("Shell session: %s", session.toString()));
            return session;
        }
        Log.e(TAG,
                "Failed to create session, see Logcat and/or yasul-<pid>.log for possible cause !");
        return null;
    }

    @Override
    protected void onPostExecute(YslSession yslSession) {
        super.onPostExecute(yslSession);
        if (mClient != null)
            mClient.onSessionFactoryEvent(yslSession);
    }

    private static final String TAG = "YASUL";
}