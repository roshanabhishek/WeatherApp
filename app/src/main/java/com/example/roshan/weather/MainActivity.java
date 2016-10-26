package com.example.roshan.weather;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

;import java.io.InputStream;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    private Button button_revoke, button_logout;
    private TextView textView_name, textView_email;
   // private LinearLayout profile_layout;
    private ImageView imageView_profile_image;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);
        // button_revoke = (Button) findViewById(R.id.button_revoke);
        // button_revoke.setOnClickListener(this);
        //  button_logout = (Button) findViewById(R.id.btn_sign_out);
        //  button_logout.setOnClickListener(this);
        //  imageView_profile_image = (ImageView) findViewById(R.id.imageView_profile_image);
        //  textView_name = (TextView) findViewById(R.id.textView_name);
        //  textView_email = (TextView) findViewById(R.id.textView_email);
          //profile_layout = (LinearLayout) findViewById(R.id.profilelayout);

// Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();



    }




        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.btn_sign_in:
// Signin button clicked
                    signInWithGplus();
                    Intent intent = new Intent(this,MainPage.class);
                    startActivity(intent);
                    finish();
                    break;
                //  case R.id.btn_sign_out:
// logout button clicked
                //     signOutFromGplus();
                //     break;
                //   case R.id.button_revoke:
// revoke button clicked
                // revokeGplusAccess();
                //  break;
            }

        }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
       // Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
// Get user's information
        getProfileInformation();
// Update the UI after signin
        updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this,
                    0).show();
            return;
        }
        if (!mIntentInProgress) {
// Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;
            if (mSignInClicked) {
// The user has already clicked 'sign-in' so we attempt to
// resolve all
// errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    private static final int GOOGLE_SIGIN = 50;

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, GOOGLE_SIGIN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == GOOGLE_SIGIN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }
            mIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {

            btnSignIn.setVisibility(View.GONE);
           Intent intent = new Intent(this, MainPage.class);
           startActivity(intent);
            //profile_layout.setVisibility(View.VISIBLE);
          //  coordinatorLayout.setVisibility(View.VISIBLE);


        }
      else {
           // Intent intent = new Intent(this,MainActivity.class);
           // startActivity(intent);

            btnSignIn.setVisibility(View.VISIBLE);
           // profile_layout.setVisibility(View.GONE);
            //coordinatorLayout.setVisibility(View.GONE);
        }
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                textView_name.setText(personName);
                textView_email.setText(email);
// by default the profile url gives 50x50 px image only
// we can replace the value with whatever dimension we want by
// replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + 400;
                new LoadProfileImage(imageView_profile_image).execute(personPhotoUrl);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Sign-out from google
     */

    public void signOutFromGplus() {

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
        }
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e("pavan", "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }
                    });
        }
    }

    /**
     * Background Async task to load user profile picture from url
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;

        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }


}

