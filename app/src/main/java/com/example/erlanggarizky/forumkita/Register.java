package com.example.erlanggarizky.forumkita;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {


    private EditText inputnama, inputemail, inputpassword;
    private Button btndaftar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_register);

        getWindow().setStatusBarColor(ContextCompat.getColor(Register.this, R.color.white));

        auth = FirebaseAuth.getInstance();

        btndaftar = (Button) findViewById(R.id.btn_daftar);
        inputnama = (EditText) findViewById(R.id.input_nama);
        inputemail = (EditText) findViewById(R.id.input_email);
        inputpassword = (EditText) findViewById(R.id.input_password);

        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputnama.getText().toString().trim();
                String email = inputemail.getText().toString().trim();
                String password = inputpassword.getText().toString().trim();


                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(),"Masukan Nama Lengkap",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Masukan alamat email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"masukan password",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password.length()<6){
                    Toast.makeText(getApplicationContext(),"password terlalu pendek, masukan minimal 6 karakter",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Register.this,"failed" + task.getException(),Toast.LENGTH_SHORT).show();
                            }
                            else {

                                FirebaseUser user = auth.getCurrentUser();
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                String uid = user.getUid();

                                HashMap<Object, String> hashMap = new HashMap<>();

                                hashMap.put("name",name);
                                hashMap.put("email",email);
                                hashMap.put("uid",uid);
                                hashMap.put("jurusan","");
                                hashMap.put("universitas","");
                                hashMap.put("image","");

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("USERS");
                                reference.child(uid).setValue(hashMap);
                                startActivity(new Intent(Register.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}
