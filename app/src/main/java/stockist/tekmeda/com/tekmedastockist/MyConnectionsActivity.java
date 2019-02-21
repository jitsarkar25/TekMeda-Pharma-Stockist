package stockist.tekmeda.com.tekmedastockist;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import stockist.tekmeda.com.tekmedastockist.bean.Connections;
import stockist.tekmeda.com.tekmedastockist.bean.Retailers;
import stockist.tekmeda.com.tekmedastockist.util.NewRequestAdapter;
import stockist.tekmeda.com.tekmedastockist.util.RetaileConnectionAdapter;

public class MyConnectionsActivity extends AppCompatActivity {

    private FirebaseUser user;
    private TextView tvSearchedStockist;
    private ListView newRequests,connectedRetails;
    public static ArrayList<Connections> connectionsArrayList,connectedArrayList;
    private Boolean req=false;
    private boolean isPresent=false;
    private boolean stockistExists =false;
    private Retailers searchedRetailer;
    public static RetaileConnectionAdapter retaileConnectionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_connections);
        user= FirebaseAuth.getInstance().getCurrentUser();
        tvSearchedStockist = (TextView) findViewById(R.id.tvSearchedStockistName) ;
        newRequests = (ListView) findViewById(R.id.lvNewRequest);
        connectedRetails = (ListView) findViewById(R.id.lvMyConnections);
        connectionsArrayList = new ArrayList<>();
        connectedArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Connections").child("Stockists").child(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Connections connections = ds.getValue(Connections.class);
                    if(connections.getConnectionStatus().equalsIgnoreCase("1") && !connections.getSender().equalsIgnoreCase("Stockist"))
                    {
                        connectionsArrayList.add(connections);
                        req=true;
                    }
                    else if(connections.getConnectionStatus().equalsIgnoreCase("2"))
                        connectedArrayList.add(connections);
                }
                NewRequestAdapter newRequestAdapter = new NewRequestAdapter(MyConnectionsActivity.this,connectionsArrayList);
                newRequests.setAdapter(newRequestAdapter);

                retaileConnectionAdapter = new RetaileConnectionAdapter(MyConnectionsActivity.this,connectedArrayList);
                connectedRetails.setAdapter(retaileConnectionAdapter);
                if(req!=true)
                    ((TextView)findViewById(R.id.tvNewReq)).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void searchedStockist(View v)
    {
        EditText et = (EditText) findViewById(R.id.etStockistId);
        final String stId = et.getText().toString();
        stockistExists=false;
       /* if(stId.equals("123456"))
        {
            ((LinearLayout)findViewById(R.id.searchResult)).setVisibility(View.VISIBLE);
        }u
        else
        {
            Toast.makeText(getApplicationContext(),"Stockist not found",Toast.LENGTH_SHORT).show();
        }*/

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Retailers");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getKey().equalsIgnoreCase(stId))
                    {
                        ((LinearLayout)findViewById(R.id.searchResult)).setVisibility(View.VISIBLE);
                        searchedRetailer = ds.getValue(Retailers.class);
                        tvSearchedStockist.setText(searchedRetailer.getRetailName());
                        stockistExists=true;
                        break;
                    }
                }
                if(!stockistExists)
                {
                    Toast.makeText(getApplicationContext(),"Retailer not found",Toast.LENGTH_SHORT).show();
                    ((LinearLayout)findViewById(R.id.searchResult)).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void SendReq(View v)
    {
        isPresent=false;
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Connections").child("Stockists").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Connections connections = ds.getValue(Connections.class);
                    if(connections.getRetailerId().equalsIgnoreCase(searchedRetailer.getId()) )
                    {
                        if(connections.getConnectionStatus().equalsIgnoreCase("1"))
                            Toast.makeText(getApplicationContext(),"Connection Request already sent",Toast.LENGTH_SHORT).show();
                        else if(connections.getConnectionStatus().equalsIgnoreCase("2"))
                            Toast.makeText(getApplicationContext(),"Connection already present",Toast.LENGTH_SHORT).show();

                        isPresent=true;
                    }

                }
                if(!isPresent) {
                    final Connections connections = new Connections();
                    connections.setStockistId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    connections.setRetailerId(searchedRetailer.getId());
                    connections.setConnectionStatus("1");
                    connections.setSender("Stockist");
                    final int random = (int) (Math.random() * 99999999);
                    connections.setId(random + "");

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Connections").child("Stockists").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random + "");
                    databaseReference.setValue(connections).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Connections").child("Retailers").child(searchedRetailer.getId()).child(random + "");
                            databaseReference2.setValue(connections).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Request Sent To Stockist", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
