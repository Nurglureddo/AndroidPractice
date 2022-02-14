package com.example.arufureddotask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText loginEmail,loginPassword;
    private Button loginBtn;
    private TextView loginQn;

    private FirebaseAuth Auth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        toolbar=findViewById(R.id.loginToolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Login");

        Auth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        loginEmail=findViewById(R.id.Login);
        loginPassword=findViewById(R.id.Password);
        loginBtn=findViewById(R.id.LoginButton);
        loginQn=findViewById(R.id.LoginRegister);

        loginQn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
            startActivity(intent);
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    loginEmail.setError("Электронная почта обязательна");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    loginEmail.setError("Пароль обязателен");
                    return;
                }
                else{
                    loader.setMessage("Авторизация");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    Auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();
                            }else{
                                String error = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Регистрация прошла неудачно", Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }

                        }
                    });
                }
            }
        });

    }
}