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
import android.widget.Button;
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


public class LenderActivity extends Activity implements LenderOffersListAdapter.DeleteOfferInterface {

    AlertDialog alertDw;
    AlertDialog.Builder builder;


    String ME = "";
    String lenborroCategory = null;


    private ListView listView;
    private LenderOffersListAdapter adapter;

    private ArrayList<String> LoanOffersIDs;
    private ArrayList<String> EmailIDs;
    private ArrayList<String> LoanAmountList;

    private ArrayList<String> InterestRateList;

    private ArrayList<String> RepaymentDurationList;



    String []dataValues = new String[5];
    int counter = 0;


    String LoanOfferID = null;
    String loanAmount = null;
    String interestRate = null;
    String repaymentDuration = null;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lender_activity);

        LoadData();



        dataValues[0] = "";
        dataValues[1] = "";
        dataValues[2] = "";
        dataValues[3] = "";
        dataValues[4] = "";

        EmailIDs = new ArrayList<>();
        LoanOffersIDs = new ArrayList<>();
        LoanAmountList = new ArrayList<>();
        InterestRateList = new ArrayList<>();
        RepaymentDurationList = new ArrayList<>();




        listView = (ListView) findViewById(R.id.listView);




        adapter = new LenderOffersListAdapter(this, LoanAmountList, InterestRateList, RepaymentDurationList);
        adapter.setCallbackForOfferDeletion(this);
        listView.setAdapter(adapter);







        LoanOffersIDs.clear();
        EmailIDs.clear();
        LoanAmountList.clear();
        InterestRateList.clear();
        RepaymentDurationList.clear();



        new FetchMyLoanOffersFromServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/LoanOffersWall/ActiveLoanOffers.txt");




    }

    public void onRefresh() {

        LoadData();



        LoanOffersIDs.clear();
        EmailIDs.clear();
        LoanAmountList.clear();
        InterestRateList.clear();
        RepaymentDurationList.clear();


        new FetchMyLoanOffersFromServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/PrivateSection/ActiveLoanOffers-" + ME + ".txt");


    }


    public void LoadData()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        ME = prefs.getString("LenborroME", null);




    }




    private class FetchMyLoanOffersFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return MyLoanOffers(urls[0]);
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

            counter = 0;


            LoanOffersIDs.clear();
            EmailIDs.clear();
            LoanAmountList.clear();
            InterestRateList.clear();
            RepaymentDurationList.clear();



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

    private String MyLoanOffers(String myurl) throws IOException, UnsupportedEncodingException {
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


                if (ME.equals(dataValues[1]))
                {
                    LoanOffersIDs.add(dataValues[0]);
                    EmailIDs.add(dataValues[1]);
                    LoanAmountList.add(dataValues[2]);
                    InterestRateList.add(dataValues[3]);
                    RepaymentDurationList.add(dataValues[4]);
                }





                counter = 0;

                dataValues[0] = "";
                dataValues[1] = "";
                dataValues[2] = "";
                dataValues[3] = "";
                dataValues[4] = "";







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



    public void CreateOfferButtonClicked(View v)
    {

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.create_new_loan_offer, null);

        final EditText LoanAmountEditText = (EditText) dialoglayout.findViewById(R.id.LoanAmountID);
        final EditText InterestRateEditText = (EditText) dialoglayout.findViewById(R.id.InterestRateID);
        final EditText RepaymentDurationEditText = (EditText) dialoglayout.findViewById(R.id.RepaymentDurationID);


        builder = new AlertDialog.Builder(this);

        builder.setView(dialoglayout);
        builder.setTitle("Create New Loan Offer");
        builder.setMessage("Enter the loan details that you want to offer to potential borrowers.");
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                loanAmount = LoanAmountEditText.getText().toString();
                interestRate = InterestRateEditText.getText().toString();
                repaymentDuration = RepaymentDurationEditText.getText().toString();


                loanAmount = loanAmount.replace(" ", "");
                interestRate = interestRate.replace(" ", "");
                repaymentDuration = repaymentDuration.replace(" ", "");

                try {
                    long loanAmount_int = Long.valueOf(loanAmount);
                    long interestRate_int = Long.valueOf(interestRate);
                    long repaymentDuration_int = Long.valueOf(repaymentDuration);

                    double loanAmount_double = (double) loanAmount_int;
                    double interestRate_double = (double) interestRate_int;
                    double repaymentDuration_double = (double) repaymentDuration_int;


                    loanAmount = String.valueOf(loanAmount_double);
                    interestRate = String.valueOf(interestRate_double);
                    repaymentDuration = String.valueOf(repaymentDuration_double);
                }
                catch (Exception e)
                {

                }
                finally {

                    if (loanAmount.length() > 0 && interestRate.length() > 0  && repaymentDuration.length() > 0)
                    {
                        LoanOfferID = GenerateRandomLoanOfferID();

                        new AddNewLoanOfferToServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/AddNewLoanOffer.php");

                    }

                }





            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(ClientActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });


        alertDw = builder.create();


        alertDw.show();




    }





    private class AddNewLoanOfferToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return NewLoanOffer(urls[0]);
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


                new FetchMyLoanOffersFromServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/LoanOffersWall/ActiveLoanOffers.txt");


            } else {


                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private String NewLoanOffer(String myurl) throws IOException, UnsupportedEncodingException {

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
                    .appendQueryParameter("LoanOfferID", LoanOfferID)
                    .appendQueryParameter("LoanAmount", loanAmount)
                    .appendQueryParameter("InterestRate", interestRate)
                    .appendQueryParameter("RepaymentDuration", repaymentDuration)
                    .appendQueryParameter("Email", ME);

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


    public String GenerateRandomLoanOfferID()
    {

        Random random = new Random();
        long randomNumber = random.nextInt(999999999);

        NumberFormat numberFormat = new DecimalFormat("000000000");

        String randomID = numberFormat.format(randomNumber);

        return randomID;
    }

    @Override
    public void DeleteOffer(int position) {

        LoanOfferID = LoanOffersIDs.get(position);


        new DeleteLoanOfferFromServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/DeleteLoanOffer.php");


    }





    private class DeleteLoanOfferFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return DeleteLoanOffer(urls[0]);
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


                new FetchMyLoanOffersFromServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/LoanOffersWall/ActiveLoanOffers.txt");


            } else {


                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private String DeleteLoanOffer(String myurl) throws IOException, UnsupportedEncodingException {

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
                    .appendQueryParameter("LoanOfferID", LoanOfferID);

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
        Intent intent = new Intent(LenderActivity.this , PendingLoanRequestListActivity.class);
        startActivity(intent);
        finish();
    }

    public void ApprovedOffersButtonClicked(View v)
    {
        Intent intent = new Intent(LenderActivity.this , ApprovedLoanOffersListActivity.class);
        startActivity(intent);
        finish();
    }




}

