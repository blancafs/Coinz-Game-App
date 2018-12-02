package com.example.blanca.coinz2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ////////////////
    // Variables ///
    ////////////////

    // Declaring private variables from first screen //
    private static final String tag = "MainActivity.java";
    private EditText name1;
    private EditText password1;
    private Button login1;
    private TextView tosignup;

    // Firebase variables //
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private DocumentReference firestoreUsers;
    private static final String TAG = "UsersActivity";
    private static final String COLLECTION_KEY = "users";
    private static final String DOCUMENT_KEY = "users";
    private static final String NAME_FIELD = "Name";
    private static final String PASSWORD_FIELD = "Password";

    //Map Download variables //
    private String downloadDate = ""; // Format YYYY/MM/DD
    private final String preferencesFile = "MyPrefsFile"; // for storing preferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    ////////////////
    // On methods //
    ////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d(tag, "onCreate [MainActivity just started running ==========================================================================================================]");

        mAuth = FirebaseAuth.getInstance();

        // Assigning Ids from Login page to vars //
        name1 = findViewById(R.id.etEmail1);
        password1 = findViewById(R.id.etPassword1);
        login1 = findViewById(R.id.btLogin1);
        login1.setOnClickListener(this);
        tosignup = findViewById(R.id.bt2SignUp);
        tosignup.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user signed in and update UI accordingly //
        // String strIWAntToGet = MySharedPreferences.getPrefUserName(context);

    }

    // On stop called even if our app is killed by the operating system
    @Override
    public void onStop() {
        super.onStop();
        Log.d(tag, "[onStop] String lastDownloadDate of " + downloadDate);

        //All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(preferencesFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("lastDownloadDate", downloadDate);

        //Apply the edits!
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.bt2SignUp:
                Log.d(tag,"onClick[Button to sign up has been clicked. To second activity.]");
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.btLogin1:
                Log.d(tag,"onClick[Button to log in has been clicked. To user login method.]");
                String ema = name1.getText().toString();
                MySharedPreferences.setUserName(getApplicationContext(), ema);
                userLogin();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //////////////////
    // User methods //
    //////////////////

    private void userLogin() {
        String email = name1.getText().toString().trim();
        String password = password1.getText().toString().trim();

        if (email.isEmpty()) {
            Log.d(tag,"userLogin [email area is found empty]");
            name1.setError("Email is required");
            name1.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.d(tag,"userLogin [email is not valid]");
            name1.setError("Please enter a valid email");
            name1.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Log.d(tag,"userLogin [password area is found empty]");
            password1.setError("Password is required");
            password1.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Log.d(tag,"userLogin [password length is smaller than 6]");
            password1.setError("Minimum length of password should be 6");
            password1.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(tag,"userLogin onComplete[sign in successfull!]");
                    // toast that shows how to work screen
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                    View toastLayout = inflater.inflate(R.layout.logintoast, findViewById(R.id.llCustom2));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();
                    MySharedPreferences.setUserName(getApplicationContext(),email);
                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                } else {
                    Log.d(tag," userLogin onComplete[sign up unsuccessfull]");
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
