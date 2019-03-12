package com.corcuera.neyra.petsworldapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.corcuera.neyra.petsworldapp.Models.Post;
import com.corcuera.neyra.petsworldapp.R;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyViewHolder> {

    private Context mContext;
    private List<Post> mData;
    /*private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }*/

    public MyPostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myrow = LayoutInflater.from(mContext).inflate(R.layout.row_my_post_item, parent, false);
        return new MyViewHolder(myrow);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostAdapter.MyViewHolder holder, int position) {
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
        private TextView tvName, tvTypo, tvGenero, tvColor, tvComentario;

        public MyViewHolder(View view) {
            super(view);

            tvName = itemView.findViewById(R.id.row_my_postName);
            tvTypo = itemView.findViewById(R.id.row_my_postTipo);
            tvGenero = itemView.findViewById(R.id.row_my_postGenero);
            tvColor = itemView.findViewById(R.id.row_my_postColor);
            tvComentario = itemView.findViewById(R.id.row_my_postComentario);

        }
    }
}
