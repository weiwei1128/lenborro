package com.groupone.lenborro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class BorrowerActivity extends Activity{

    String ME = "";


    private ListView listView;
    private BorrowerOffersListAdapter adapter;

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
        setContentView(R.layout.borrower_activity);

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



        adapter = new BorrowerOffersListAdapter(this, LoanAmountList, InterestRateList, RepaymentDurationList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(BorrowerActivity.this , OfferDetailsActivity.class);
                intent.putExtra("YOU", EmailIDs.get(position));
                intent.putExtra("LoanOfferID", LoanOffersIDs.get(position));
                intent.putExtra("LoanAmount", LoanAmountList.get(position));
                intent.putExtra("InterestRate", InterestRateList.get(position));
                intent.putExtra("RepaymentDuration", RepaymentDurationList.get(position));

                startActivity(intent);
                finish();

            }
        });






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


        new FetchMyLoanOffersFromServer().execute("http://www.voltbuy.com/Lenborro/LoanOffers/LoanOffersWall/ActiveLoanOffers.txt");



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



                    LoanOffersIDs.add(dataValues[0]);
                    EmailIDs.add(dataValues[1]);
                    LoanAmountList.add(dataValues[2]);
                    InterestRateList.add(dataValues[3]);
                    RepaymentDurationList.add(dataValues[4]);






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



    public void PendingRequestsButtonClicked(View v)
    {
        Intent intent = new Intent(BorrowerActivity.this , PendingLoanRequestListActivity.class);
        startActivity(intent);
        finish();
    }

    public void ApprovedOffersButtonClicked(View v)
    {
        Intent intent = new Intent(BorrowerActivity.this , ApprovedLoanOffersListActivity.class);
        startActivity(intent);
        finish();
    }






}

