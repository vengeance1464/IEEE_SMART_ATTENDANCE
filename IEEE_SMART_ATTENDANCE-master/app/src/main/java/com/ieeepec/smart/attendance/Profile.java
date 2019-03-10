package com.ieeepec.smart.attendance;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {
    private TextView name,sid,college,prof;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name=(TextView)findViewById(R.id.name);
        sid=(TextView)findViewById(R.id.sid);
        college=(TextView)findViewById(R.id.college);
        prof=(TextView)findViewById(R.id.prof);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot=task.getResult();
                name.setText(String.valueOf(documentSnapshot.get("name")));
                college.setText(String.valueOf(documentSnapshot.get("college")));
                sid.setText(String.valueOf(documentSnapshot.get("sid")));
                if(!(String.valueOf(documentSnapshot.get("status")).equals("Outside PEC")))
                {
                    prof.setText(String.valueOf(documentSnapshot.get("status")));
                }
            }
        });

    }
}