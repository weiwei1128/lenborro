package com.groupone.lenborro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.ArrayList;


public class PendingLoanRequestListActivity extends Activity {

    String ME = "";


    private ListView listView;
    private BorrowerOffersListAdapter adapter;

    private ArrayList<String> LoanOffersIDs;
    private ArrayList<String> LoanAmountList;
    private ArrayList<String> InterestRateList;
    private ArrayList<String> RepaymentDurationList;
    private ArrayList<String> TotalRepaymentAmountList;
    private ArrayList<String> MonthlyPaymentList;

    private ArrayList<String> EmailsList;

    private ArrayList<String> LoanStatusList;



    String []dataValues = new String[9];
    int counter = 0;


    String LoanOfferID = null;
    String loanAmount = null;
    String interestRate = null;
    String repaymentDuration = null;




    String BorrowerEmail;
    String LenderEmail;

    String lenborroCategory = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_loan_request_list_activity);

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

        LoanOffersIDs = new ArrayList<>();
        LoanAmountList = new ArrayList<>();
        InterestRateList = new ArrayList<>();
        RepaymentDurationList = new ArrayList<>();
        TotalRepaymentAmountList = new ArrayList<>();
        MonthlyPaymentList = new ArrayList<>();
        EmailsList = new ArrayList<>();
        LoanStatusList = new ArrayList<>();



        listView = (ListView) findViewById(R.id.listView);



        adapter = new BorrowerOffersListAdapter(this, LoanAmountList, InterestRateList, RepaymentDurationList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PendingLoanRequestListActivity.this, LoanRequestActivity.class);

                intent.putExtra("LoanOfferID", LoanOffersIDs.get(position));
                intent.putExtra("LoanAmount", LoanAmountList.get(position));
                intent.putExtra("InterestRate", InterestRateList.get(position));
                intent.putExtra("RepaymentDuration", RepaymentDurationList.get(position));
                intent.putExtra("TotalRepaymentAmount", TotalRepaymentAmountList.get(position));
                intent.putExtra("MonthlyPayment", MonthlyPaymentList.get(position));
                intent.putExtra("Email", EmailsList.get(position));
                intent.putExtra("LoanStatus", LoanStatusList.get(position));

                startActivity(intent);
                finish();

            }
        });



        LoanOffersIDs.clear();
        LoanAmountList.clear();
        InterestRateList.clear();
        RepaymentDurationList.clear();
        TotalRepaymentAmountList.clear();
        MonthlyPaymentList.clear();
        EmailsList.clear();
        LoanStatusList.clear();



        new FetchPendingLoanRequestFromServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/PendingLoanRequests/PendingLoanRequests.txt");




    }

    public void onRefresh() {

        LoadData();


        LoanOffersIDs.clear();
        LoanAmountList.clear();
        InterestRateList.clear();
        RepaymentDurationList.clear();
        TotalRepaymentAmountList.clear();
        MonthlyPaymentList.clear();
        EmailsList.clear();
        LoanStatusList.clear();


        new FetchPendingLoanRequestFromServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/PendingLoanRequests/PendingLoanRequests.txt");



    }



    public void LoadData()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        ME = prefs.getString("LenborroME", null);
        lenborroCategory = prefs.getString("LenborroCategory", null);


    }




    private class FetchPendingLoanRequestFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return PendingLoanRequest(urls[0]);
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

            counter = 0;


            LoanOffersIDs.clear();
            LoanAmountList.clear();
            InterestRateList.clear();
            RepaymentDurationList.clear();
            TotalRepaymentAmountList.clear();
            MonthlyPaymentList.clear();
            EmailsList.clear();
            LoanStatusList.clear();


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

    private String PendingLoanRequest(String myurl) throws IOException, UnsupportedEncodingException {
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

                if(lenborroCategory.equals("Borrower")) {

                    if (ME.equals(dataValues[6])) {


                        LoanOffersIDs.add(dataValues[0]);
                        LoanAmountList.add(dataValues[1]);
                        InterestRateList.add(dataValues[2]);
                        RepaymentDurationList.add(dataValues[3]);
                        TotalRepaymentAmountList.add(dataValues[4]);
                        MonthlyPaymentList.add(dataValues[5]);
                        EmailsList.add(dataValues[7]);
                        LoanStatusList.add(dataValues[8]);
                    }

                }


               else if(lenborroCategory.equals("Lender")) {

                    if (ME.equals(dataValues[7])) {

                        LoanOffersIDs.add(dataValues[0]);
                        LoanAmountList.add(dataValues[1]);
                        InterestRateList.add(dataValues[2]);
                        RepaymentDurationList.add(dataValues[3]);
                        TotalRepaymentAmountList.add(dataValues[4]);
                        MonthlyPaymentList.add(dataValues[5]);
                        EmailsList.add(dataValues[6]);
                        LoanStatusList.add(dataValues[8]);
                    }

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






}

