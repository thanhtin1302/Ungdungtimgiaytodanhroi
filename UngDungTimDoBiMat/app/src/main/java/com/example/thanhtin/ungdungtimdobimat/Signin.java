package com.example.thanhtin.ungdungtimdobimat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.thanhtin.ungdungtimdobimat.Class.Users;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signin extends AppCompatActivity {

    SignInButton buttongg;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    private final static int RC_SIGN_IN = 2;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseFirestore db;
    ProgressBar progressBar3;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        progressBar3 = findViewById(R.id.progressBar3);
        mAuth = FirebaseAuth.getInstance();
        buttongg = (SignInButton) findViewById(R.id.googleBtn);
        buttongg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                buttongg.setVisibility(View.GONE);
                progressBar3.setVisibility(View.VISIBLE);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() !=null){

                    db = FirebaseFirestore.getInstance();
                    db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Users u = documentSnapshot.toObject(Users.class);
                            String av=mAuth.getCurrentUser().getPhotoUrl().toString();
                            if(u != null){
                                startActivity(new Intent(Signin.this,MainActivity.class));

                            }else {
                                Users users = new Users("","",firebaseAuth.getCurrentUser().getEmail(),av,firebaseAuth.getCurrentUser().getDisplayName(),firebaseAuth.getCurrentUser().getUid(),"","");
                                db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Signin.this,"Chào mừng bạn đến với Quest App", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Signin.this,MainActivity.class));
                                    }
                                });
                            }
                        }
                    });
                }
            }
        };


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1065398276824-0v30p33shfrph24bm5p9kaornf9v8cj2.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Signin.this,"Lỗi Kết Nối",Toast.LENGTH_SHORT).show();

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(Signin.this,"Người Dùng Không Đúng",Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Signin.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }


                    }
                });
    }

}
