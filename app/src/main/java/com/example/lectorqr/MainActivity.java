package com.example.lectorqr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button ok;
    EditText user, pass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mp;
    private static final String TAG = "a";
    private boolean vali = false;
    private boolean log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        ok = (Button) findViewById(R.id.ok);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llama funcion login
                if (TextUtils.isEmpty(user.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Se necesita llenar el campo de usaurio",
                            Toast.LENGTH_SHORT).show();
                    vali = false;
                }else if (TextUtils.isEmpty(pass.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Se necesita llenar el campo de contraseña",
                            Toast.LENGTH_SHORT).show();
                    vali = false;
                }else {
                    vali = true;
                }
                if (vali) {
                    LoginUp();
                }

               //

            }

        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        System.out.println("logisssssssssssssssssssssssssssssssssssssssn" + mAuth.getCurrentUser());
        if (currentUser != null){
           // Toast loge = Toast.makeText(getApplicationContext(), "Usuario log", Toast.LENGTH_SHORT);
           // loge.show();
            Intent facil = new Intent(getApplicationContext(), Eventos.class);
            startActivity(facil);//Inicia la actividad
            finish();
        }else{
           // Toast logeOff = Toast.makeText(getApplicationContext(), "Usuario logeOff", Toast.LENGTH_SHORT);
            //logeOff.show();
        }
        //updateUI(currentUser);
    }

    private void LoginUp() {
        //mAuth = FirebaseAuth.getInstance();
        String userSs =  user.getText().toString();
        String passSs  = pass.getText().toString();
       // Toast toast1 = Toast.makeText(getApplicationContext(), userSs, Toast.LENGTH_SHORT);
       // toast1.show();
        mAuth.signInWithEmailAndPassword(userSs, passSs)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast toast1 = Toast.makeText(getApplicationContext(), "Usuario correcto", Toast.LENGTH_SHORT);
                            toast1.show();
                            log = true;
                            if (log){
                                Intent facil = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(facil);//Inicia la actividad
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Usuario invalido",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }

                });

    }


    @Override
    public void onBackPressed (){

    }

}
