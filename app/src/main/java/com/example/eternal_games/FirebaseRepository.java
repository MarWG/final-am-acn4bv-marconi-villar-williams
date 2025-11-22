package com.example.eternal_games;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    public FirebaseRepository() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseUser obtenerUsuarioActual() {
        return auth.getCurrentUser();
    }

    public boolean estaLogueado() {
        return obtenerUsuarioActual() != null;
    }
}
