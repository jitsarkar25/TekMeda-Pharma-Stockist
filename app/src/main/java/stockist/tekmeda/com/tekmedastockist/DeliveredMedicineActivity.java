package stockist.tekmeda.com.tekmedastockist;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import stockist.tekmeda.com.tekmedastockist.bean.Orders;
import stockist.tekmeda.com.tekmedastockist.util.AcceptedOrderAdapter;
import stockist.tekmeda.com.tekmedastockist.util.DeliveredOrderAdapter;
import stockist.tekmeda.com.tekmedastockist.util.DeliveredOrderComparator;

public class DeliveredMedicineActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String retailerId;
    public static ArrayList<Orders> ordersList;
    private DeliveredOrderAdapter deliveredOrderAdapter;
    private ListView listView;
    private boolean showOrderNumber=false;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_medicine);
        user = FirebaseAuth.getInstance().getCurrentUser();
        retailerId = getIntent().getStringExtra("retailerId");
        ordersList = new ArrayList<>();
        listView = (ListView)findViewById(R.id.lvMedicinesOrderedDelivered);
        progressDialog = new ProgressDialog(DeliveredMedicineActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("DeliveredOrders").child("Stockists").child(user.getUid()).child(retailerId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    showOrderNumber=true;
                    for(DataSnapshot dss:ds.getChildren()) {
                        Orders orders = dss.getValue(Orders.class);
                        if(showOrderNumber==true)
                        {
                            orders.setShowOrderNumber(true);
                            showOrderNumber=false;
                        }
                        else
                            orders.setShowOrderNumber(false);
                        ordersList.add(orders);
                    }
                }
                Collections.sort(ordersList,new DeliveredOrderComparator());
                deliveredOrderAdapter= new DeliveredOrderAdapter(DeliveredMedicineActivity.this,ordersList);
                listView.setAdapter(deliveredOrderAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
