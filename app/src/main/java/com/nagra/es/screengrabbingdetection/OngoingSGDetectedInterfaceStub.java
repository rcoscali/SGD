package com.nagra.es.screengrabbingdetection;

import android.content.Intent;
import android.os.RemoteException;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by cohen on 28/10/16.
 */
public class OngoingSGDetectedInterfaceStub
        extends ISGDetectedInterface.Stub
{
    @Override
    public void OngoingSGDetected(int[] displayIds)
            throws RemoteException
    {
        Intent start = new Intent(ScreenGrabbingIntentService.context, DetectionActivity.class);
        start.putExtra(DetectionActivity.PARAM_DISPLAY_IDS, displayIds);
        startActivity(DetectionActivity, startActivity(), null);
    }
}
