package com.nagra.es.screengrabbingdetection;

import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetectionActivity extends AppCompatActivity {

    public static final String PARAM_DISPLAY_IDS = "com.nagra.es.screengrabbingdetection.DetectionActivity.DISPLAY_IDS";
    private static final String[] DISPLAY_STATES = {
            "STATE_UNKNOWN",
            "STATE_OFF",
            "STATE_ON",
            "STATE_DOZE",
            "STATE_DOZE_SUSPEND"
    };
    private TextView numberOfDisplayText;
    private ExpandableListView displayIdsList;
    ExpandableListAdapter displayIdsAdapter;
    List<String> displayIdsHeader;
    HashMap<String, List<String>> displayIdsDataChild;
    DisplayManager displayManager;

    DetectionActivity()
    {
        displayManager = (DisplayManager) getApplicationContext().getSystemService(DISPLAY_SERVICE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        Intent ongoingSGDetectedIntent = getIntent();
        int[] displayIds = ongoingSGDetectedIntent.getIntArrayExtra(PARAM_DISPLAY_IDS);

        numberOfDisplayText = (TextView)findViewById(R.id.numberOfDisplay);
        displayIdsList = (ExpandableListView)findViewById(R.id.displayIdsList);

        numberOfDisplayText.setText(displayIds.length);
        displayIdsHeader = new ArrayList<String>();
        displayIdsDataChild = new HashMap<String, List<String>>();

        displayIdsHeader.add(getResources().getString(R.string.display_id));
        displayIdsHeader.add(getResources().getString(R.string.display_mode));
        displayIdsHeader.add(getResources().getString(R.string.display_state));

        List<String> displayIdsDataList = new ArrayList<>();
        List<String> displayModesDataList = new ArrayList<>();
        List<String> displayStatesDataList = new ArrayList<>();

        for (int ix = 0; ix < displayIds.length; ix++)
        {
            displayIdsDataList.add(displayIds[ix]);
            Display aDisplay = displayManager.getDisplay(displayIds[ix]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                displayModesDataList.add(aDisplay.getMode().toString());
            }
            else
            {
                displayModesDataList.add(getResources().getString(R.string.mode_not_supported));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH)
            {
                displayStatesDataList.add(DISPLAY_STATES[aDisplay.getState()]);
            }
            else
            {
                displayStatesDataList.add(getResources().getString(R.string.state_not_supported));
            }
        }

        displayIdsDataChild.put(displayIdsHeader.get(0), displayIdsDataList);
        displayIdsDataChild.put(displayIdsHeader.get(1), displayModesDataList);
        displayIdsDataChild.put(displayIdsHeader.get(2), displayStatesDataList);

        displayIdsAdapter = new ExpandableListAdapter(this, displayIdsHeader, displayIdsDataChild);

        displayIdsList.setAdapter(displayIdsAdapter);
    }
}
