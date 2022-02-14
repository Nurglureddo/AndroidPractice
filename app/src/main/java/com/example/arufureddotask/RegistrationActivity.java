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

import org.w3c.dom.Text;

public class RegistrationActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private EditText RegLogin,RegPassword;
    private Button RegBtn;
    private TextView RegQn;
    private FirebaseAuth Auth;

    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

            toolbar=findViewById(R.id.RegistrationToolBar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setTitle("Registration");

            Auth = FirebaseAuth.getInstance();
            loader = new ProgressDialog(this);

            RegLogin =findViewById(R.id.RegistrationLogin);
            RegPassword=findViewById(R.id.RegistrationPassword);
            RegBtn =findViewById(R.id.RegistrationLoginButton);
            RegQn =findViewById(R.id.RegisterQuestion);

        RegQn.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
            startActivity(intent);
        });

        RegBtn.setOnClickListener(v -> {
            String email = RegLogin.getText().toString().trim();
            String password = RegPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)){
                RegLogin.setError("Введите email");
                return;
            }
            if(TextUtils.isEmpty(password)){
                RegPassword.setError("Введите пароль");
                return;
            }else{
                loader.setMessage("Регистарция");
                loader.setCanceledOnTouchOutside(false);
                loader.show();
                Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(RegistrationActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                            loader.dismiss();
                        }else{
                            String error = task.getException().toString();
                             Toast.makeText(RegistrationActivity.this, "Регистрация прошла неудачно", Toast.LENGTH_SHORT).show();
                              loader.dismiss();
                        }

                    }
                });
            }


        });
    }
}