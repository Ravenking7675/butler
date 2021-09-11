package com.avinash.butler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    private int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "Login";
    private FirebaseAuth mAuth;

    private ImageView signin;
    private ImageView loginB;
    private ImageView loginBoy;

    Animation animFadein;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void send(){
        Intent intent = new Intent(Login.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        signin = findViewById(R.id.signinB);
        loginB = findViewById(R.id.loginB);
        loginBoy = findViewById(R.id.loginBoy);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.hover);
        loginBoy.startAnimation(animFadein);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(Login.this, "Logged out", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(Login.this, "No user exist", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(Login.this, "No user exist", Toast.LENGTH_SHORT).show();

                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }


    //Google signin
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();

                            send();
//                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                            Toast.makeText(Login.this, "something went wrong!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        try {
            if (currentUser.getUid() != null) {
                Toast.makeText(Login.this, "Logged In" + currentUser.getUid(), Toast.LENGTH_SHORT).show();
//                send();

            }
        }
        catch (Exception e){
            Toast.makeText(Login.this, "No user exist", Toast.LENGTH_SHORT).show();

        }

//        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

}
