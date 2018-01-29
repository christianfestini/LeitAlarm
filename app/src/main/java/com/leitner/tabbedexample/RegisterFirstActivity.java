package com.leitner.tabbedexample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaCodec;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by i0004913 on 12.10.2017.
 */

public class RegisterFirstActivity extends FragmentActivity {
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button registerButton;
    private Spinner spinner;
    private EditText email;
    private ProgressBar progressBarasSpinner;

    ProgressDialog progressDialog;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_first);
        DownloadResortsFromServer downloadResortsFromServer = new DownloadResortsFromServer(getApplicationContext());

        registerButton = (Button)findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finishOnBoarding();
            }
        });

        email = (EditText)findViewById(R.id.editText_email);

        spinner = (Spinner)findViewById(R.id.spinner_resorts);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.resorts, android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        progressBarasSpinner = (ProgressBar)findViewById(R.id.progress_bar_registration);
        progressDialog = new ProgressDialog(RegisterFirstActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.download_resorts));
        progressDialog.show();
        downloadResortsFromServer.execute();
    }

    private void finishOnBoarding(){
        Matcher m = VALID_EMAIL_ADDRESS_REGEX.matcher(email.getText().toString());
//        SharedPreferences preferences =
//                getSharedPreferences("my_preferences", MODE_PRIVATE);
//
//        preferences.edit()
//                .putBoolean("onboarding_complete", true).apply();
//        Toast.makeText(this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG);
        if (email.getText().toString().matches("")){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                builder = new AlertDialog.Builder(this);
            }
            else{
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Warning")
            .setMessage("Please fill all necessary fields")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else if (!m.matches()){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                builder = new AlertDialog.Builder(this);
            }
            else{
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Error")
                    .setMessage("Please fill in a valid email address")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            RegisterToServer registerToServer = new RegisterToServer(this);
            String[] params = new String[2];
            params[0] = email.getText().toString();
            params[1] = spinner.getSelectedItem().toString();
            registerToServer.execute(params);
        }

       //Intent main = new Intent(this, MainActivity.class);
//        startActivity(main);
//        finish();
    }

    class DownloadResortsFromServer extends  AsyncTask<String, Void, String>{
        private Context mContext;
        public DownloadResortsFromServer(Context context){this.mContext = context;}

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            email.setEnabled(false);
            spinner.setEnabled(false);
            registerButton.setEnabled(false);
        }
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            try{
               url = new URL("http://185.131.252.18/Notifications/api/resort");
//                url = new URL("http://ws.geonames.org/findNearByWeatherJSON?lat=" + 37.77493 + "&lng=" + -122.419416);
               HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("API_KEY", "qwertzasdfg");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8")
                );
                String line = null;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }
                return  stringBuilder.toString();

            }catch (Exception e){
                Log.d("WEBRESPONSE", "thefuk");return "nein";}
        }

        protected void onPostExecute(String result){
            progressBarasSpinner.setVisibility(View.INVISIBLE);
            email.setEnabled(true);
            spinner.setEnabled(true);
            registerButton.setEnabled(true);
            progressDialog.dismiss();
            if (!result.equals("nein")){
                try{
                    result = result.replace("[", "");
                    result = result.replace("]", "");
                    result = result.replace("\"", "");
                    result = result.replace("\n", "");
                    result = result.replace("\\", "");
                    String[] stringArrayList = result.split(",");
                    List<String> spinnerList = Arrays.asList(stringArrayList);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            mContext, R.layout.spinner_item, spinnerList);
    //                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinner.setAdapter(adapter);
                }catch (Exception e){
                    Log.d("EXCEPTION", e.getMessage());
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterFirstActivity.this);
                builder.setTitle(R.string.alert);
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setMessage(R.string.connection_timeout);
                builder.show();
            }
        }
    }

    class RegisterToServer extends AsyncTask<String, Void, String>{
        private Exception exception;
        private Context mContext;
        public RegisterToServer(Context context){
            mContext = context;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            email.setEnabled(false);
            spinner.setEnabled(false);
            registerButton.setEnabled(false);
            progressBarasSpinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls){
            URL url;
            String email = urls[0];
            String resort = urls[1];

            try{
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d("TOKEN", token);
                //TODO: insert real address of registration server
//                url = new URL("http://10.0.2.2:56589/api/register");
                url = new URL("http://185.131.252.18/Notifications/api/register");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("API_KEY", "qwertzasdfg");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("accept", "application/json");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                JSONObject registrationThings = new JSONObject();
                registrationThings.put("e_mail", email);
                registrationThings.put("token", token);
                registrationThings.put("resort", resort);

                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(registrationThings.toString());
                wr.flush();

                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8")
                );
                String line = null;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }

                bufferedReader.close();
                Log.d("Web Response", stringBuilder.toString());
                return stringBuilder.toString();
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (Exception e){
                return "Exception";
//                e.printStackTrace();
            }
            return "Error";
        }

        protected  void onPostExecute(String result){
            progressBarasSpinner.setVisibility(View.INVISIBLE);
            email.setEnabled(true);
            spinner.setEnabled(true);
            registerButton.setEnabled(true);

            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterFirstActivity.this);

            if (result.contains("succesfully")){
                //Success
                //Toast.makeText(RegisterFirstActivity.this, "You registered yourself successfully", Toast.LENGTH_SHORT);
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                editor = sharedPreferences.edit();
                editor.putBoolean("app_is_registered", true);
                editor.putString("e_mail", email.getText().toString()).commit();
                editor.putString("resort", spinner.getSelectedItem().toString());
                editor.putString("registration_token", FirebaseInstanceId.getInstance().getToken()).commit();
                builder.setTitle("Success");
                builder.setMessage("You registered yourself successfully\nYou will get notifications for " + spinner.getSelectedItem().toString() + " after your registration gets confirmed");
                builder.setIcon(android.R.drawable.ic_dialog_info);
            }
            else if (result.contains("valid")){
                //invalid registration (already registered)
                //Toast.makeText(RegisterFirstActivity.this, "You are already registered to this resort", Toast.LENGTH_LONG);
                builder.setTitle("Already registered");
                builder.setMessage("You registered yourself already");
                builder.setIcon(android.R.drawable.ic_dialog_info);
            }
            else if (result.contains("failed")){
                //less information given than needed
                //should never happen
                Toast.makeText(RegisterFirstActivity.this, "Less info was given to the server", Toast.LENGTH_LONG);
                builder.setTitle("Already registered");
                builder.setMessage("You registered yourself already");
                builder.setIcon(android.R.drawable.ic_dialog_info);
            }
            else if (result.contains("Exception")){
                Toast.makeText(RegisterFirstActivity.this, "Please go online", Toast.LENGTH_LONG);
                builder.setTitle("Connection Error");
                builder.setMessage("Please make sure you're connected to the internet and try again!");
                builder.setIcon(android.R.drawable.ic_dialog_info);
            }
            else{
                Toast.makeText(RegisterFirstActivity.this, "Connection timeout! Please make sure your device has got access to internet", Toast.LENGTH_SHORT).show();
            }
            final String stringResult = result;
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (stringResult.contains(("succesfully")) || stringResult.contains("valid")){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
            builder.show();
        }
    }
}
