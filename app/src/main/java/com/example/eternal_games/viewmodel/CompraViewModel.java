package com.example.eternal_games.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eternal_games.model.CarritoItem;
import com.example.eternal_games.model.Compra;
import com.example.eternal_games.repository.CompraRepository;

import java.util.List;

public class CompraViewModel extends AndroidViewModel {

    private final CompraRepository repo = CompraRepository.getInstance();

    // LiveData que expone la lista de compras
    public LiveData<List<Compra>> getCompras() {
        return repo.getCompras();
    }

    // LiveData para una compra seleccionada
    private final MutableLiveData<Compra> compraSeleccionada = new MutableLiveData<>();
    public LiveData<Compra> getCompraSeleccionada() {
        return compraSeleccionada;
    }

    // Nuevo LiveData para saber si hay compras no leídas
    private final MutableLiveData<Boolean> hayNotificacionesNoLeidas = new MutableLiveData<>(false);
    public LiveData<Boolean> getHayNotificacionesNoLeidas() {
        return hayNotificacionesNoLeidas;
    }
    public CompraViewModel(@NonNull Application application) {
        super(application);
        // Enganchamos el repo apenas se crea el VM
        repo.getCompras().observeForever(compras -> {
            if (compras != null) {
                boolean hayNoLeidas = compras.stream().anyMatch(c -> !c.leido);
                hayNotificacionesNoLeidas.setValue(hayNoLeidas);
            }
        });
    }

    // Cargar todas las compras del usuario desde Firebase
    public void cargarCompras() {
        repo.cargarCompras();
        // cada vez que se cargan, evaluamos si hay alguna no leída
        repo.getCompras().observeForever(compras -> {
            if (compras != null) {
                boolean hayNoLeidas = compras.stream().anyMatch(c -> !c.isLeido());
                hayNotificacionesNoLeidas.setValue(hayNoLeidas);
            }
        });
    }

    // Registrar una nueva compra
    public void registrarCompra(List<CarritoItem> items) {
        repo.registrarCompra(items);
    }

    // Actualizar campo 'leido'
    public void marcarComoLeido(String compraId, boolean leido) {
        repo.marcarComoLeido(compraId, leido);
        // refrescar estado global
        cargarCompras();
    }

    // Cargar una compra específica
    public void cargarCompra(String compraId) {
        repo.getCompra(compraId,
                compraSeleccionada::setValue,
                e -> Log.e("ViewModel", "Error al cargar compra", e));
    }
}
