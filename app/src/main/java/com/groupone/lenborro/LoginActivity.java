package com.groupone.lenborro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends Activity {


    String lenborroCategory = "";



    EditText EmailField;
    EditText PasswordField;

    String Email = null;
    String Password = null;

    String []dataValues = new String[10];
    int counter = 0;

    boolean userValidated = false;

    Bitmap photo_ME = null;

    Button loginButton;
    Button signUpButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

            //LoadLenborroCategory();

        dataValues[0] = "";
        dataValues[1] = "";
        dataValues[2] = "";
        dataValues[3] = "";
        dataValues[4] = "";
        dataValues[5] = "";
        dataValues[6] = "";
        dataValues[7] = "";
        dataValues[8] = "";
        dataValues[9] = "";

        counter = 0;


        EmailField = (EditText) findViewById(R.id.EmailID);
        PasswordField = (EditText) findViewById(R.id.PasswordID);

        loginButton = (Button) findViewById(R.id.loginButtonID);
        signUpButton = (Button) findViewById(R.id.signUpButtonID);




    }




    public void LoadLenborroCategory()
    {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        lenborroCategory = prefs.getString("LenborroCategory", null);




    }







    public void LoginButtonClicked(View v)
    {

        Email = EmailField.getText().toString();
        Password = PasswordField.getText().toString();

        Email = Email.replace(" ", "");



        if(isEmailValid(Email) && Password.length() > 0 && lenborroCategory.length() > 0)
        {

            new ValidateLoginDataOnServer().execute("http://www.voltbuy.com/Lenborro/UsersCredientals/" + lenborroCategory + "/Credientals.txt");
        }
        else
        {
            Toast.makeText(this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }


    }




    public void SignUpButtonClicked(View v)
    {

        loginButton.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);


        Intent intent = new Intent (LoginActivity.this , SignUpActivity.class);
        startActivity(intent);
        finish();

    }






    private class ValidateLoginDataOnServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return ValidateLoginData(urls[0]);
            } catch (IOException e) {
                return "NotFound";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loginButton.setVisibility(View.GONE);
            signUpButton.setVisibility(View.GONE);

            dataValues[0] = "";
            dataValues[1] = "";
            dataValues[2] = "";
            dataValues[3] = "";
            dataValues[4] = "";
            dataValues[5] = "";
            dataValues[6] = "";
            dataValues[7] = "";
            dataValues[8] = "";
            dataValues[9] = "";

            counter = 0;

            userValidated = false;



        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            if (result.equals("OK")) {

                if(userValidated)
                {

                    SaveData();

                    if(lenborroCategory.equals("Lender")) {

                        Intent intent = new Intent(LoginActivity.this, LenderActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(lenborroCategory.equals("Borrower"))
                    {
                        Intent intent = new Intent(LoginActivity.this, BorrowerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(lenborroCategory.equals("Admin"))
                    {
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }






                }
                else
                {

                    loginButton.setVisibility(View.VISIBLE);
                    signUpButton.setVisibility(View.VISIBLE);


                    Toast.makeText(getApplicationContext(), "Incorrect Login Details", Toast.LENGTH_SHORT).show();


                }

            }

            else if (result.equals("NotFound"))
            {

                loginButton.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "Account not found", Toast.LENGTH_SHORT).show();

            }
            else
            {
                loginButton.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();

            }



        }
    }

    private String ValidateLoginData(String myurl) throws IOException, UnsupportedEncodingException {
        InputStream is = null;


        // Only display the first 500 characters of the retrieved
        // web page content.


        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();




            is = conn.getInputStream();

            BufferedReader textReader = new BufferedReader(new InputStreamReader(is));


            String readlineText;

            userValidated = false;


            while ((readlineText = textReader.readLine()) != null) {

                for (int i = 0 ; i < readlineText.length() ; ++i)
                {
                    if (readlineText.charAt(i) == '|') {
                        ++counter;

                        continue;
                    }

                    dataValues[counter] = (dataValues[counter] + readlineText.charAt(i));
                }

                if (dataValues[3].equals(Email) && dataValues[4].equals(Password))
                {
                    userValidated = true;
                    break;
                }


                counter = 0;

                dataValues[0] = "";
                dataValues[1] = "";
                dataValues[2] = "";
                dataValues[3] = "";
                dataValues[4] = "";
                dataValues[5] = "";
                dataValues[6] = "";
                dataValues[7] = "";
                dataValues[8] = "";
                dataValues[9] = "";


            }




            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                conn.disconnect();
                return "OK";

            }
            else
            {
                conn.disconnect();
                return "NetworkError";
            }




            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {


            if (is != null)
            {
                is.close();


            }

        }
    }



    public void SaveData()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LenborroME", Email);
        editor.putString("LenborroCategory", lenborroCategory);
        editor.apply();
    }


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



    public void LenderRadioButtonClicked(View v)
    {
        lenborroCategory = "Lender";
    }

    public void BorrowerRadioButtonClicked(View v)
    {
        lenborroCategory = "Borrower";
    }

    public void AdminRadioButtonClicked(View v)
    {
        lenborroCategory = "Admin";
    }


}
