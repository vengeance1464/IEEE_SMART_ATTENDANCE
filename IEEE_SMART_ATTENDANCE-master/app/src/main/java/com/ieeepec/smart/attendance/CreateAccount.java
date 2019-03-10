package com.ieeepec.smart.attendance;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class CreateAccount extends AppCompatActivity {
    private EditText name;
    private EditText colegeIdText, sid;
    //  private TextView login;
    private FirebaseAuth userAuth;
    private FirebaseFirestore db;
    private ImageButton cross;
    private RadioGroup radioGroup;
    private Button createAccount;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        init();
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (colegeIdText.getText().toString().equals("")) {
                        colegeIdText.setClickable(true);
                        colegeIdText.setFocusableInTouchMode(true);
                        colegeIdText.setFocusable(true);
                        createAccount.setText("ENTER COLLEGE");
                        createAccount.setClickable(false);
                        createAccount.setAlpha(0.5f);
                    } else if (sid.getText().toString().equals("")) {
                        sid.setClickable(true);
                        sid.setFocusableInTouchMode(true);
                        sid.setFocusable(true);
                        sid.setText("ENTER COLLEGE ID/SID");
                        sid.setClickable(false);
                        createAccount.setAlpha(0.5f);
                    } else {
                        createAccount.setClickable(true);
                        createAccount.setText("SIGN UP");
                        createAccount.setAlpha(1.0f);
                    }
                } else {
                    createAccount.setText("ENTER NAME");
                    createAccount.setAlpha(0.5f);
                    createAccount.setClickable(false);
                }
            }
        });
        colegeIdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (name.getText().toString().equals("")) {
                        name.setClickable(true);
                        name.setFocusableInTouchMode(true);
                        name.setFocusable(true);
                        createAccount.setText("ENTER NAME");
                        createAccount.setClickable(false);
                        createAccount.setAlpha(0.5f);
                    } else if (sid.getText().toString().equals("")) {
                        sid.setClickable(true);
                        sid.setFocusableInTouchMode(true);
                        sid.setFocusable(true);
                        createAccount.setText("ENTER COLLEGE ID/SID");
                        createAccount.setClickable(false);
                        createAccount.setAlpha(0.5f);

                    } else {
                        createAccount.setClickable(true);
                        createAccount.setText("SIGN UP");
                        createAccount.setAlpha(1.0f);
                    }
                } else {
                    createAccount.setText("ENTER COLLEGE");
                    createAccount.setAlpha(0.5f);
                    createAccount.setClickable(false);
                }
            }
        });
        sid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (name.getText().toString().equals("")) {
                        name.setClickable(true);
                        name.setFocusableInTouchMode(true);
                        name.setFocusable(true);
                        createAccount.setText("ENTER NAME");
                        createAccount.setClickable(false);
                        createAccount.setAlpha(0.5f);
                    } else if (colegeIdText.getText().toString().equals("")) {
                        colegeIdText.setClickable(true);
                        colegeIdText.setFocusableInTouchMode(true);
                        colegeIdText.setFocusable(true);
                        createAccount.setText("ENTER COLLEGE");
                        createAccount.setClickable(false);
                        createAccount.setAlpha(0.5f);
                    } else {
                        createAccount.setClickable(true);
                        createAccount.setText("SIGN UP");
                        createAccount.setAlpha(1.0f);
                    }
                } else {
                    createAccount.setText("ENTER COLLEGE ID/SID");
                    createAccount.setAlpha(0.5f);
                    createAccount.setClickable(false);
                }
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CreateAccount.this, "Choose one option", Toast.LENGTH_SHORT).show();
                } else if ((radioGroup.getCheckedRadioButtonId() != R.id.rb3) && (!collegeIdIsValid(sid.getText().toString()))) {
                    Toast.makeText(CreateAccount.this, "Enter valid PEC SID", Toast.LENGTH_SHORT).show();

                } else {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    User newUser = new User(name.getText().toString(), colegeIdText.getText().toString(), sid.getText().toString(), radioButton.getText().toString());
                    createMyAccount(newUser, radioButton.getText().toString(), sid.getText().toString(), colegeIdText.getText().toString());
                }
            }
        });
       /* cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAuth.signOut();
                startActivity(new Intent(CreateAccount.this,MainActivity.class));
            }
        });*/
    }

    private void init() {
        name = (EditText) findViewById(R.id.etname);
        colegeIdText = (EditText) findViewById(R.id.etcollege);
        sid = (EditText) findViewById(R.id.etsid);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        // login=(TextView)findViewById(R.id.textView3);
        userAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // cross=(ImageButton)findViewById(R.id.cross);
        createAccount = (Button) findViewById(R.id.createaccount);
        createAccount.setAlpha(.5f);
        createAccount.setClickable(false);
    }

    private boolean collegeIdIsValid(String s) {
        long minId = 15100000;
        long maxId = 18109999;
        long givenId = Long.parseLong(s);
        return ((givenId > minId) && (givenId < maxId)) ? true : false;
    }

    private void createMyAccount(final User newUser, final String collString, final String sid, final String college) {
        createAccount.setAlpha(0.7f);
        // progressDialog = new ProgressDialog(CreateAccount.this, R.style.AppCompatAlertDialogStyle);
        //progressDialog.setCancelable(false);
        //progressDialog.setMessage("Creating Your Account ");
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.show();
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (collString.equals("Outside PEC")) {
                    db.collection(collString).document(college).collection("Students").document(sid).set(newUser);
                } else {
                    db.collection(collString).document(sid).set(newUser);
                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

/*        AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
        builder.setTitle("Are you sure you want to proceed?").setMessage("You cannot edit the information later.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // finish();
                        dialog.dismiss();
                        progressDialog = new ProgressDialog(CreateAccount.this, R.style.AppCompatAlertDialogStyle);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Creating Your Account ");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if(collString.equals("Outside PEC"))
                                {
                                    db.collection(collString).document(college).collection("Students").document(sid).set(newUser);
                                }else {
                                    db.collection(collString).document(sid).set(newUser);
                                }
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                        });


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        createAccount.setAlpha(1.0f);
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();
*/
    }
}
