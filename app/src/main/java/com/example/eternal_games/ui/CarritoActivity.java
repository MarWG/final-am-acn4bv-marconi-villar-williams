package com.example.eternal_games.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eternal_games.R;
import com.example.eternal_games.adapter.CarritoAdapter;
import com.example.eternal_games.model.CarritoItem;
import com.example.eternal_games.viewmodel.CarritoViewModel;

import java.util.ArrayList;
import java.util.List;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView recyclerCarrito;
    private TextView txtTotalGeneral;
    private TextView txtCantidadTotal;
    private Button btnFinalizarCompra;
    private CarritoAdapter adapter;
    private CarritoViewModel carritoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        // Referencias visuales
        recyclerCarrito = findViewById(R.id.recyclerCarrito);
        txtTotalGeneral = findViewById(R.id.txtTotalGeneral);
        txtCantidadTotal = findViewById(R.id.txtCantidadTotal);
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);
        ImageButton btnInicio = findViewById(R.id.btnInicio);

        // ViewModel
        carritoViewModel = new ViewModelProvider(this).get(CarritoViewModel.class);

        // Adapter con listener simplificado
        adapter = new CarritoAdapter(this, new ArrayList<>(), item -> {
            carritoViewModel.eliminarProducto(item);
            Toast.makeText(this, item.producto.title + " eliminado del carrito", Toast.LENGTH_SHORT).show();
        });

        recyclerCarrito.setLayoutManager(new LinearLayoutManager(this));
        recyclerCarrito.setAdapter(adapter);

        // Observamos el carrito
        carritoViewModel.getCarrito().observe(this, items -> adapter.setItems(items));

        // Observamos totales
        carritoViewModel.getCantidadTotal().observe(this, cantidad ->
                txtCantidadTotal.setText("Cantidad: " + cantidad));
        carritoViewModel.getTotalGeneral().observe(this, total ->
                txtTotalGeneral.setText("Total: $" + (double) total));

        // Inicializamos con datos recibidos del intent
        ArrayList<CarritoItem> carritoInicial =
                (ArrayList<CarritoItem>) getIntent().getSerializableExtra("carrito");
        if (carritoInicial != null) {
            carritoViewModel.setCarrito(carritoInicial);
        }

        // Botón finalizar compra
        btnFinalizarCompra.setOnClickListener(v -> {
            List<CarritoItem> items = carritoViewModel.getCarrito().getValue();
            if (items == null || items.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            // Capturamos los valores antes de vaciar
            int total = carritoViewModel.getTotalGeneral().getValue().intValue();
            int cantidad = carritoViewModel.getCantidadTotal().getValue();

            carritoViewModel.finalizarCompra(); // ahora sí vaciamos

            // Inflamos el layout personalizado
            View view = getLayoutInflater().inflate(R.layout.compra_finalizada, null);

            TextView txtResumen = view.findViewById(R.id.txtResumen);
            txtResumen.setText("Total: $" + total + "\nCantidad: " + cantidad);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .create();

            Button btnCerrar = view.findViewById(R.id.btnCerrar);
            btnCerrar.setOnClickListener(cerrarView -> {
                dialog.dismiss();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });

            dialog.show();

            // Devolver carrito vacío y cantidad al MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("carritoActualizado", new ArrayList<>(carritoViewModel.getCarrito().getValue()));
            resultIntent.putExtra("cantidadTotal", cantidad);
            setResult(RESULT_OK, resultIntent);
        });

        // Botón volver a MainActivity
        btnInicio.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("carritoActualizado", new ArrayList<>(carritoViewModel.getCarrito().getValue()));
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
