package stockist.tekmeda.com.tekmedastockist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import stockist.tekmeda.com.tekmedastockist.bean.Connections;
import stockist.tekmeda.com.tekmedastockist.util.AcceptedRetailerListAdapter;
import stockist.tekmeda.com.tekmedastockist.util.DeliveredRetailerListAdapter;

public class DeliveredOrdersActivity extends AppCompatActivity {


    private FirebaseUser user;
    private ArrayList<Connections> retailersArrayList;
    private ListView listView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_orders);
        listView = (ListView)findViewById(R.id.lvRetailersListDelivered);
        user = FirebaseAuth.getInstance().getCurrentUser();
        retailersArrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(DeliveredOrdersActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Connections").child("Stockists").child(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Connections connections = ds.getValue(Connections.class);
                    if(connections.getConnectionStatus().equalsIgnoreCase("2"))
                        retailersArrayList.add(connections);
                }
                DeliveredRetailerListAdapter newRequestAdapter = new DeliveredRetailerListAdapter(DeliveredOrdersActivity.this,retailersArrayList);
                listView.setAdapter(newRequestAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),DeliveredMedicineActivity.class);
                intent.putExtra("retailerId",retailersArrayList.get(i).getRetailerId());
                startActivity(intent);
            }
        });
    }
}
