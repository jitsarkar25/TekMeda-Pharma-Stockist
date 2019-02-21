package stockist.tekmeda.com.tekmedastockist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import stockist.tekmeda.com.tekmedastockist.bean.Retailers;
import stockist.tekmeda.com.tekmedastockist.bean.Stockists;

public class ProfileDetailsActivity extends AppCompatActivity {

    EditText etFirstName,etLastName,etEmail,etPhone,etState,etPin,etAddress,etCity,etStoreName,etStreet;
    private boolean isEdit=false;
    private ProgressDialog progressDialog;
    private Stockists stockists;
    private TextView tvId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPhone = (EditText)findViewById(R.id.etphone);
        etState = (EditText)findViewById(R.id.etState);
        etPin = (EditText)findViewById(R.id.etPIN);
        etAddress = (EditText)findViewById(R.id.etStoreAddress);
        etCity = (EditText)findViewById(R.id.etCity);
        etStoreName = (EditText)findViewById(R.id.etStoreName);
        etStreet = (EditText)findViewById(R.id.etStreet);
        tvId = (TextView) findViewById(R.id.tvId);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        etEmail.setText(user.getEmail());
        tvId.setText("ID : " + user.getUid());
        isEdit = getIntent().getBooleanExtra("edit",false);
        if(isEdit)
        {
            progressDialog = new ProgressDialog(ProfileDetailsActivity.this);
            progressDialog.setMessage("Fetching Details");
            progressDialog.setTitle("Please Wait");
            progressDialog.show();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stockists");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals(user.getUid())) {
                            progressDialog.dismiss();
                            stockists = child.getValue(Stockists.class);
                            etFirstName.setText(stockists.getFirstName());
                            etLastName.setText(stockists.getLastName());
                            etEmail.setText(stockists.getEmailid());
                            etStoreName.setText(stockists.getEnterpriseName());
                            etPhone.setText(stockists.getPhone());
                            etState.setText(stockists.getState());
                            etCity.setText(stockists.getCity());
                            etPin.setText(stockists.getPin());
                            etAddress.setText(stockists.getAddress());
                            etStreet.setText(stockists.getStreet());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        }
    }
    public void saveuserdetails(View v)
    {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        Stockists stockists = new Stockists();
        stockists.setFirstName(etFirstName.getText().toString());
        stockists.setLastName(etLastName.getText().toString());
        stockists.setEmailid(etEmail.getText().toString());
        stockists.setPhone(etPhone.getText().toString());
        stockists.setState(etState.getText().toString());
        stockists.setPin(etPin.getText().toString());
        stockists.setAddress(etAddress.getText().toString());
        stockists.setCity(etCity.getText().toString());
        stockists.setEnterpriseName(etStoreName.getText().toString());
        stockists.setId(user.getUid());
        stockists.setStreet(etStreet.getText().toString());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stockists").child(user.getUid());
        databaseReference.setValue(stockists);

        Toast.makeText(getApplicationContext(),"Profile updated",Toast.LENGTH_SHORT).show();
        if(!isEdit)
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        finish();

    }
}
