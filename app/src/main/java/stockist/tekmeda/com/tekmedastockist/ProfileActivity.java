package stockist.tekmeda.com.tekmedastockist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import stockist.tekmeda.com.tekmedastockist.bean.Stockists;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView profileName;
    private FirebaseUser user;
    private TextView tvNumberOfOrdersAccepted;
    private TextView tvNumberOfOrdersDelivered;
    private TextView tvNumberOfOrdersNew;
    private TextView tvNumberOfOrdersRejected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = (TextView)findViewById(R.id.tvProfileName) ;
        tvNumberOfOrdersAccepted = (TextView)findViewById(R.id.tvNumberOfOrdersAccepted);
        tvNumberOfOrdersDelivered= (TextView)findViewById(R.id.tvNumberOfOrdersDelivered);
        tvNumberOfOrdersNew= (TextView)findViewById(R.id.tvNumberOfOrdersNew);
        tvNumberOfOrdersRejected = (TextView)findViewById(R.id.tvNumberOfOrdersRejected);
        user=FirebaseAuth.getInstance().getCurrentUser();


        SharedPreferences sharedPreferences = getSharedPreferences("messageidtoken", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("useridtoken","");
        DatabaseReference databaseReferencetoken = FirebaseDatabase.getInstance().getReference("usertokens");
        databaseReferencetoken.child(user.getUid()).setValue(token);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stockists");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals(user.getUid())) {

                        Stockists stockists = child.getValue(Stockists.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("retailspref",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username",stockists.getEnterpriseName());
                        editor.commit();
                        profileName.setText(stockists.getEnterpriseName());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("RejectedOrders").child("Stockists");
        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;

                    count = count + (int) dataSnapshot.getChildrenCount();

                    tvNumberOfOrdersRejected.setText(count + "");
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    count = count + (int) dataSnapshot.getChildrenCount();
                    tvNumberOfOrdersRejected.setText(count + "");
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString()))
                    tvNumberOfOrdersRejected .setText(0+"");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("DeliveredOrders").child("Stockists");
        databaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersDelivered.setText(count + "");
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersDelivered.setText(count + "");
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString()))
                    tvNumberOfOrdersDelivered.setText(0+"");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("PlacedOrders").child("Stockists");
        databaseReference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersNew.setText(count + "");
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersNew.setText(count + "");
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString()))
                    tvNumberOfOrdersNew.setText(0+"");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child("AcceptedOrders").child("Stockists");
        databaseReference4.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersAccepted.setText(count + "");
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Draft Child Changed ",dataSnapshot.getKey());
                Log.d("Draft Child bool",String.valueOf(dataSnapshot.getKey().contains(user.getUid().toString())));
                if(!dataSnapshot.getKey().contains(user.getUid().toString()))
                    tvNumberOfOrdersAccepted.setText(0 + "");
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersAccepted.setText(count + "");
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Draft Child removed ",dataSnapshot.getKey());
                Log.d("Draft Child bool",String.valueOf(dataSnapshot.getKey().contains(user.getUid().toString())));
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString()))
                    tvNumberOfOrdersAccepted.setText(0+"");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intentService = new Intent(getApplicationContext(), NotificationssService.class);
            stopService(intentService);
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

            DatabaseReference databaseReferencetoken = FirebaseDatabase.getInstance().getReference("usertokens").child(user.getUid());
            databaseReferencetoken.removeValue();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(getApplicationContext(),NewOrdersActivity.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(getApplicationContext(),AcceptedOrdersActivity.class));
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(getApplicationContext(),DeliveredOrdersActivity.class));
        }
        else if (id == R.id.nav_share) {
            startActivity(new Intent(getApplicationContext(),MyConnectionsActivity.class));
        }
        else if (id == R.id.nav_newmed) {
            startActivity(new Intent(getApplicationContext(),NewMedicineActivity.class));
        }else if(id == R.id.nav_profile)
        {
            Intent intent = new Intent(getApplicationContext(),ProfileDetailsActivity.class);
            intent.putExtra("edit",true);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void draftorder(View v)
    {
        startActivity(new Intent(getApplicationContext(),NewOrdersActivity.class));
    }

    public void myconnection(View v)
    {
        startActivity(new Intent(getApplicationContext(),MyConnectionsActivity.class));
    }

    public void newmedicine(View v)
    {
        startActivity(new Intent(getApplicationContext(),NewMedicineActivity.class));
    }
}
