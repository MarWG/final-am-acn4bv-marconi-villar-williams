package com.example.eternal_games;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> signInLauncher;

    private EditText etUsuario, etContrasena;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        // Referencias
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        TextView linkRegistro = findViewById(R.id.linkRegistro);
        TextView headerError = findViewById(R.id.headerError);

        // Google Sign-In
        googleSignInClient = GoogleSignInManager.configurarGoogle(this);
        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null) {
                                GoogleSignInManager.autenticarConFirebase(
                                        account.getIdToken(),
                                        firebaseAuth,
                                        this,
                                        () -> {
                                            startActivity(new Intent(this, MainActivity.class));
                                            finish();
                                        }
                                );
                            }
                        } catch (ApiException e) {
                            //Toast.makeText(this, "Error al iniciar con Google", Toast.LENGTH_SHORT).show();
                            //Mostramos error een el header
                            String mensajeError = "Error al iniciar con Google";
                            headerError.setText(mensajeError);
                            headerError.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                            headerError.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        findViewById(R.id.btnLoginGoogle).setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        });
    }
    // Login con email/contraseña. Falta realizar login tradicional con el registro
}