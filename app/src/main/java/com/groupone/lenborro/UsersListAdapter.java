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

/**
 * Created by user on 12/17/2016.
 */

public class UsersListAdapter  extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;

    private ArrayList<String> NamesList;
    private ArrayList<String> EmailsList;
    private ArrayList<String> PhoneNumbersList;


    Typeface font;

    DeleteUserInterface deleteUserListener;




    public UsersListAdapter(Activity activity, ArrayList<String> NamesList, ArrayList<String> EmailsList, ArrayList<String> PhoneNumbersList) {

        this.activity = activity;
        this.NamesList = NamesList;
        this.EmailsList = EmailsList;
        this.PhoneNumbersList = PhoneNumbersList;


        /*
               Font type for text
       */

        font = Typeface.createFromAsset(activity.getAssets(), "BakersfieldBold.ttf");



    }

    @Override
    public int getCount() {
        return NamesList.size();
    }

    @Override
    public Object getItem(int location) {
        return NamesList.get(location);
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
            convertView = inflater.inflate(R.layout.users_list_row, null);


        TextView Name = (TextView) convertView.findViewById(R.id.NameID);
        TextView Email = (TextView) convertView.findViewById(R.id.EmailID);
        TextView PhoneNumber = (TextView) convertView.findViewById(R.id.PhoneNumberID);
        final Button DeleteUserButton = (Button) convertView.findViewById(R.id.DeleteUserButtonID);



        Name.setTypeface(font);
        Email.setTypeface(font);
        PhoneNumber.setTypeface(font);
        DeleteUserButton.setTypeface(font);

        DeleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteUserListener.DeleteUser(position);

                DeleteUserButton.setEnabled(false);
            }
        });



        Name.setText("Name: " + NamesList.get(position));
        Email.setText("Email: " + EmailsList.get(position));
        PhoneNumber.setText("Phone No: : " + PhoneNumbersList.get(position));



        return convertView;
    }



    public void setCallbackForOfferDeletion(DeleteUserInterface deleteUserListener){

        this.deleteUserListener = deleteUserListener;
    }

    public interface DeleteUserInterface{
        public void DeleteUser(int position);
    }



}

