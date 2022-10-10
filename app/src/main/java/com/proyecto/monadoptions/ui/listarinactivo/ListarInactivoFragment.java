package com.proyecto.monadoptions.ui.listarinactivo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.proyecto.monadoptions.R;
import com.proyecto.monadoptions.bd.SQLite;

import java.io.File;
import java.util.ArrayList;

public class ListarInactivoFragment extends Fragment {
    ArrayList<String> registros;
    ArrayList<String> imagenes;
    ArrayList<String> registroid;
    public SQLite sqlite;

    private ListarInactivoViewModel mViewModel;

    public static ListarInactivoFragment newInstance() {
        return new ListarInactivoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_listar_inactivo, container, false);
        ListView list = root.findViewById(R.id.lVListaAnimlesINAct);

        sqlite = new SQLite(getContext());
        sqlite.abrir();
        Cursor cursor=sqlite.getRegistroINactivo();
        registros=sqlite.getAnimales(cursor);
        imagenes=sqlite.getImagenes(cursor);
        registroid=sqlite.getIDall(cursor);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, registros);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_animal,null);
                ((TextView)dialogView.findViewById(R.id.tvInfoAnimalDialogo)).setText(registros.get(position));
                ImageView imageView = dialogView.findViewById(R.id.iVFotoDialogo);
                cargarImagen (imagenes.get(position),imageView);


                AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                dialogo.setTitle("Animal");
                dialogo.setView(dialogView);
                dialogo.setPositiveButton("Aceptar",null);
                dialogo.setNeutralButton("Activar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        activar(registroid.get(position));
                    }
                });
                dialogo.show();

            }
        });
        sqlite.cerrar();
        return root;
    }

    public void cargarImagen(String imagen, ImageView iv){
        try {
            File filePhoto = new File(imagen);
            Uri uriPhoto = FileProvider.getUriForFile(getContext(),"com.proyecto.monadoptions",filePhoto);
            iv.setImageURI(uriPhoto);
        }catch (Exception ex){
            Toast.makeText(getContext(),"Ocurrio un error al cargar la imagen",Toast.LENGTH_SHORT).show();
            Log.d("Cargar Imagen","Error al cargar imagen: "+imagen+"\nMensaje: "+
                    ex.getMessage()+"\nCausa: "+ex.getCause());
        }

    }
    public void activar(String id){
        sqlite.abrir();
        int idint= Integer.parseInt(id);
        sqlite.StatusActivo(idint);
        Toast.makeText(getContext(),"Animal Activado",Toast.LENGTH_LONG).show();
        sqlite.cerrar();

        //refrescar fragment y lista
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }



}