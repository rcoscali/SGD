// ISGDetectedInterface.aidl
package com.nagra.es.screengrabbingdetection;

// Declare any non-default types here with import statements

interface ISGDetectedInterface {
    /**
     * An ongoing ScreenGrabbing has just been detected.
     * This method is called with the displayIds
     */
    void OngoingSGDetected(out int[] displayIds);
}
