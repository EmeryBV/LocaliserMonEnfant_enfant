package com.example.localisermonenfant_enfant.activity.Contacts.CallLog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.activity.Contacts.SMS.MessageAdapter;
import com.example.localisermonenfant_enfant.activity.Contacts.SMS.Sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CallLogActivity extends AppCompatActivity {

    private String TAG = "TAG";
    private static final int MY_READ_PERMISSION_CODE = 105;
    ArrayList<CallLogModel>  callLogList = new ArrayList<>();

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            {
                Log.i(TAG, "Contacts permission NOT granted");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, MY_READ_PERMISSION_CODE);
                return;
            }
        }else getCallDetails();
    }


    private void getCallDetails() {
        Intent intent = getIntent();
        if (intent.hasExtra("phoneNumber")){ // vérifie qu'une valeur est associée à la clé “edittext”
            phoneNumber = intent.getStringExtra("phoneNumber"); // on récupère la valeur associée à la clé
        }


        setContentView(R.layout.activity_call_log);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(
                new LinearLayoutManager
                        (this));

        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            if(phoneNumber!=null)
                if(phoneNumber.equals(managedCursor.getString(number))){
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = getString(R.string.OutGoing);
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = getString(R.string.Incoming);
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = getString(R.string.Missed);
                    break;
            }
            CallLogModel callLogModel = new CallLogModel(phNumber,callDayTime.toString()+ " s",callDuration,dir);
            callLogList.add(callLogModel);
        }
        }

        managedCursor.close();



        if (intent.hasExtra("name")){ // vérifie qu'une valeur est associée à la clé “edittext”
            String name = intent.getStringExtra("name"); // on récupère la valeur associée à la clé
            TextView titleName = findViewById(R.id.titleName);
            titleName.setText(name);
        }



        CallLogAdapter callLogAdapter = new CallLogAdapter(callLogList);
        recyclerView.setAdapter(callLogAdapter);


    }


}