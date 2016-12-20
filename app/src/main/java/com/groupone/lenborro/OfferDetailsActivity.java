package com.groupone.lenborro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
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


public class OfferDetailsActivity extends Activity {

    String YOU = null;
    String ME = null;
    String loanOfferID = null;
    String loanAmount = null;
    String interestRate = null;
    String repaymentDuration = null;

    String lenderFirstName = null;
    String lenderLastName = null;
    String lenderDateOfBirth = null;
    String lenderPhoneNumber = null;
    String lenderSSN = null;
    String lenderAddress = null;
    String lenderBankName = null;
    String lenderIBAN = null;


    TextView loanOfferIDView;
    TextView loanAmountView;
    TextView interestRateView;
    TextView repaymentDurationView;
    TextView totalRepaymentAmountView;
    TextView monthlyPaymentView;


    TextView lenderFirstNameView;
    TextView lenderLastNameView;
    TextView lenderDateOfBirthView;
    TextView lenderEmailView;
    TextView lenderPhoneNumberView;
    TextView lenderSSNView;
    TextView lenderAddressView;
    TextView lenderBankNameView;
    TextView lenderIBANView;


    double loan_amount;
    double interest_rate;
    double repayment_duration;
    double total_repayment_amount;
    double monthly_payment;

    String []dataValues = new String[10];
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_details_activity);

        LoadData();

        Intent intent = getIntent();

        YOU = intent.getStringExtra("YOU");
        loanOfferID = intent.getStringExtra("LoanOfferID");
        loanAmount = intent.getStringExtra("LoanAmount");
        interestRate = intent.getStringExtra("InterestRate");
        repaymentDuration = intent.getStringExtra("RepaymentDuration");

        loan_amount = Double.valueOf(loanAmount);
        interest_rate = Double.valueOf(interestRate);
        repayment_duration = Double.valueOf(repaymentDuration);
        total_repayment_amount = (loan_amount) + (loan_amount*(interest_rate/100.0f));
        monthly_payment = (total_repayment_amount/repayment_duration);


        loanOfferIDView = (TextView) findViewById(R.id.LoanOfferID);
        loanAmountView = (TextView) findViewById(R.id.LoanAmountID);
        interestRateView = (TextView) findViewById(R.id.InterestRateID);
        repaymentDurationView = (TextView) findViewById(R.id.RepaymentDurationID);
        totalRepaymentAmountView = (TextView) findViewById(R.id.TotalRepaymentAmountID);
        monthlyPaymentView = (TextView) findViewById(R.id.MonthlyPaymentID);


        lenderFirstNameView = (TextView) findViewById(R.id.LenderFirstNameID);
        lenderLastNameView  = (TextView) findViewById(R.id.LenderLastNameID);
        lenderDateOfBirthView = (TextView) findViewById(R.id.LenderDateOfBirthID);
        lenderEmailView = (TextView) findViewById(R.id.LenderEmailID);
        lenderPhoneNumberView = (TextView) findViewById(R.id.LenderPhoneNumberID);
        lenderSSNView = (TextView) findViewById(R.id.LenderSSNID);
        lenderAddressView = (TextView) findViewById(R.id.LenderAddressID);
        lenderBankNameView = (TextView) findViewById(R.id.LenderBankNameID);
        lenderIBANView = (TextView) findViewById(R.id.LenderIBANID);


        loanOfferIDView.setText("Loan Offer ID: " + loanOfferID);
        loanAmountView.setText("Loan Amount: " + loanAmount + " EUR");
        interestRateView.setText("Interest Rate: " + interestRate + " %");
        repaymentDurationView.setText("Repayment Duration: " + repaymentDuration + "months");
        totalRepaymentAmountView.setText("Total Repayment Amount: " +total_repayment_amount + " EUR");
        monthlyPaymentView.setText("Monthly Payment: " + monthly_payment + " EUR");



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


        new FetchLenderDetailsFromServer().execute("http://www.voltbuy.com/Lenborro/UsersCredientals/Lender/Credientals.txt");




    }


    public void LoadData()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        ME = prefs.getString("LenborroME", null);


    }


    public void ApplyButtonClicked(View v)
    {

        new DeleteLoanOfferFromServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/DeleteLoanOffer.php");


    }


    private class FetchLenderDetailsFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return FetchLenderDetails(urls[0]);
            } catch (IOException e) {
                return "NotFound";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



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


            if (result.equals("OK")) {



                lenderFirstNameView.setText("First Name: " + lenderFirstName);
                lenderLastNameView.setText("Last Name: " + lenderLastName);
                lenderDateOfBirthView.setText("Date of Birth: " + lenderDateOfBirth);
                lenderEmailView.setText("Email: " + YOU);
                lenderPhoneNumberView.setText("Contact No: " + lenderPhoneNumber);;
                lenderSSNView.setText("Social Security Number: " + lenderSSN);
                lenderAddressView.setText("Address: " + lenderAddress);
                lenderBankNameView.setText("Bank Name: " + lenderBankName);
                lenderIBANView.setText("IBAN: " + lenderIBAN);




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

    private String FetchLenderDetails(String myurl) throws IOException, UnsupportedEncodingException {
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



                if (YOU.equals(dataValues[3]))
                {
                    lenderFirstName = dataValues[0];
                    lenderLastName = dataValues[1];
                    lenderDateOfBirth = dataValues[2];
                    lenderPhoneNumber = dataValues[5];
                    lenderSSN = dataValues[6];
                    lenderAddress = dataValues[7];
                    lenderBankName = dataValues[8];
                    lenderIBAN = dataValues[9];

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



                new AddPendingLoanRequestToServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/AddPendingLoanRequest.php");


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
                    .appendQueryParameter("LoanOfferID", loanOfferID);

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




    private class AddPendingLoanRequestToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return PendingLoanRequest(urls[0]);
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

                Intent intent = new Intent(OfferDetailsActivity.this , PendingLoanRequestListActivity.class);
                startActivity(intent);
                finish();


            } else {


                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private String PendingLoanRequest(String myurl) throws IOException, UnsupportedEncodingException {

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
                    .appendQueryParameter("LoanOfferID", loanOfferID)
                    .appendQueryParameter("LoanAmount", loanAmount)
                    .appendQueryParameter("InterestRate", interestRate)
                    .appendQueryParameter("RepaymentDuration", repaymentDuration)
                    .appendQueryParameter("TotalRepaymentAmount", String.valueOf(total_repayment_amount))
                    .appendQueryParameter("MonthlyPayment", String.valueOf(monthly_payment))
                    .appendQueryParameter("BorrowerEmail", ME)
                    .appendQueryParameter("LenderEmail", YOU)
                    .appendQueryParameter("Status", "APPLIED");

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





}
