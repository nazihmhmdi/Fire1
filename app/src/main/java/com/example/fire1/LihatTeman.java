package com.example.fire1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.fire1.database.Teman;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LihatTeman extends AppCompatActivity {

    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Teman> daftarTeman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_teman);

        /* Inisialisasi RecyclerView & komponennya
         */
        rvView = (RecyclerView) findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);
        /**
         * Inisialisasi dan mengambil Firebase Database Reference
         */
        database = FirebaseDatabase.getInstance().getReference();
        /**
         * Mengambil data barang dari Firebase Realtime DB
         */
        database.child("Teman").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                daftarTeman = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot :
                        snapshot.getChildren()) {
                    /**
                     * Mapping data pada DataSnapshot ke dalam object Teman
                     * Dan juga menyimpan primary key pada object Teman
                     * untuk keperluan Edit dan Delete data
                     */
                    Teman teman = noteDataSnapshot.getValue(Teman.class);
                    teman.setNama(noteDataSnapshot.getKey());
                    /**
                     * Menambahkan object Barang yang sudah dimapping
                     * ke dalam ArrayList
                     */
                    daftarTeman.add(teman);
                }
                adapter = new AdapterLihatTeman(daftarTeman, LihatTeman.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getDetails()+" "+error.getMessage());
            }
        });
    }
    public static Intent getActIntent(Activity activity){
        return new Intent(activity, LihatTeman.class);
    }
}