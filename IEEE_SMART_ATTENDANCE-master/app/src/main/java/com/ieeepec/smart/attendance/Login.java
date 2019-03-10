package com.ieeepec.smart.attendance;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.concurrent.TimeUnit;


public class Login extends AppCompatActivity {

    private FirebaseAuth userAuth;
    private String verification_code;
    private EditText otp, mobile;
    private TextView carrier, mobileIntro;
    private Button verify, sendOtp;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    boolean doubleBackToExitPressedOnce = false;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
   // private ProgressDialog progressDialog;
    //Context context;
    private static final int PERMISSION_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(Login.this, "verification completed", Toast.LENGTH_SHORT).show();
                signInWithPhone(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Login.this, "verification failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification_code = s;
                Toast.makeText(Login.this, "Code sent", Toast.LENGTH_SHORT).show();
            }
        };
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp.setAlpha(0.7f);
                checkPermission();
                sendOtp.setAlpha(1.0f);
                /*
                String number = mobile.getText().toString();
                if(number.length()==10) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + number, 60, TimeUnit.SECONDS, Login.this, mCallback
                    );
                    mobileIntro.setText("Enter OTP in case we fail to detect SMS automatically");
                    carrier.setVisibility(View.GONE);
                    verify.setVisibility(View.VISIBLE);
                    otp.setVisibility(View.VISIBLE);
                    mobile.setVisibility(View.GONE);
                    sendOtp.setVisibility(View.GONE);
                }else
                {
                    Toast.makeText(Login.this,"Enter a valid mobile number",Toast.LENGTH_SHORT);
                }*/
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify.setAlpha(0.7f);
                String code = otp.getText().toString();
                if (code.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Enter a valid OTP", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_code, code);
                    signInWithPhone(credential);
                }
                verify.setAlpha(1.0f);
            }
        });
    }
    public void signInWithPhone(PhoneAuthCredential credential) {
        //progressDialog.show();
        userAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            DocumentReference currentUser = db.collection("Users").document(user.getUid());
                            currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()) {
                                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                    if (!task.isSuccessful()) {
                                                        Toast.makeText(Login.this, "Unable to login", Toast.LENGTH_SHORT).show();

                                                    }
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                    //finish();
                                                }
                                            });

                                        } else {
                                            startActivity(new Intent(getApplicationContext(), CreateAccount.class));
                                            // finish();
                                        }
                                     //   progressDialog.dismiss();
                                    } else {
                                       // progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Login Failed!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Login.this, "Already exists", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    /*public void send_sms()
    {
        String number = mobile.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,60, TimeUnit.SECONDS,this,mCallback
        );
    }*/
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallback,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    public void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //SEE NEXT STEP
            if (checkSelfPermission(android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                //SEE NEXT STEP
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);

            } else {

                String number = mobile.getText().toString();
                if (number.length() == 10) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + number, 60, TimeUnit.SECONDS, this, mCallback
                    );
                    mobileIntro.setText("Enter OTP in case we fail to detect SMS automatically");
                    carrier.setVisibility(View.GONE);
                    verify.setVisibility(View.VISIBLE);
                    otp.setVisibility(View.VISIBLE);
                    mobile.setVisibility(View.GONE);
                    sendOtp.setVisibility(View.GONE);
                } else {
                    Toast.makeText(Login.this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show();
                }
            }

        } else {

            String number = mobile.getText().toString();
            if (number.length() == 10) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + number, 60, TimeUnit.SECONDS, Login.this, mCallback
                );
                mobileIntro.setText("Enter OTP in case we fail to detect SMS automatically");
                carrier.setVisibility(View.GONE);
                verify.setVisibility(View.VISIBLE);
                otp.setVisibility(View.VISIBLE);
                mobile.setVisibility(View.GONE);
                sendOtp.setVisibility(View.GONE);
            } else {
                Toast.makeText(Login.this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Do the stuff that requires permission...
                //Intent createAccount = new Intent(context, Login.class);
                //startActivity(createAccount);
                String number = mobile.getText().toString();
                if (number.length() == 10) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + number, 60, TimeUnit.SECONDS, Login.this, mCallback
                    );
                    mobileIntro.setText("Enter OTP in case we fail to detect SMS automatically");
                    carrier.setVisibility(View.GONE);
                    verify.setVisibility(View.VISIBLE);
                    otp.setVisibility(View.VISIBLE);
                    mobile.setVisibility(View.GONE);
                    sendOtp.setVisibility(View.GONE);
                } else {
                    Toast.makeText(Login.this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show();
                }
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show permission explanation dialog...
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(Login.this);
                    alertBuilder.setCancelable(true);
                    // alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Allow APS to send and read SMS");
                    alertBuilder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            // Intent createAccount = new Intent(getApplicationContext(), Login.class);
                            //startActivity(createAccount);
                            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);

                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                }
            }
        }
    }

    private void init() {
        /*progressDialog = new ProgressDialog(Login.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging You In ");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);*/
        userAuth = FirebaseAuth.getInstance();
        // context=getApplicationContext();
        sendOtp = (Button) findViewById(R.id.sendotp);
        carrier = (TextView) findViewById(R.id.carrier);
        mobileIntro = (TextView) findViewById(R.id.mobileintro);
        verify = (Button) findViewById(R.id.verify);
        otp = (EditText) findViewById(R.id.otp);

        mobile = (EditText) findViewById(R.id.mobilenumber);
    }


}
