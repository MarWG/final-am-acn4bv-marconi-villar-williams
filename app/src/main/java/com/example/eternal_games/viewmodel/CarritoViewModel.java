package com.example.eternal_games.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eternal_games.model.CarritoItem;
import com.example.eternal_games.repository.FirebaseRepository;

import java.util.ArrayList;
import java.util.List;

public class CarritoViewModel extends ViewModel {

    private final FirebaseRepository repo = new FirebaseRepository();

    private final MutableLiveData<List<CarritoItem>> carrito = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> cantidadTotal = new MutableLiveData<>(0);
    private final MutableLiveData<Double> totalGeneral = new MutableLiveData<>(0.0);

    public LiveData<List<CarritoItem>> getCarrito() { return carrito; }
    public LiveData<Integer> getCantidadTotal() { return cantidadTotal; }
    public LiveData<Double> getTotalGeneral() { return totalGeneral; }

    // Inicializar carrito con datos recibidos
    public void setCarrito(List<CarritoItem> items) {
        carrito.setValue(items);
        recalcular(items);
    }

    // Eliminar un producto del carrito y de Firebase
    public void eliminarProducto(CarritoItem item) {
        String userId = repo.obtenerUserId();
        repo.eliminarDelCarrito(userId, item.producto.id,
                aVoid -> {
                    List<CarritoItem> actual = new ArrayList<>(carrito.getValue());
                    actual.remove(item);
                    setCarrito(actual);
                },
                e -> {
                    // Manejar error si querés exponer un LiveData de mensajes
                }
        );
    }

    // Finalizar compra: elimina todos los productos en Firebase y vacía el carrito
    public void finalizarCompra() {
        List<CarritoItem> items = carrito.getValue();
        if (items == null || items.isEmpty()) return;

        String userId = repo.obtenerUserId();
        int total = 0;
        int cantidad = 0;

        for (CarritoItem item : items) {
            total += item.getTotal();
            cantidad += item.cantidad;

            repo.eliminarDelCarrito(userId, item.producto.id,
                    aVoid -> {}, e -> {});
        }

        // Actualizamos LiveData con los resultados antes de vaciar
        cantidadTotal.setValue(cantidad);
        totalGeneral.setValue((double) total);

        // Vaciar carrito y recalcular
        carrito.setValue(new ArrayList<>());
        recalcular(new ArrayList<>());
    }

    // Recalcular totales
    private void recalcular(List<CarritoItem> items) {
        int cantidad = 0;
        double total = 0.0;
        for (CarritoItem item : items) {
            cantidad += item.cantidad;
            total += item.getTotal();
        }
        cantidadTotal.setValue(cantidad);
        totalGeneral.setValue(total);
    }

}
