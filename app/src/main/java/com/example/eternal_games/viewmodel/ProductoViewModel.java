package com.example.eternal_games.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eternal_games.model.CarritoItem;
import com.example.eternal_games.model.Producto;
import com.example.eternal_games.repository.CartRepository;
import com.example.eternal_games.repository.ProductoRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductoViewModel extends ViewModel {

    // Repo compartido (cache en memoria + sync Firebase)
    private final CartRepository cartRepo = CartRepository.getInstance();

    // Productos (solo catálogo)
    private final MutableLiveData<List<Producto>> productos = new MutableLiveData<>();

    // Mensajes para UI
    private final MutableLiveData<String> mensajeToast = new MutableLiveData<>();


    public LiveData<List<Producto>> getProductos() {
        return productos;
    }

    // 🔥 El carrito viene del repo compartido
    public LiveData<List<CarritoItem>> getCarrito() {
        return cartRepo.getCarrito();
    }

    public LiveData<String> getMensajeToast() {
        return mensajeToast;
    }


    // Carga solo productos (no mezcla con carrito)
    public void cargarProductos() {
        ProductoRepository.cargarDesdeFirebase(null, productos::setValue);
    }

    // Carga carrito SOLO una vez (si no estaba cargado)
    public void cargarCarritoSiHaceFalta() {
        cartRepo.cargarDesdeFirebaseSiHaceFalta();
    }


    public void agregarAlCarrito(Producto producto) {
        if (producto == null) return;

        cartRepo.agregar(producto); // actualiza memoria + escribe en Firebase
        mensajeToast.setValue(producto.title + " agregado al carrito");
    }


    public int calcularCantidadTotal(List<CarritoItem> carritoItems) {
        int total = 0;
        if (carritoItems != null) {
            for (CarritoItem item : carritoItems) {
                total += item.cantidad;
            }
        }
        return total;
    }


    public void insertarProductosDemo() {
        ProductoRepository.obtenerProductosDemo();
        mensajeToast.setValue("Productos demo insertados");
        cargarProductos();
    }
    public void inicializarDatos() {
        ProductoRepository.cargarDesdeFirebase(null, productosList -> {
            productos.setValue(productosList);

            cartRepo.cargarRawDesdeFirebase(rawItems -> {

                List<CarritoItem> conDetalles = new ArrayList<>();

                if (rawItems != null) {
                    for (CarritoItem item : rawItems) {
                        Producto encontrado = null;

                        for (Producto p : productosList) {
                            if (p.id != null && p.id.equals(item.producto.id)) {
                                encontrado = p;
                                break;
                            }
                        }

                        if (encontrado != null) {
                            conDetalles.add(new CarritoItem(encontrado, item.cantidad));
                        }
                    }
                }

                //setea una sola vez el carrito “hidratado”
                cartRepo.setCarrito(conDetalles);
            });
        });
    }


}
