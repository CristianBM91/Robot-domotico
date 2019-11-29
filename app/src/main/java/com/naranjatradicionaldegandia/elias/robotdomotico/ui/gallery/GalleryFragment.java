package com.naranjatradicionaldegandia.elias.robotdomotico.ui.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.naranjatradicionaldegandia.elias.ambos.Imagen;
import com.naranjatradicionaldegandia.elias.robotdomotico.AdaptadorImagenes;
import com.naranjatradicionaldegandia.elias.robotdomotico.R;
import com.naranjatradicionaldegandia.elias.robotdomotico.ui.gallery.GalleryViewModel;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static android.provider.MediaStore.Images.Media.getBitmap;

public class GalleryFragment extends Fragment {
    private View view;
    private GalleryViewModel galleryViewModel;
    private Context context;
    private Imagen imagen;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        context = getContext();
        storageRef = FirebaseStorage.getInstance().getReference();
        bajarFichero();


        view = this.getView();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        Query query = FirebaseFirestore.getInstance()
                .collection("Grabaciones")
                .orderBy("tiempo", Query.Direction.DESCENDING)
                .limit(50);
        FirestoreRecyclerOptions<Imagen> opciones = new FirestoreRecyclerOptions
                .Builder<Imagen>().setQuery(query, Imagen.class).build();
        adaptador = new AdaptadorImagenes(this.getContext(), opciones);
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        return root;
    }

    private StorageReference storageRef;
    private RecyclerView recyclerView;
    private AdaptadorImagenes adaptador;





    @Override
    public void onActivityResult(final int requestCode,
                                 final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1234) {
                String nombreFichero = UUID.randomUUID().toString();
                subirFichero(data.getData(), "imagenes/" + nombreFichero);
                try {
                    Bitmap bitmap = getBitmap(context.getContentResolver(), data.getData());
                    ImageView imageView = this.getView().findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void subirFichero(Uri fichero, String referencia) {
        final StorageReference ref = storageRef.child(referencia);
        UploadTask uploadTask = ref.putFile(fichero);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull
                                          Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful())
                    throw task.getException();
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.e("Almacenamiento", "URL: " + downloadUri.toString());
                } else {
                    Log.e("Almacenamiento", "ERROR: subiendo fichero");
                }
            }
        });
    }


    private void bajarFichero() {
        File localFile = null;
        try {
            localFile = File.createTempFile("image", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String path = localFile.getAbsolutePath();
        Log.d("Almacenamiento", "creando fichero: " + path);
        StorageReference ficheroRef = storageRef.child("imagenes/6cd7dcac-1630-476b-8cc5-4af9672d356f");
        ficheroRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Almacenamiento", "Fichero bajado");
                        ImageView imageView = view.findViewById(R.id.imageView);
                        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("Almacenamiento", "ERROR: bajando fichero");
            }
        });
    }

    public void click(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, 1234);
    }

    @Override public void onStart() {
        super.onStart();
        adaptador.startListening();
    }
    @Override public void onStop() {
        super.onStop();
        adaptador.stopListening();
    }
}