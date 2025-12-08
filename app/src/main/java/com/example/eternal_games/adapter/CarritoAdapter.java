package com.example.eternal_games.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eternal_games.R;
import com.example.eternal_games.model.CarritoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private Context context;
    private List<CarritoItem> carrito;
    private OnItemClickListener listener;

    // Nueva interface simplificada
    public interface OnItemClickListener {
        void onEliminarClick(CarritoItem item);
    }

    public CarritoAdapter(Context context, List<CarritoItem> carrito, OnItemClickListener listener) {
        this.context = context;
        this.carrito = carrito;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        CarritoItem item = carrito.get(position);

        holder.txtTitulo.setText(item.producto.title);
        holder.txtPrecioUnitario.setText("Precio unitario: " + item.producto.price);
        holder.txtCantidad.setText("Cantidad: " + item.cantidad);
        holder.txtTotal.setText("Total: " + item.getTotal());

        if (item.producto.imgUrl != null && !item.producto.imgUrl.isEmpty()) {
            Picasso.get()
                    .load(item.producto.imgUrl)
                    .placeholder(R.drawable.imagen_no_disponible)
                    .error(R.drawable.imagen_no_disponible)
                    .into(holder.imgProducto);
        } else {
            holder.imgProducto.setImageResource(R.drawable.imagen_no_disponible);
        }

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEliminarClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carrito.size();
    }

    // Método para actualizar lista desde el ViewModel
    public void setItems(List<CarritoItem> nuevosItems) {
        this.carrito = nuevosItems;
        notifyDataSetChanged();
    }

    public static class CarritoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto, btnEliminar;
        TextView txtTitulo, txtPrecioUnitario, txtCantidad, txtTotal;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtPrecioUnitario = itemView.findViewById(R.id.txtPrecioUnitario);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
