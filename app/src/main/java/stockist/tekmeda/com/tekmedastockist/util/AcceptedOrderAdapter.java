package stockist.tekmeda.com.tekmedastockist.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import stockist.tekmeda.com.tekmedastockist.AcceptedMedicineActivity;
import stockist.tekmeda.com.tekmedastockist.OrderedMedicineActivity;
import stockist.tekmeda.com.tekmedastockist.R;
import stockist.tekmeda.com.tekmedastockist.bean.Orders;

import static android.content.Context.MODE_PRIVATE;


public class AcceptedOrderAdapter extends ArrayAdapter<Orders> {

    FirebaseUser user;
    Context con;
    ProgressDialog progressDialog;
    public AcceptedOrderAdapter(@NonNull Context context, @NonNull List<Orders> ordersList) {
        super(context, 0, ordersList);
        con=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.acceptedmedicinescontent, parent, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        final Orders orders =getItem(position);
        TextView medicineName = (TextView)convertView.findViewById(R.id.tvorderedMedicineNameAccepted);
        TextView medicineQty = (TextView)convertView.findViewById(R.id.tvorderedMedicineQtyAccepted);
        TextView ordernumb = (TextView)convertView.findViewById(R.id.orderNumberAccepted);
        medicineName.setText(orders.getMedicineName());
        medicineQty.setText(orders.getMedicineQty()+" "+orders.getUnit());

        if(orders.isShowOrderNumber())
        {
            ordernumb.setText("Order# :"+orders.getOrderNumber());
        }
        else{
            ordernumb.setVisibility(View.GONE);
        }

        Button bDelivered = (Button)convertView.findViewById(R.id.bDelivered);
        bDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(con);
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Fetching Data");
                progressDialog.show();
                DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference().child("AcceptedOrders").child("Stockists").child(user.getUid()).child(orders.getRetailerId()).child(orders.getOrderNumber()).child(orders.getOrderId());
                databaseReference.removeValue();
                DatabaseReference databaseReference1  = FirebaseDatabase.getInstance().getReference().child("AcceptedOrders").child("Retailers").child(orders.getRetailerId()).child(user.getUid()).child(orders.getOrderNumber()).child(orders.getOrderId());
                databaseReference1.removeValue();
                orders.setTime(System.currentTimeMillis()+"");
                DatabaseReference databaseReference3  = FirebaseDatabase.getInstance().getReference().child("DeliveredOrders").child("Stockists").child(user.getUid()).child(orders.getRetailerId()).child(orders.getOrderNumber()).child(orders.getOrderId());
                databaseReference3.setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DatabaseReference databaseReference4  = FirebaseDatabase.getInstance().getReference().child("DeliveredOrders").child("Retailers").child(orders.getRetailerId()).child(user.getUid()).child(orders.getOrderNumber()).child(orders.getOrderId());
                        databaseReference4.setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(con,"Order Marked Delivered",Toast.LENGTH_SHORT).show();
                                AcceptedMedicineActivity.ordersList.remove(orders);
                                AcceptedOrderAdapter.this.notifyDataSetChanged();
                                final String retailerName =con.getSharedPreferences("retailspref",MODE_PRIVATE).getString("username","");
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
                                                RequestQueue requestQueue = Volley.newRequestQueue(con);
                                                JSONObject jsonObject = new JSONObject();
                                                String url = "https://fcm.googleapis.com/fcm/send";
                                                final String jsonString = "{\"notification\":{\"title\":\"Order Delivered\",\"body\":\""+retailerName+"has delivered your order\",\"sound\":\"default\"},\"data\": {\"retailerid\": \""+user.getUid()+"\"},\"to\":\""+token[0]+"\"}\n";
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
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });

        return convertView;

    }
}
