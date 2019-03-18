package com.ieeepec.smart.attendance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public FirebaseFirestore db;
    public  BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DocumentReference currentUser = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (!documentSnapshot.exists()) {
                            startActivity(new Intent(getApplicationContext(), CreateAccount.class));
                        }
                    }
                }
            });
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            menu.findItem(R.id.login).setTitle("Profile");
            menu.findItem(R.id.signout).setVisible(true);
        }else
        {
            menu.findItem(R.id.signout).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

          bottomNavigationView = findViewById(R.id.bottomNav);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.feed: {
                        bottomNavigationView.getMenu().findItem(R.id.events).setChecked(false);
                        item.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Frag_feeds()).commit();

                        break;
                    }
                    case R.id.events:

                    {
                        bottomNavigationView.getMenu().findItem(R.id.feed).setChecked(false);
                        item.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Frag_events()).commit();
                        break;
                    }
                    }
                return false;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Frag_feeds()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.login: {
                if (menuItem.getTitle().equals("Profile")) {
                    startActivity(new Intent(this, Profile.class));
                    break;
                } else {
                    startActivity(new Intent(this, Login.class));
                    break;
                }
            }
            case R.id.signout:{
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,MainActivity.class));
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
