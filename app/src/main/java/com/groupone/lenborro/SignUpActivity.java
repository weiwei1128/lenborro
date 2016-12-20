package com.groupone.lenborro;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class SignUpActivity extends Activity {

    static final int REQUEST_PROFILE_PHOTO_CAPTURE_1 = 1;
    static final int REQUEST_PROFILE_PHOTO_CAPTURE_2 = 2;
    static final int REQUEST_ID_CARD_PHOTO = 3;
    static final int REQUEST_BANK_STATEMENT_PHOTO = 4;

    String lenborroCategory = "";

    private Uri picUri;

    String ProfilePhotoBase64 = "";
    String IDCardPhotoBase64 = "";
    String BankStatementPhotoBase64 = "";

    TextView dateOfBirthText;

    EditText firstNameTextField;
    EditText lastNameTextField;
    EditText emailTextField;
    EditText passwordTextField;
    EditText confirmPasswordTextField;
    EditText phoneNumberTextField;
    EditText SSNTextField;
    EditText addressTextField;
    EditText bankNameTextField;
    EditText IBANTextField;


    String firstName = null;
    String lastName = null;
    String email = null;
    String password = null;
    String confirmPassword = null;
    String phoneNumber = null;
    String SSN = null;
    String address = null;
    String bankName = null;
    String IBAN = null;
    String dateofBirth = null;


    Button registerButton;


    String[] dataValues = new String[10];
    int counter = 0;

    boolean userFound = false;

    ImageView ProfilePhotoView;
    ImageView IDCardPhotoView;
    ImageView BankStatementPhotoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        dateOfBirthText = (TextView) findViewById(R.id.dateOfBirthID);

        firstNameTextField = (EditText) findViewById(R.id.FirstNameID);
        lastNameTextField = (EditText) findViewById(R.id.LastNameID);
        emailTextField = (EditText) findViewById(R.id.EmailID);
        passwordTextField = (EditText) findViewById(R.id.PasswordID);
        confirmPasswordTextField = (EditText) findViewById(R.id.ConfirmPasswordID);
        phoneNumberTextField = (EditText) findViewById(R.id.PhoneNumberID);
        SSNTextField = (EditText) findViewById(R.id.SocialSecurityNumberID);
        addressTextField = (EditText) findViewById(R.id.AddressID);
        bankNameTextField = (EditText) findViewById(R.id.BankNameID);
        IBANTextField = (EditText) findViewById(R.id.IBANID);

        registerButton = (Button) findViewById(R.id.RegisterButtonID);


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

        userFound = false;


        ProfilePhotoView = (ImageView) findViewById(R.id.profilePhotoID);
        ProfilePhotoView.setEnabled(false);

        IDCardPhotoView = (ImageView) findViewById(R.id.IDCardPhotoID);
        IDCardPhotoView.setEnabled(false);

        BankStatementPhotoView = (ImageView) findViewById(R.id.BankStatementPhotoID);
        BankStatementPhotoView.setEnabled(false);


    }


    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    public void updatDOBTextView(int day, int month, int year) {

        dateOfBirthText.setText(day + "/" + month + "/" + year);
    }


    public void IDCardButtonClicked(View v)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_ID_CARD_PHOTO);
        }

    }


    public void BankStatementButtonClicked(View v)
    {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_BANK_STATEMENT_PHOTO);
        }

    }

    public void RegisterButtonClicked(View v)
    {
        firstName = firstNameTextField.getText().toString();
        lastName = lastNameTextField.getText().toString();
        email = emailTextField.getText().toString();
        password = passwordTextField.getText().toString();
        confirmPassword = confirmPasswordTextField.getText().toString();
        phoneNumber = phoneNumberTextField.getText().toString();
        SSN = SSNTextField.getText().toString();
        address = addressTextField.getText().toString();
        bankName = bankNameTextField.getText().toString();
        IBAN = IBANTextField.getText().toString();
        dateofBirth = dateOfBirthText.getText().toString();

        email = email.replace(" ", "");


        if (firstName.length() > 0 && lastName.length() > 0 && isEmailValid(email) && password.length() > 0 && confirmPassword.length() > 0 && phoneNumber.length() > 0 && SSN.length() > 0 && address.length() > 0 && bankName.length() > 0 && IBAN.length() > 0 && ProfilePhotoView.isEnabled() && IDCardPhotoView.isEnabled() && BankStatementPhotoView.isEnabled() && lenborroCategory.length() > 0) {
            if (password.equals(confirmPassword)) {

                new CheckWhetherUserAlreadyExistsOnServer().execute("http://www.voltbuy.com/Lenborro/UsersCredientals/Credientals.txt");

            } else {

                Toast.makeText(getApplicationContext(), "Passwords did not match", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Fields are not filled properly", Toast.LENGTH_SHORT).show();
        }


    }


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private class CheckWhetherUserAlreadyExistsOnServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return CheckWhetherUserAlreadyExists(urls[0]);
            } catch (IOException e) {
                return "NotFound";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            registerButton.setVisibility(View.INVISIBLE);

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


            userFound = false;


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            if (result.equals("OK")) {

                if (userFound) {


                    registerButton.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), "This email is already registered", Toast.LENGTH_SHORT).show();

                } else {

                    new SignUpToServer().execute("http://www.voltbuy.com/Lenborro/Register.php");


                }

            } else if (result.equals("NotFound")) {

                new SignUpToServer().execute("http://www.voltbuy.com/Lenborro/Register.php");

            } else {

                registerButton.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();

            }


        }
    }

    private String CheckWhetherUserAlreadyExists(String myurl) throws IOException, UnsupportedEncodingException {
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

            userFound = false;


            while ((readlineText = textReader.readLine()) != null) {


                for (int i = 0; i < readlineText.length(); ++i) {
                    if (readlineText.charAt(i) == '|') {
                        ++counter;

                        continue;
                    }

                    dataValues[counter] = (dataValues[counter] + readlineText.charAt(i));
                }


                if (dataValues[3].equals(email)) {
                    userFound = true;
                    break;
                }


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


            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                conn.disconnect();
                return "OK";

            } else {
                conn.disconnect();
                return "NetworkError";
            }


            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {


            if (is != null) {
                is.close();


            }

        }
    }


    private class SignUpToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return SignUpFunction(urls[0]);
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

                Intent intent = new Intent(SignUpActivity.this , LoginActivity.class);
                startActivity(intent);
                finish();

            } else {
                registerButton.setVisibility(View.VISIBLE);


                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private String SignUpFunction(String myurl) throws IOException, UnsupportedEncodingException {

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
                    .appendQueryParameter("FirstName", firstName)
                    .appendQueryParameter("LastName", lastName)
                    .appendQueryParameter("DateOfBirth", dateofBirth)
                    .appendQueryParameter("Email", email)
                    .appendQueryParameter("Password", password)
                    .appendQueryParameter("PhoneNumber", phoneNumber)
                    .appendQueryParameter("SSN", SSN)
                    .appendQueryParameter("Address", address)
                    .appendQueryParameter("BankName", bankName)
                    .appendQueryParameter("IBAN", IBAN)
                    .appendQueryParameter("LenborroCategory", lenborroCategory)
                    .appendQueryParameter("ProfilePhotoBase64", ProfilePhotoBase64)
                    .appendQueryParameter("IDCardPhotoBase64", IDCardPhotoBase64)
                    .appendQueryParameter("BankStatementPhotoBase64", BankStatementPhotoBase64)
                    .appendQueryParameter("ProfilePhotoName", ("ProfilePhotos/" + email + ".jpg"))
                    .appendQueryParameter("IDCardPhotoName", ("IDCardPhotos/" + email + ".jpg"))
                    .appendQueryParameter("BankStatementPhotoName", ("BankStatementPhotos/" + email + ".jpg"));

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


    public void ProfilePhotoButtonClicked(View v) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_PROFILE_PHOTO_CAPTURE_1);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_PROFILE_PHOTO_CAPTURE_1 && resultCode == RESULT_OK) {

            picUri = data.getData();

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQUEST_PROFILE_PHOTO_CAPTURE_2);

        }

        if (requestCode == REQUEST_PROFILE_PHOTO_CAPTURE_2 && resultCode == RESULT_OK) {


            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Matrix matrix = new Matrix();
            //  matrix.postScale(-1, 1, imageBitmap.getWidth()/2, imageBitmap.getHeight()/2);
            // Bitmap bbb = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);

            ProfilePhotoView.setImageBitmap(imageBitmap);
            ProfilePhotoView.setEnabled(true);


            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] ba = bao.toByteArray();
            ProfilePhotoBase64 = Base64.encodeToString(ba, Base64.DEFAULT);


        }



        if (requestCode == REQUEST_ID_CARD_PHOTO && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Matrix matrix = new Matrix();
            //  matrix.postScale(-1, 1, imageBitmap.getWidth()/2, imageBitmap.getHeight()/2);
            // Bitmap bbb = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);

            IDCardPhotoView.setImageBitmap(imageBitmap);
            IDCardPhotoView.setEnabled(true);


            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
            byte[] ba = bao.toByteArray();
            IDCardPhotoBase64 = Base64.encodeToString(ba, Base64.DEFAULT);
        }




        if (requestCode == REQUEST_BANK_STATEMENT_PHOTO && resultCode == RESULT_OK) {

            Uri uri = data.getData();


            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");



           BankStatementPhotoView.setImageBitmap(imageBitmap);
            BankStatementPhotoView.setEnabled(true);


           ByteArrayOutputStream bao = new ByteArrayOutputStream();
           imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);




            byte[] ba = bao.toByteArray();

            BankStatementPhotoBase64 = Base64.encodeToString(ba, Base64.DEFAULT);

        }



    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        return byteBuffer.toByteArray();
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




