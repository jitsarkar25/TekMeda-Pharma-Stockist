package stockist.tekmeda.com.tekmedastockist.util;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.List;

import stockist.tekmeda.com.tekmedastockist.MyConnectionsActivity;
import stockist.tekmeda.com.tekmedastockist.R;
import stockist.tekmeda.com.tekmedastockist.bean.Connections;
import stockist.tekmeda.com.tekmedastockist.bean.Retailers;

public class DeliveredRetailerListAdapter extends ArrayAdapter<Connections> {

    private String Id;
    private Context con;
    private ProgressDialog progressDialog;
    public DeliveredRetailerListAdapter(@NonNull Context context, @NonNull List<Connections> connectionsList) {
        super(context, 0, connectionsList);
        con=context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.deliveredorderscontents, parent, false);
        progressDialog = new ProgressDialog(con);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Data");
        // progressDialog.show();
        final Connections connections =getItem(position);
        Id = connections.getId();

        final TextView retailName = (TextView)convertView.findViewById(R.id.tvRetailerListNameDelivered);
        final TextView retailAddress = (TextView)convertView.findViewById(R.id.tvStockistAreaDelivered);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Retailers");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {

                    Retailers retailers = ds.getValue(Retailers.class);
                    if(retailers.getId().equalsIgnoreCase(connections.getRetailerId()))
                    {
                        retailName.setText(retailers.getRetailName());
                        retailAddress.setText(retailers.getAddress());
                        //progressDialog.dismiss();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return convertView;

    }
}
