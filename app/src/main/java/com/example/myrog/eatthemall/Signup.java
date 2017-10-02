package com.example.myrog.eatthemall;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myrog.eatthemall.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Signup extends AppCompatActivity {

    MaterialEditText edtname,edtpassword,edtphone;
    Button btnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtname = (MaterialEditText)findViewById(R.id.edtname);
        edtpassword = (MaterialEditText)findViewById(R.id.edtpassword);
        edtphone = (MaterialEditText)findViewById(R.id.edtphone);

        btnsignup = (Button) findViewById(R.id.btnsignup);

        //Gọi firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mdialog = new ProgressDialog(Signup.this);
                mdialog.setMessage("Hãy chờ chúng tôi kiểm tra thông tin...");
                mdialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Kiểm tra số điện thoại xem có tồn tại chưa
                        if (dataSnapshot.child(edtphone.getText().toString()).exists()){
                            mdialog.dismiss();
                            Toast.makeText(Signup.this, "Số điện thoại này đã có người sử dụng", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            mdialog.dismiss();
                            User user = new User(edtname.getText().toString(),edtpassword.getText().toString());
                            table_user.child(edtphone.getText().toString()).setValue(user);
                            Toast.makeText(Signup.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }); 
            }
        });
    }
}
