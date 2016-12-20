package com.groupone.lenborro;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

// This source code is developed by Group D, M.Sc Applied Computer Science (IT-Security) 2016.


/*

This is an adapter class holding the Chat conversation between users that is listed in the PrivateChatActivity class.

 */


public class LenderOffersListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;

    private ArrayList<String> LoanAmountList;

    private ArrayList<String> InterestRateList;

    private ArrayList<String> RepaymentDurationList;

    Typeface font;

    DeleteOfferInterface deleteOfferListener;




    public LenderOffersListAdapter(Activity activity, ArrayList<String> LoanAmountList, ArrayList<String> InterestRateList, ArrayList<String> RepaymentDurationList) {

        this.activity = activity;
        this.LoanAmountList = LoanAmountList;
        this.InterestRateList = InterestRateList;
        this.RepaymentDurationList = RepaymentDurationList;


        /*
               Font type for text
       */

        font = Typeface.createFromAsset(activity.getAssets(), "BakersfieldBold.ttf");



    }

    @Override
    public int getCount() {
        return LoanAmountList.size();
    }

    @Override
    public Object getItem(int location) {
        return LoanAmountList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.lender_offers_list_row, null);


        TextView LoanAmount = (TextView) convertView.findViewById(R.id.LoanAmountID);
        TextView InterestRate = (TextView) convertView.findViewById(R.id.InterestRateID);
        TextView RepaymentDuration = (TextView) convertView.findViewById(R.id.RepaymentDurationID);
        final Button DeleteOfferButton = (Button) convertView.findViewById(R.id.DeleteOfferButtonID);



        LoanAmount.setTypeface(font);
        InterestRate.setTypeface(font);
        RepaymentDuration.setTypeface(font);
        DeleteOfferButton.setTypeface(font);

        DeleteOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteOfferListener.DeleteOffer(position);

                DeleteOfferButton.setEnabled(false);
            }
        });



        LoanAmount.setText("Loan Amount: " + LoanAmountList.get(position) + " EUR");
        InterestRate.setText("Interest Rate: " + InterestRateList.get(position) + " %");
        RepaymentDuration.setText("Repayment Time Duration: " + RepaymentDurationList.get(position) + " months");



        return convertView;
    }



    public void setCallbackForOfferDeletion(DeleteOfferInterface deleteOfferListener){

        this.deleteOfferListener = deleteOfferListener;
    }

    public interface DeleteOfferInterface{
        public void DeleteOffer(int position);
    }



}

