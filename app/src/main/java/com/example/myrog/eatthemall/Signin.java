package com.example.myrog.eatthemall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myrog.eatthemall.Common.Common;
import com.example.myrog.eatthemall.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Signin extends AppCompatActivity {

    EditText edtphone,edtpassword;
    Button btnsignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edtphone = (MaterialEditText)findViewById(R.id.edtphone);
        edtpassword=(MaterialEditText)findViewById(R.id.edtpassword);
        btnsignin = (Button) findViewById(R.id.btnsignin);

        // Gọi firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnsignin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Dialog hiển thị thông báo cho người dùng , trong khi chờ đăng nhập
                final ProgressDialog mdialog = new ProgressDialog(Signin.this);
                mdialog.setMessage("Hãy chờ chúng tôi kiểm tra thông tin...");
                mdialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Hàm lấy thông tin user
                        //Kiểm tra user có tồn tại hay ko
                        if (dataSnapshot.child(edtphone.getText().toString()).exists()){
                            mdialog.dismiss();
                            User user = dataSnapshot.child(edtphone.getText().toString()).getValue(User.class);
                            // Điều kiện đúng để get user thông tin
                            if (user.getMatkhau().equals(edtpassword.getText().toString())) {
                                Intent homeintent = new Intent(Signin.this,Home.class);
                                Common.currentUser = user;
                                startActivity(homeintent);
                                finish();


                            }
                            else {
                                Toast.makeText(Signin.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            mdialog.dismiss();
                            Toast.makeText(Signin.this, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
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
