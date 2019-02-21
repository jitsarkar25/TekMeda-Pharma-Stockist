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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


import stockist.tekmeda.com.tekmedastockist.AcceptedMedicineActivity;
import stockist.tekmeda.com.tekmedastockist.OrderedMedicineActivity;
import stockist.tekmeda.com.tekmedastockist.R;
import stockist.tekmeda.com.tekmedastockist.bean.Orders;


public class DeliveredOrderAdapter extends ArrayAdapter<Orders> {

    FirebaseUser user;
    Context con;

    public DeliveredOrderAdapter(@NonNull Context context, @NonNull List<Orders> ordersList) {
        super(context, 0, ordersList);
        con=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.deliveredmedicinecontent, parent, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        final Orders orders =getItem(position);
        TextView medicineName = (TextView)convertView.findViewById(R.id.tvorderedMedicineNameDelivered);
        TextView medicineQty = (TextView)convertView.findViewById(R.id.tvorderedMedicineQtyDelivered);
        TextView orderNumb = (TextView)convertView.findViewById(R.id.orderNumberDelivered);
        TextView time = (TextView)convertView.findViewById(R.id.tvDeliveredDate);
        medicineName.setText(orders.getMedicineName());
        medicineQty.setText(orders.getMedicineQty()+" "+orders.getUnit());
        SimpleDateFormat sdfutc= new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        sdfutc.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        Date date = new Date(Long.parseLong(orders.getTime()));
        try {
            Date utcdate=sdfutc.parse(sdfutc.format(date));

            time.setText(sdf.format(utcdate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(orders.isShowOrderNumber())
        {
            orderNumb.setText("Order# :"+orders.getOrderNumber());
        }
        else{
            orderNumb.setVisibility(View.GONE);
        }




        return convertView;

    }
}
