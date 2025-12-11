package com.example.eternal_games.viewmodel;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eternal_games.GoogleSignInManager;
import com.example.eternal_games.repository.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Context;
public class SesionViewModel extends ViewModel {

    private FirebaseRepository repo = new FirebaseRepository();

    private MutableLiveData<Boolean> usuarioLogueado = new MutableLiveData<>();
    private MutableLiveData<String> usuarioMail = new MutableLiveData<>();
    private MutableLiveData<Boolean> sesionCerrada = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registroExitoso = new MutableLiveData<>();
    private final MutableLiveData<String> errorMensaje = new MutableLiveData<>();


    public LiveData<Boolean> getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public LiveData<String> getUsuarioMail() {
        return usuarioMail;
    }

    public LiveData<Boolean> getSesionCerrada() {
        return sesionCerrada;
    }
    public LiveData<String> getErrorMensaje() {
        return errorMensaje;
    }

    public LiveData<Boolean> getRegistroExitoso() {
        return registroExitoso;
    }

    // Verifica si hay sesión activa
    public void verificarSesion() {
        usuarioLogueado.setValue(repo.estaLogueado());
        if (repo.estaLogueado()) {
            usuarioMail.setValue(repo.obtenerMailActual());
        }
    }

    // Cierra sesión
    public void cerrarSesion() {
        repo.cerrarSesion();
        sesionCerrada.setValue(true);
    }

    // Registro con email/contraseña
    public void registrarUsuario(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            errorMensaje.setValue("Completa todos los campos");
            return;
        }

        if (password.length() < 6) {
            errorMensaje.setValue("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        repo.registrarUsuario(email, password,
                authResult -> registroExitoso.setValue(true),
                error -> errorMensaje.setValue("Error: " + error.getMessage())
        );
    }

    // Login con Google (sirve tanto para registro como login)
    public void loginConGoogle(String idToken, FirebaseAuth auth, Context context) {
        GoogleSignInManager.autenticarConFirebase(idToken, auth, context,
                () -> {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        repo.crearDocumentoUsuario(user,
                                aVoid -> {
                                    usuarioLogueado.setValue(true);
                                    usuarioMail.setValue(user.getEmail());
                                },
                                error -> errorMensaje.setValue("Error Firestore: " + error.getMessage())
                        );
                    } else {
                        errorMensaje.setValue("Error: usuario nulo");
                    }
                }
        );
    }

    // Login con email/contraseña
    public void loginUsuario(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            errorMensaje.setValue("Completa todos los campos");
            return;
        }

        repo.login(email, password,
                user -> {
                    usuarioLogueado.setValue(true);
                    usuarioMail.setValue(user.getEmail());
                },
                error -> errorMensaje.setValue("Error: " + error.getMessage())
        );
    }


}