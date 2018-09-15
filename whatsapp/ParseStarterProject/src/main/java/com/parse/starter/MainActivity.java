
//importing all the packages needed

package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

    Boolean loginModeActive = false;                                                                         //nobody has logged in

    public void redirectIfLoggedIn(){                                                                        //if logged in, then...

        if (ParseUser.getCurrentUser() != null){

            Intent intent = new Intent(getApplicationContext(), UserListActivity.class);                     //if user logged in, then jump to user list activity
            startActivity(intent);                                                                           // jumping to user list activity using "intent"

        }else {



        }

    }

    public void toggleLoginMode (View view){                                                                 // for jumping internally from login mode if want to login or else sign up mode if want to sign up

        Button loginSignUpButton = (Button) findViewById(R.id.loginSignupButton);
        TextView toggleloginModeTextView = (TextView) findViewById(R.id.toggleLoginModeTextView);

        if (loginModeActive){

            loginModeActive = false;                                                                         //if want to sign up, highlight the button to sign up and text view to log in
            loginSignUpButton.setText("Sign Up!");
            toggleloginModeTextView.setText("Or, Log In!");

        }else {

            loginModeActive = true;                                                                          //if want to login, highlight the button to login and text view to sign up
            loginSignUpButton.setText("Log in!");
            toggleloginModeTextView.setText("Or, SignUp");

        }

    }

    public void signupLogin(View view){                                                                      //code for Sign up or Login

        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);                          //including the required layout elements related to sign up or login

        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        if (loginModeActive){                                                                                //if in login mode, then...

            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (e == null){                                                                          //if exception is null, meaning no exception, then...

                        Log.i("Info", "user logged in!");                                          //no exception, so log the user in...

                        redirectIfLoggedIn();                                                                //redirecting to intent if logging finished in background

                    }else {                                                                                  //else if exception, display it

                        String message = e.getMessage();

                        if (message.toLowerCase().contains("java")){                                         //contains java lang class exception

                            message = e.getMessage().substring(e.getMessage().indexOf(" "));                 //printing the exception raised by java lang class

                        }

                        Toast.makeText(MainActivity.this, message , Toast.LENGTH_SHORT).show();      //for displaying small pop up messages of exception, Toast is used

                    }

                }
            });

        }else {                                                                                              //else if in sign up mode, then...



            ParseUser user = new ParseUser();                                                                //creating a new parse server user

            user.setUsername(usernameEditText.getText().toString());                                         //getting text entered in username field and converting it to string

            user.setPassword(passwordEditText.getText().toString());                                         //getting text entered in password field and converting it to string

            user.signUpInBackground(new SignUpCallback() {                                                   //signing up in background
                @Override
                public void done(ParseException e) {

                    if (e == null) {                                                                         //if no exception,then...

                        Log.i("Info", "user signed up!");                                         //sign the user up in background

                        redirectIfLoggedIn();                                                               //redirect to next screen/activity using intent

                    } else {                                                                                //else, if exception, display it...

                        String message = e.getMessage();                                                    //getting the exception

                        if (message.toLowerCase().contains("java")){                                        //if exception is from java lang class

                            message = e.getMessage().substring(e.getMessage().indexOf(" "));                //print the exception raised by java lang

                        }

                        Toast.makeText(MainActivity.this, message , Toast.LENGTH_SHORT).show();      //using small pop up message to display exception

                    }

                }
            });

        }

    }


  @Override
  protected void onCreate(Bundle savedInstanceState) {                                                        //process defining what happens after app launches
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);                                                                   //connection or making the xml file alive

    setTitle("Whatsapp Login");                                                                               // setting screen display title

    redirectIfLoggedIn();                                                                                     //if already logged in then directly show chats and history or user list
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());                                                   //once the app launches, begin database synchronisation
  }

}