package stockist.tekmeda.com.tekmedastockist;

import android.app.ProgressDialog;
import android.content.Intent;
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

import stockist.tekmeda.com.tekmedastockist.bean.Orders;
import stockist.tekmeda.com.tekmedastockist.util.OrderAdapter;

public class OrderedMedicineActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String retailerId;
    public static ArrayList<Orders> ordersList;
    public static OrderAdapter orderAdapter;
    private ListView listView;
    private boolean showOrderNumber=false;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_medicine);
        user = FirebaseAuth.getInstance().getCurrentUser();
        retailerId = getIntent().getStringExtra("retailerId");
        ordersList = new ArrayList<>();
        listView = (ListView)findViewById(R.id.lvMedicinesOrdered);
        progressDialog = new ProgressDialog(OrderedMedicineActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PlacedOrders").child("Stockists").child(user.getUid()).child(retailerId);
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
                orderAdapter= new OrderAdapter(OrderedMedicineActivity.this,ordersList);
                listView.setAdapter(orderAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1002 && data!=null)
        {
            String message=data.getStringExtra("MESSAGE");
            int position=data.getIntExtra("position",0);
            if(message.equalsIgnoreCase("modified"))
            {
                // Toast.makeText(getApplicationContext(),"modified",Toast.LENGTH_SHORT).show();
                ordersList.remove(position);
                orderAdapter.notifyDataSetChanged();
            }
        }
    }
}
