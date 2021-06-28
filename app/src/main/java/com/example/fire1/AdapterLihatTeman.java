package com.example.fire1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fire1.database.Teman;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdapterLihatTeman extends
        RecyclerView.Adapter<AdapterLihatTeman.ViewHolder> {
    private ArrayList<Teman> daftarTeman;
    private Context context;
    private DatabaseReference databaseReference;
    
    public AdapterLihatTeman(ArrayList<Teman> temans, Context ctx){
        /**
         * Inisiasi data dan variabel yang akan digunakan
         */
        daftarTeman = temans;
        context = ctx;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        ViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_namateman);

            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         * Inisiasi ViewHolder
         */
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teman, parent,
                        false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String kode, nama, telpon;

        nama  = daftarTeman.get(position).getNama();
        kode  = daftarTeman.get(position).getKode();
        telpon  = daftarTeman.get(position).getTelpon();

        holder.tvTitle.setText(nama);

        final String name = daftarTeman.get(position).getNama();
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        holder.tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                View anchor;
                PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                popupMenu.inflate(R.menu.menuteman);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.mnEdit:
                                Bundle bundle = new Bundle();
                                bundle.putString("kunci1", kode);
                                bundle.putString("kunci2", nama);
                                bundle.putString("kunci3", telpon);

                                Intent intent = new Intent(view.getContext(), TemanEdit.class);
                                intent.putExtras(bundle);
                                view.getContext().startActivity(intent);
                                break;

                            case R.id.mnHapus:
                                AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
                                alertDlg.setTitle("Yakin data " + nama + " akan dihapus?");
                                alertDlg.setMessage("Tekan 'Ya' untuk menghapus")
                                        .setCancelable(false)
                                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DeleteData(kode);
                                                Toast.makeText(view.getContext(), "Data "+nama+" berhasil dihapus",Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(view.getContext(), MainActivity.class);
                                                view.getContext().startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog aDlg = alertDlg.create();
                                aDlg.show();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
        holder.tvTitle.setText(name);
    }
    @Override
    public int getItemCount() {

        return daftarTeman.size();
    }

    public void DeleteData(String kode){
        if (databaseReference != null) {
            databaseReference.child("Teman").child(kode).removeValue();
        }
    }
}