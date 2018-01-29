package com.leitner.tabbedexample;

/**
 * Created by i0004913 on 12.10.2017.
 */

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = "MyFirebaseIIDService";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onTokenRefresh(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        if (refreshedToken!=null) {
//            SettingPreferences.setStringValueInPref(this, SettingPreferences.REG_ID, refreshedToken);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor = sharedPreferences.edit();
            if (sharedPreferences.getBoolean("app_is_registered", false)){
                //e-mail
                //resort
                //registration_token
                try{
                    URL url = new URL("http://185.131.252.18/Notifications/api/register");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("API_KEY", "qwertzasdfg");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("accept", "application/json");
                    connection.setConnectTimeout(10000);
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    JSONObject registrationThings = new JSONObject();
                    registrationThings.put("e_mail", sharedPreferences.getString("e_mail", ""));
                    registrationThings.put("token", refreshedToken);
                    registrationThings.put("token_old", sharedPreferences.getString("registration_token", ""));
                    registrationThings.put("resort", sharedPreferences.getString("resort", ""));

                    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                    wr.write(registrationThings.toString());
                    wr.flush();
                }catch (Exception e){}
            }
        }
        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(refreshedToken);
    }
}
