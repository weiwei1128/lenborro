package com.groupone.lenborro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by user on 12/17/2016.
 */

public class AdminActivity extends Activity implements UsersListAdapter.DeleteUserInterface {

    AlertDialog alertDw;
    AlertDialog.Builder builder;


    String ME = "";
    String lenborroCategory = null;


    private ListView listView;
    private UsersListAdapter adapter;

    private ArrayList<String> NamesList;
    private ArrayList<String> EmailsList;
    private ArrayList<String> PhoneNumbersList;


    String []dataValues = new String[10];
    int counter = 0;



    String Email = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        LoadData();



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


        NamesList = new ArrayList<>();
        EmailsList = new ArrayList<>();
        PhoneNumbersList = new ArrayList<>();





        listView = (ListView) findViewById(R.id.listView);




        adapter = new UsersListAdapter(this, NamesList, EmailsList, PhoneNumbersList);
        adapter.setCallbackForOfferDeletion(this);
        listView.setAdapter(adapter);







        NamesList.clear();
        EmailsList.clear();
        PhoneNumbersList.clear();


        new FetchUsersListFromServer().execute("http://www.voltbuy.com/Lenborro/UsersCredientals/Borrower/Credientals.txt");




    }

    public void onRefresh() {

        LoadData();



        NamesList.clear();
        EmailsList.clear();
        PhoneNumbersList.clear();


        new FetchUsersListFromServer().execute("http://www.voltbuy.com/Lenborro/UsersCredientals/Borrower/Credientals.txt");


    }


    public void LoadData()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        ME = prefs.getString("LenborroME", null);




    }




    private class FetchUsersListFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return FetchUsersList(urls[0]);
            } catch (IOException e) {
                return "NotFound";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            listView.setEnabled(false);

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


            NamesList.clear();
            EmailsList.clear();
            PhoneNumbersList.clear();


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {



            listView.setEnabled(true);


            if (result.equals("OK")) {





                new Fetchyyy().execute("http://www.voltbuy.com/Lenborro/UsersCredientals/Lender/Credientals.txt");




            }


            else if (result.equals("NotFound"))

            {
                Toast.makeText(getApplicationContext(), "No Loan Offers found", Toast.LENGTH_SHORT).show();



            }

            else
            {
                Toast.makeText(getApplicationContext(), "Network Connection Problem. Make sure that your internet is properly connected", Toast.LENGTH_SHORT).show();


            }






        }
    }

    private String FetchUsersList(String myurl) throws IOException, UnsupportedEncodingException {
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


            while ((readlineText = textReader.readLine()) != null) {



                if(readlineText.length() <= 0)
                {
                    continue;
                }



                for (int i = 0 ; i < readlineText.length() ; ++i)
                {
                    if (readlineText.charAt(i) == '|')
                    {
                        ++counter;

                        continue;
                    }

                    dataValues[counter] = (dataValues[counter] + readlineText.charAt(i));
                }



                    NamesList.add(dataValues[0] + " " + dataValues[1]);
                    EmailsList.add(dataValues[3]);
                    PhoneNumbersList.add(dataValues[5]);



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







    private class Fetchyyy extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return Fetchy(urls[0]);
            } catch (IOException e) {
                return "NotFound";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            listView.setEnabled(false);

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


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {



            listView.setEnabled(true);


            if (result.equals("OK")) {



                listView.post(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                        listView.smoothScrollToPosition(adapter.getCount() - 1);


                    }
                });







            }


            else if (result.equals("NotFound"))

            {
                Toast.makeText(getApplicationContext(), "No Loan Offers found", Toast.LENGTH_SHORT).show();



            }

            else
            {
                Toast.makeText(getApplicationContext(), "Network Connection Problem. Make sure that your internet is properly connected", Toast.LENGTH_SHORT).show();


            }






        }
    }

    private String Fetchy(String myurl) throws IOException, UnsupportedEncodingException {
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


            while ((readlineText = textReader.readLine()) != null) {



                if(readlineText.length() <= 0)
                {
                    continue;
                }



                for (int i = 0 ; i < readlineText.length() ; ++i)
                {
                    if (readlineText.charAt(i) == '|')
                    {
                        ++counter;

                        continue;
                    }

                    dataValues[counter] = (dataValues[counter] + readlineText.charAt(i));
                }



                if(!EmailsList.contains(dataValues[3])) {
                    NamesList.add(dataValues[0] + " " + dataValues[1]);
                    EmailsList.add(dataValues[3]);
                    PhoneNumbersList.add(dataValues[5]);
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














    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { //Back key pressed



            return true;

        }
        return super.onKeyDown(keyCode, event);
    }







    @Override
    public void DeleteUser(int position) {

        Email = EmailsList.get(position);

        Toast.makeText(this, "hahhaha", Toast.LENGTH_SHORT).show();

        new DeleteUserFromServer().execute("http://www.voltbuy.com/Lenborro/UsersCredientals/DeleteUser.php");


    }





    private class DeleteUserFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return DeleteUser(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";

            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();




        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            if (result.equals("OK")) {


                new FetchUsersListFromServer().execute("http://www.voltbuy.com/Lenborro/UsersCredientals/Borrower/Credientals.txt");


            } else {


                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private String DeleteUser(String myurl) throws IOException, UnsupportedEncodingException {

        OutputStream os = null;


        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // Starts the query
            conn.connect();


            os = conn.getOutputStream();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Email", Email);

            String query = builder.build().getEncodedQuery();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();

            // Convert the InputStream into a string
            // String contentAsString = readIt(is, len);


            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return "OK";
            } else {
                return "NetworkError";
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {

            if (os != null) {
                os.close();

            }

        }
    }


    public void PendingRequestsButtonClicked(View v)
    {
        Intent intent = new Intent(AdminActivity.this , PendingLoanRequestListActivity.class);
        startActivity(intent);
        finish();
    }

    public void ApprovedOffersButtonClicked(View v)
    {
        Intent intent = new Intent(AdminActivity.this , ApprovedLoanOffersListActivity.class);
        startActivity(intent);
        finish();
    }




}

