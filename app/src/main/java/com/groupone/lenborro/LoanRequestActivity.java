package com.groupone.lenborro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
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


public class LoanRequestActivity extends Activity {


    TextView loanOfferIDView;
    TextView loanAmountView;
    TextView interestRateView;
    TextView repaymentDurationView;
    TextView totalRepaymentAmountView;
    TextView monthlyPaymentView;


    TextView FirstNameView;
    TextView LastNameView;
    TextView DateOfBirthView;
    TextView EmailView;
    TextView PhoneNumberView;
    TextView SSNView;
    TextView AddressView;
    TextView BankNameView;
    TextView IBANView;


    String []dataValues = new String[10];
    int counter = 0;


    String lenborroCategory = null;

    String ME = null;
    String loanOfferID = null;
    String loanAmount = null;
    String interestRate = null;
    String repaymentDuration = null;
    String totalRepaymentAmount = null;
    String monthlyPayment = null;
    String Email = null;
    String loanStatus = null;

    String FirstName = null;
    String LastName = null;
    String DateOfBirth = null;
    String PhoneNumber = null;
    String SSN = null;
    String Address = null;
    String BankName = null;
    String IBAN = null;

    String borrowerEmail = null;
    String lenderEmail = null;



    TextView TextStatusView;

    Button approveButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_request_activity);

        LoadData();

        Intent intent = getIntent();

        loanOfferID = intent.getStringExtra("LoanOfferID");
        loanAmount  = intent.getStringExtra("LoanAmount");
        interestRate = intent.getStringExtra("InterestRate");
        repaymentDuration = intent.getStringExtra("RepaymentDuration");
        totalRepaymentAmount = intent.getStringExtra("TotalRepaymentAmount");
        monthlyPayment = intent.getStringExtra("MonthlyPayment");
        Email = intent.getStringExtra("Email");
        loanStatus = intent.getStringExtra("LoanStatus");


        loanOfferIDView = (TextView) findViewById(R.id.LoanOfferID);
        loanAmountView = (TextView) findViewById(R.id.LoanAmountID);
        interestRateView = (TextView) findViewById(R.id.InterestRateID);
        repaymentDurationView = (TextView) findViewById(R.id.RepaymentDurationID);
        totalRepaymentAmountView = (TextView) findViewById(R.id.TotalRepaymentAmountID);
        monthlyPaymentView = (TextView) findViewById(R.id.MonthlyPaymentID);


        FirstNameView = (TextView) findViewById(R.id.FirstNameID);
        LastNameView  = (TextView) findViewById(R.id.LastNameID);
        DateOfBirthView = (TextView) findViewById(R.id.DateOfBirthID);
        EmailView = (TextView) findViewById(R.id.EmailID);
        PhoneNumberView = (TextView) findViewById(R.id.PhoneNumberID);
        SSNView = (TextView) findViewById(R.id.SSNID);
        AddressView = (TextView) findViewById(R.id.AddressID);
        BankNameView = (TextView) findViewById(R.id.BankNameID);
        IBANView = (TextView) findViewById(R.id.IBANID);

        TextStatusView = (TextView) findViewById(R.id.TextStatusID);

        approveButton = (Button) findViewById(R.id.ApproveButtonID);



        loanOfferIDView.setText("Loan Offer ID: " + loanOfferID);
        loanAmountView.setText("Loan Amount: " + loanAmount + " EUR");
        interestRateView.setText("Interest Rate: " + interestRate + " %");
        repaymentDurationView.setText("Repayment Duration: " + repaymentDuration + "months");
        totalRepaymentAmountView.setText("Total Repayment Amount: " + totalRepaymentAmount + " EUR");
        monthlyPaymentView.setText("Monthly Payment: " + monthlyPayment + " EUR");


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




        if (lenborroCategory.equals("Lender"))
        {
            lenderEmail = ME;
            borrowerEmail = Email;
            new FetchDetailsFromServer().execute("http://www.voltbuy.com/Lenborro/UsersCredientals/Borrower/Credientals.txt");
        }
        else if (lenborroCategory.equals("Borrower"))
        {
            borrowerEmail = ME;
            lenderEmail = Email;
            new FetchDetailsFromServer().execute("http://www.voltbuy.com/Lenborro/UsersCredientals/Lender/Credientals.txt");
        }


    }





    private class FetchDetailsFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return FetchDetails(urls[0]);
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


                FirstName = dataValues[0];
                LastName = dataValues[1];
                DateOfBirth = dataValues[2];
                PhoneNumber = dataValues[5];
                SSN = dataValues[6];
                Address = dataValues[7];
                BankName = dataValues[8];
                IBAN = dataValues[9];


                FirstNameView.setText("First Name: " + FirstName);
                LastNameView.setText("Last Name: " + LastName);
                DateOfBirthView.setText("Date of Birth: " + DateOfBirth);
                EmailView.setText("Email: " + Email);
                PhoneNumberView.setText("Contact No: " + PhoneNumber);;
                SSNView.setText("Social Security Number: " + SSN);
                AddressView.setText("Address: " + Address);
                BankNameView.setText("Bank Name: " + BankName);
                IBANView.setText("IBAN: " + IBAN);

                if (lenborroCategory.equals("Lender"))
                {
                    approveButton.setVisibility(View.VISIBLE);
                    TextStatusView.setText("Dear Lender, This offer is waiting for your approval.");
                }
                else if(lenborroCategory.equals("Borrower"))
                {
                    TextStatusView.setText("Dear Borrower, We are waiting for Lender's approval.");
                }


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

    private String FetchDetails(String myurl) throws IOException, UnsupportedEncodingException {
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



                if (Email.equals(dataValues[3]))
                {
                    FirstName = dataValues[0];
                    LastName = dataValues[1];
                    DateOfBirth = dataValues[2];
                    PhoneNumber = dataValues[5];
                    SSN = dataValues[6];
                    Address = dataValues[7];
                    BankName = dataValues[8];
                    IBAN = dataValues[9];

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


    public void LoadData()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        ME = prefs.getString("LenborroME", null);
        lenborroCategory = prefs.getString("LenborroCategory", null);


    }

    public void ApproveButtonClicked(View v)
    {

        new DeletePendingLoanRequestFromServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/DeletePendingLoanRequest.php");


    }




    private class DeletePendingLoanRequestFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return DeletePendingLoanRequest(urls[0]);
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



                new AddApprovedLoanOfferToServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/AddApprovedLoanOffer.php");


            } else {


                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private String DeletePendingLoanRequest(String myurl) throws IOException, UnsupportedEncodingException {

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





    private class AddApprovedLoanOfferToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return ApprovedLoanOffer(urls[0]);
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

                Intent intent = new Intent(LoanRequestActivity.this , ApprovedLoanOffersListActivity.class);
                startActivity(intent);
                finish();


            } else {


                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private String ApprovedLoanOffer(String myurl) throws IOException, UnsupportedEncodingException {

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
                    .appendQueryParameter("TotalRepaymentAmount", totalRepaymentAmount)
                    .appendQueryParameter("MonthlyPayment", monthlyPayment)
                    .appendQueryParameter("BorrowerEmail", borrowerEmail)
                    .appendQueryParameter("LenderEmail", lenderEmail)
                    .appendQueryParameter("Status", "Approved");

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
