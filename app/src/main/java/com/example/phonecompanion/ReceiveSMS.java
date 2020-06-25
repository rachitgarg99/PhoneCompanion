package com.example.phonecompanion;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class ReceiveSMS extends BroadcastReceiver {
    String i="";


    @SuppressLint("ShowToast")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.provider.Telephony.SMS_RECEIVED")) {
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();
            SmsManager sms1 = SmsManager.getDefault();

            String action = intent.getAction();
            assert action != null;
            if(action.equals("my.action.string")){
                i= Objects.requireNonNull(intent.getExtras()).getString("pass");
                //do your stuff
            }

            assert i != null;



            try {
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    Object[] pdus = (Object[]) bundle.get("pdus");

                    assert pdus != null;
                    String senderNumber = null;
                    String message = null;





                    for (Object pdu : pdus) {
                        SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);


                        message = sms.getMessageBody();


                        senderNumber = sms.getOriginatingAddress();
                    }



                    //Edited from here
                    assert message != null;

                    if (message.contains(i+"Silent")) {
                        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        //To here
                    }
                    if (message.contains(i+"Vibrate")) {
                        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        //To here
                    }
                    if (message.contains(i+"Ring")) {
                        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        //To here
                    }

                    String[] message1 = message.split(": ");
                    if (message1[0].contains(i+" Contact")){




                        String ret = null;
                        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" +message1[1] +"%'";
                        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
                        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,projection, selection, null, null);
                        assert c != null;
                        if (c.moveToFirst()) {
                            ret = c.getString(0);
                        }
                        c.close();
                        if(ret==null)
                            ret = "Unsaved";

                        sms1.sendTextMessage(senderNumber, null, ret, null, null);

                    }




                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("MyReceiver", "Exception smsReceiver" + e);

            }


        }
    }
}
