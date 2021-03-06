package com.acharya.habba2k18.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acharya.habba2k18.Events.HttpHandler;
import com.acharya.habba2k18.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_LONG;


public class Registration extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPhone;
    private EditText editTextEmail;
    Spinner s1, s2;
    TextView textView, amountTextview;
    private static String url = null;
    private static String url1 = null;

    private Button buttonRegister;

    private static final String REGISTER_URL = "http://theprince.96.lt//android/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        url = "http://acharyahabba.in/habba18/events.php";
        spinnerlist = new ArrayList<>();
        new GetContacts().execute();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPhone = (EditText) findViewById(R.id.editPhone);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        s1 = (Spinner) findViewById(R.id.spinner);
        s2 = (Spinner) findViewById(R.id.spinner2);
        textView = (TextView) findViewById(R.id.textView);
        amountTextview = (TextView)findViewById(R.id.amount);


        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister) {
            registerUser();
        }
    }

    private void registerUser() {

        String name = editTextName.getText().toString().trim().toLowerCase();
        if (editTextName.getText().toString().trim().equals("")) {
            editTextName.setError("First name is required!");

            editTextName.setHint("Please enter your name");
        }
        String clg = editTextUsername.getText().toString().trim().toLowerCase();

        if (editTextUsername.getText().toString().trim().equals("")) {
            editTextUsername.setError("College name is required!");

           // editTextUsername.setHint("Please enter your College's name");
        }
        String number = editTextPhone.getText().toString().trim().toLowerCase();

        if (editTextPhone.getText().toString().trim().equals("")) {
            editTextPhone.setError("Phone number is required!");

           // editTextPhone.setHint("Please enter your phone number");
        }

        String email = editTextEmail.getText().toString().trim().toLowerCase();

        if (editTextEmail.getText().toString().trim().equals("")) {
            editTextEmail.setError("Email address is required!");

            editTextEmail.setHint("Please enter your email address");
        }
        String ctg = s1.getSelectedItem().toString();
        String sctg = s2.getSelectedItem().toString();

        register(name, clg, number, email, ctg, sctg);
    }

    private void register(String name, String clg, String number, String email, String ctg, String sctg) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Registration.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("name", params[0]);
                data.put("clg", params[1]);
                data.put("number", params[2]);
                data.put("email", params[3]);
                data.put("ctg", params[4]);
                data.put("sctg", params[5]);

                String result = ruc.sendPostRequest(REGISTER_URL, data);

                return result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name, clg, number, email, ctg, sctg);
    }

    private String TAG = Registration.class.getSimpleName();

    // URL to get contacts JSON

    public static ArrayList<String> spinnerlist;


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray contacts = jsonObj.getJSONArray("result");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String name = c.getString("name");

                        spinnerlist.add(name);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(Registration.this,android.R.layout.simple_spinner_dropdown_item, spinnerlist);
                    s1.setAdapter(spinnerAdapter);

                   s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                       @Override
                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                           String main = s1.getSelectedItem().toString();
                           url1 = "http://acharyahabba.in/habba18/subevents.php?main=" + main;
                           System.out.println("spinner"+url1);
                           subspinnerlist = new ArrayList<>();
                           new GetCategory().execute();

                       }

                       @Override
                       public void onNothingSelected(AdapterView<?> parent) {

                       }
                   });
                    s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                           s2.setSelection(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            });
        }


    }

    public static ArrayList<String> subspinnerlist;


    private class GetCategory extends AsyncTask<Void, Void, Void> {
        String amount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url1);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray contacts = jsonObj.getJSONArray("result1");
                    for(int i = 0; i<contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String name = c.getString("name");
                         amount = c.getString("amount");

                        subspinnerlist.add(name);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ArrayAdapter<String> subspinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item , subspinnerlist);
                    s2.setAdapter(subspinnerAdapter);
                    amountTextview.setText(amount);


                }
            });
        }


    }

}