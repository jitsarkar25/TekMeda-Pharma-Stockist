package stockist.tekmeda.com.tekmedastockist;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import stockist.tekmeda.com.tekmedastockist.bean.Medicine;

public class NewMedicineActivity extends AppCompatActivity {

    private FirebaseUser user;
    private EditText name,desc,company,generic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);
        name = (EditText) findViewById(R.id.etMedicineName);
        desc = (EditText) findViewById(R.id.etMedicineDesc);
        company = (EditText) findViewById(R.id.etMedicineCompany);
        generic = (EditText) findViewById(R.id.etMedicineGeneric);
        user= FirebaseAuth.getInstance().getCurrentUser();

    }


    public void SaveMed(View v)
    {
        Medicine medicine = new Medicine();
        medicine.setCompany(company.getText().toString());
        medicine.setName(name.getText().toString());
        medicine.setGeneric(generic.getText().toString());
        medicine.setDesc(desc.getText().toString());
        medicine.setStockistId(user.getUid());
        medicine.setVerified("1");
        int rand = (int)(Math.random()*99999);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("NewMeds").child(rand+"");
        databaseReference.setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"Medicine Added",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
