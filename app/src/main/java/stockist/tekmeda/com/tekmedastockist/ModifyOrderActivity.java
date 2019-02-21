package stockist.tekmeda.com.tekmedastockist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import stockist.tekmeda.com.tekmedastockist.bean.Medicine;
import stockist.tekmeda.com.tekmedastockist.bean.Orders;

public class ModifyOrderActivity extends AppCompatActivity {
    private Spinner spinnerUnit,spinnerStockist;
    private AutoCompleteTextView medicineName;
    private EditText quantity;
    private LinearLayout ll;

    private boolean isEdit = false;
    private boolean isReorder = false;
    private boolean isNew = false;
    private String retailerId="";
    private String orderId= "";
    private String orderNumber= "";
    private Set<Medicine> MedicineSet;
    private ArrayList<String> medicineList;
    private boolean isOrderPresent;
    private TextView textView;
    Orders ord;
    private String oldunit,oldqty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_order);
        // stockistId= getIntent().getStringExtra("stockistId");
        //isEdit=getIntent().getBooleanExtra("isEdit",false);
        spinnerUnit = (Spinner)findViewById(R.id.spUnit);
        spinnerStockist = (Spinner)findViewById(R.id.spStockist);
        quantity = (EditText) findViewById(R.id.tvQuanity);
        textView = (TextView) findViewById(R.id.tvMedicineName);


        ArrayList<String> unitList = new ArrayList<String>();
        unitList.add("Bottle");
        unitList.add("Box");
        unitList.add("Carton");
        unitList.add("File");
        unitList.add("Strips");
        unitList.add("Tubes");
        unitList.add("Lot");
        unitList.add("Piece");

        ArrayAdapter<String> unitadapter = new ArrayAdapter<String>(
                this, R.layout.spinner_xml, unitList);

        //   unitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitadapter);

        Orders orders = (Orders) getIntent().getSerializableExtra("orders");
        ord = (Orders) getIntent().getSerializableExtra("orders");
        orderId = orders.getOrderId();
        retailerId=orders.getRetailerId();
        orderNumber = orders.getOrderNumber();
        textView.setText(orders.getMedicineName());
        spinnerUnit.setSelection(unitList.indexOf(orders.getUnit()));
        quantity.setText(orders.getMedicineQty());
        oldqty=orders.getMedicineQty();
        oldunit=orders.getUnit();




    }

    public void modifyOrder(View v){
        if(quantity.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter a quantity",Toast.LENGTH_SHORT).show();
            return;
        }
        if(oldunit.equalsIgnoreCase("Piece") || oldunit.equalsIgnoreCase("Bottle") || oldunit.equalsIgnoreCase("Strips") || oldunit.equalsIgnoreCase("Tubes"))
        {
            if(spinnerUnit.getSelectedItem().toString().equalsIgnoreCase("Box") || spinnerUnit.getSelectedItem().toString().equalsIgnoreCase("Carton") || spinnerUnit.getSelectedItem().toString().equalsIgnoreCase("File"))
            {
                Toast.makeText(getApplicationContext(),"Cannot modify order to a larger unit",Toast.LENGTH_SHORT).show();
                return;
            }
            else if(Double.parseDouble(oldqty)< Double.parseDouble(quantity.getText().toString()))
            {
                Toast.makeText(getApplicationContext(),"Cannot increase medicine quantity",Toast.LENGTH_SHORT).show();
                return;
            }

        }
        else if(oldunit.equalsIgnoreCase("Box") || oldunit.equalsIgnoreCase("File"))
        {
            if(spinnerUnit.getSelectedItem().toString().equalsIgnoreCase("Lot"))
            {
                Toast.makeText(getApplicationContext(),"Cannot modify order to a larger unit",Toast.LENGTH_SHORT).show();
                return;
            }
            else if(spinnerUnit.getSelectedItem().toString().equalsIgnoreCase("Box") || spinnerUnit.getSelectedItem().toString().equalsIgnoreCase("File"))
            {
                if(Double.parseDouble(oldqty)< Double.parseDouble(quantity.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Cannot increase medicine quantity",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        else{
            if(spinnerUnit.getSelectedItem().toString().equalsIgnoreCase("Lot"))
            {
                if(Double.parseDouble(oldqty)< Double.parseDouble(quantity.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Cannot increase medicine quantity",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        }



        final Orders orders = new Orders();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        orders.setRetailerId(retailerId);
        orders.setStockistId(user.getUid());
        orders.setMedicineName(textView.getText().toString());
        orders.setMedicineQty(quantity.getText().toString());
        orders.setUnit(spinnerUnit.getSelectedItem().toString());
        orders.setOrderId(orderId);
        orders.setOrderNumber(orderNumber);
        orders.setModified(true);



        DatabaseReference databaseReferenced1 = FirebaseDatabase.getInstance().getReference().child("PlacedOrders").child("Retailers").child(retailerId).child(user.getUid()).child(orderNumber).child(orderId);
        databaseReferenced1.removeValue();
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("PlacedOrders").child("Stockists").child(user.getUid()).child(retailerId).child(orderNumber).child(orderId);
        databaseReference2.removeValue();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Retailers").child(retailerId).child(user.getUid()).child(orderNumber).child(orderId);
        databaseReference.setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Stockists").child(user.getUid()).child(retailerId).child(orderNumber).child(orderId);
                databaseReference1.setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Order Modified Successfully", Toast.LENGTH_SHORT).show();
                        final String retailerName =getSharedPreferences("retailspref",MODE_PRIVATE).getString("username","");
                        final String[] token = {""};
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("usertokens");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren())
                                {
                                    if(ds.getKey().equalsIgnoreCase(orders.getRetailerId()))
                                    {
                                        token[0] =ds.getValue(String.class);
                                        Log.i("LOG_VOLLEY", "Start");
                                        RequestQueue requestQueue = Volley.newRequestQueue(ModifyOrderActivity.this);
                                        JSONObject jsonObject = new JSONObject();
                                        String url = "https://fcm.googleapis.com/fcm/send";
                                        final String jsonString = "{\"notification\":{\"title\":\"Order Modified\",\"body\":\""+retailerName+"has requested to modify order\",\"sound\":\"default\"},\"data\": {\"retailerid\": \""+user.getUid()+"\"},\"to\":\""+token[0]+"\"}\n";
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.i("LOG_VOLLEY", response);
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("LOG_VOLLEY", error.toString());
                                            }
                                        }) {
                                            @Override
                                            public String getBodyContentType() {
                                                return "application/json; charset=utf-8";
                                            }

                                            @Override
                                            public byte[] getBody() throws AuthFailureError {
                                                try {
                                                    return jsonString == null ? null : jsonString.getBytes("utf-8");
                                                } catch (UnsupportedEncodingException uee) {
                                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonString, "utf-8");
                                                    return null;
                                                }
                                            }

                                            @Override
                                            public Map<String, String> getHeaders() throws AuthFailureError {
                                                Map<String, String>  params = new HashMap<String, String>();
                                                params.put("Content-Type", "application/json");
                                                params.put("Authorization", "key=AAAAEKrp9Vg:APA91bGcsMj-sJlrHBBbBldnb71pQF0mnEINfdvslYFAqzy0u1oeyygwx8OGZGXnfQ_QGwjRDfpv-l01Qzb8B05qvBWkMQ4B6vDFiZZUdmqKm_GbhNW618j2SmrLU8wY5gQarQFQxbd4");
                                                //params.put("Authorization", "key="+token[0]);
                                                return params;
                                            }

                                        };

                                        requestQueue.add(stringRequest);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //startActivity(new Intent(getApplicationContext(),NewOrdersActivity.class));
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE","modified");
                        intent.putExtra("position",getIntent().getIntExtra("position",0));
                        setResult(1002,intent);
                        finish();
                    }
                });
            }
        });

    }


}
