package com.ieeepec.smart.attendance;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class Barcode extends AppCompatActivity {

    private Button scan;
    private ImageView qrcode;
    private TextView textb;
    private String qr, dId;
    private int id;
    private ArrayList<String> regList=new ArrayList<>();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcodescn);
        Intent getIntent=getIntent();
        qr=getIntent.getStringExtra("qr");
        dId=getIntent.getStringExtra("did");
        regList= (ArrayList<String>) getIntent.getSerializableExtra("reg");
        id=getIntent.getIntExtra("id",0);
        scan = findViewById(R.id.scan);
        qrcode = findViewById(R.id.qrcode);
        textb=findViewById(R.id.textb);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(Barcode.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();


            }
        });
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            final IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if (result!=null&&result.getContents()!=null){
              /*  new AlertDialog.Builder(Barcode.this)
                        .setTitle("SCAN RESULTS")
                        .setMessage(result.getContents())
                        .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClipboardManager manager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                                ClipData data=ClipData.newPlainText("result",result.getContents());
                                manager.setPrimaryClip(data);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();*/
              if(result.getContents().equals(qr))
              {

                  db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                          regList.add(dId);
                          DocumentSnapshot documentSnapshot = task.getResult();
                          String name = String.valueOf(documentSnapshot.get("name"));
                          String sid = String.valueOf(documentSnapshot.get("sid"));
                          String status = String.valueOf(documentSnapshot.get("status"));
                          String college = String.valueOf(documentSnapshot.get("college"));
                          User newUser = new User(name, college, sid, status);
                          if (status.equals("Outside PEC")) {
                              db.collection("Event").document(String.valueOf(id)).collection(college).document(sid).set(newUser);
                          }
                          else
                          {
                              db.collection("Event").document(String.valueOf(id)).collection(status).document(sid).set(newUser);
                          }
                          db.collection("Event").document(String.valueOf(id)).update("reg",regList);
                          Toast.makeText(Barcode.this,"Attendance Marked Successfully",Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(Barcode.this,MainActivity.class));
                      }
                  });

              }else
              {
                  Toast.makeText(Barcode.this,"Scan Again",Toast.LENGTH_SHORT).show();
              }
            }


            super.onActivityResult(requestCode, resultCode, data);

        }
    }
