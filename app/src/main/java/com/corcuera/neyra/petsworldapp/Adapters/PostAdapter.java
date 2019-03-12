package com.corcuera.neyra.petsworldapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.corcuera.neyra.petsworldapp.Models.Post;
import com.corcuera.neyra.petsworldapp.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvName.setText(mData.get(position).getMascota().getNombre());
        holder.tvTypo.setText(mData.get(position).getMascota().getTipo());
        holder.tvGenero.setText(mData.get(position).getMascota().getGenero());
        holder.tvColor.setText(mData.get(position).getMascota().getColor());
        holder.tvComentario.setText(mData.get(position).getMascota().getComentario());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTypo, tvGenero, tvColor, tvComentario;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.row_postName);
            tvTypo = itemView.findViewById(R.id.row_postTipo);
            tvGenero = itemView.findViewById(R.id.row_postGenero);
            tvColor = itemView.findViewById(R.id.row_postColor);
            tvComentario = itemView.findViewById(R.id.row_postComentario);
        }
    }
}
