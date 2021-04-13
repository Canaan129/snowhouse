package com.neu.snowhouse.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.snowhouse.R;
import com.neu.snowhouse.model.response.LiteMountainResponseModel;

import java.util.ArrayList;
import java.util.Base64;

public class MountainAdapter extends RecyclerView.Adapter<MountainAdapter.MountainViewHolder> {

    ArrayList<LiteMountainResponseModel> mountains;

    public MountainAdapter(ArrayList<LiteMountainResponseModel> mountains) {
        this.mountains = mountains;
    }

    @NonNull
    @Override
    public MountainAdapter.MountainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_mountain_item, parent, false);
        return new MountainViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MountainAdapter.MountainViewHolder holder, int position) {
        final Bitmap[] bmp = new Bitmap[1];
        byte[] bytes = Base64.getDecoder().decode(mountains.get(position).getImage().getPicture());
        bmp[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.imageView.setImageBitmap(bmp[0]);
        holder.titleTextView.setText(mountains.get(position).getMountainName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("mountainId", mountains.get(position).getMountainId());
                Navigation.findNavController(v).navigate(R.id.home_mountain, bundle);
            }
        });
    }

    public void updateAdapter(ArrayList<LiteMountainResponseModel> newMountains) {
        this.mountains = newMountains;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mountains.size();
    }

    static class MountainViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;

        public MountainViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_mountain_name);
            imageView = itemView.findViewById(R.id.item_mountain_image);
        }
    }
}
