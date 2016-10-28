package com.nagra.es.screengrabbingdetection;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.hardware.display.VirtualDisplay.Callback;
import android.media.MediaRouter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;

import java.util.Vector;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenGrabbingIntentService
        extends IntentService
        implements DisplayManager.DisplayListener
{
    // Debug Log TAG
    private static final String TAG = "SGDService";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SGDETECTION = "com.nagra.es.screengrabbingdetection.action.SGDetection";

    // TODO: Rename parameters
    private static final String ACTION_PARAM_METHOD = "com.nagra.es.screengrabbingdetection.extra.PARAM_METHOD";

    // ACTION_PARAM_METHOD_VALUES
    public static final String ACTION_PARAM_METHOD_VALUE_BASIC = "com.nagra.es.screengrabbingdetection.extra.METHOD_VALUE_BASIC";

    public static Context context = null;

    private final MediaRouter.SimpleCallback mMediaRouterCallback = new MediaRouter.SimpleCallback()
    {
        @Override
        public void onRouteSelected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
            Log.d(TAG, "onRouteSelected: type=" + type + ", info=" + info);
            // TODO:
        }

        @Override
        public void onRouteUnselected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
            Log.d(TAG, "onRouteUnselected: type=" + type + ", info=" + info);
            // TODO:
        }

        @Override
        public void onRoutePresentationDisplayChanged(MediaRouter router, MediaRouter.RouteInfo info) {
            Log.d(TAG, "onRoutePresentationDisplayChanged: info=" + info);
            // TODO:
        }
    };

    private final VirtualDisplay.Callback virtualDisplayCallback = new VirtualDisplay.Callback() {
        @Override
        public void onPaused() {
            Log.d(TAG, "VirtualDisplay.Callback.onPaused: ");
        }

        @Override
        public void onResumed() {
            Log.d(TAG, "VirtualDisplay.Callback.onPaused: ");
        }

        @Override
        public void onStopped() {
            Log.d(TAG, "VirtualDisplay.Callback.onStopped: ");
        }
    };

    public ScreenGrabbingIntentService()
    {
        super("ScreenGrabbingIntentService");
        context = getApplicationContext();
        displayManager = (DisplayManager) getApplicationContext().getSystemService(DISPLAY_SERVICE);
    }

    /**
     * Starts this service to perform action SGDetection with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionSGDetection(Context context, String method) {
        Intent intent = new Intent(context, ScreenGrabbingIntentService.class);
        intent.setAction(ACTION_SGDETECTION);
        intent.putExtra(ACTION_PARAM_METHOD, method);
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        synchronized(this) {
            clientBound = true;
        }
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SGDETECTION.equals(action)) {
                final String method = intent.getStringExtra(ACTION_PARAM_METHOD);
                handleActionSGDetection(method);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSGDetection(String method) {
        displayManager.registerDisplayListener(this);
        do
        {
            Thread.currentThread().sleep(500);
            synchronized(this)
            {
                if (SGDetected && clientBound)
                {
                    // TODO: call OngoingSGDetectedInterface.OngoingSGDetected through binder
                }
            }
        }
        while (running);
    }

    @Override
    public void onDisplayAdded(int displayId) {
        Log.d(TAG, "onDisplayAdded: displayId = " + displayId);
        synchronized(this) {
            Display theDisplay = displayManager.getDisplay(displayId);
            int flags = theDisplay.getFlags();
            if (! displayIds.contains(displayId) && flags & VirtualDisplay.) {
                SGDetected = true;
                displayIds.add(displayId);
                displayNb = displayIds.size();
            }
        }
    }

    @Override
    public void onDisplayRemoved(int displayId) {
        Log.d(TAG, "onDisplayRemoved: displayId = " + displayId);
        synchronized(this) {
            Display theDisplay = displayManager.getDisplay(displayId);
            int flags = theDisplay.getFlags();
            if (displayIds.contains(displayId)) {
                displayIds.remove(displayId);
                displayNb = displayIds.size();
            }
            if (displayIds.isEmpty()) {
                SGDetected = false;
            }
        }
    }

    @Override
    public void onDisplayChanged(int displayId) {
        Log.d(TAG, "onDisplayChanged: displayId = " + displayId);
    }

    private DisplayManager displayManager;
    private Vector<Integer> displayIds = new Vector<>(5);
    private volatile boolean SGDetected = false;
    private volatile int displayNb = 0;
    private volatile boolean running = true;
    private volatile boolean clientBound = false;
}
