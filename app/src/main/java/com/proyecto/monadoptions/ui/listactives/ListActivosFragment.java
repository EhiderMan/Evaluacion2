package com.proyecto.monadoptions.ui.listactives;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.proyecto.monadoptions.R;
import com.proyecto.monadoptions.bd.SQLite;

import java.io.File;
import java.util.ArrayList;

public class ListActivosFragment extends Fragment {
    SQLite sqlite;
    String idusu;
    Editable editable;
    ArrayList<String> registros;
    ArrayList<String> registroid;
    ArrayList<String> imagenes;
    ListView list;
    private ListActivosViewModel mViewModel;

    public static ListActivosFragment newInstance() {
        return new ListActivosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_activos, container, false);

        final TextView textView = root.findViewById(R.id.text_listaractivos);

        list = root.findViewById(R.id.lVListaAnimlesAct);


        sqlite = new SQLite(getContext());
        sqlite.abrir();
        Cursor cursor=sqlite.getRegistroActivo();
        registros=sqlite.getAnimales(cursor);
        registroid=sqlite.getIDall(cursor);
        imagenes=sqlite.getImagenes(cursor);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, registros);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_animal,null);
                ((TextView)dialogView.findViewById(R.id.tvInfoAnimalDialogo)).setText(registros.get(position));
                idusu = registroid.get(position);
                ImageView imageView = dialogView.findViewById(R.id.iVFotoDialogo);
                cargarImagen (imagenes.get(position),imageView);



                AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                dialogo.setTitle("Animal");
                dialogo.setView(dialogView);
                dialogo.setNegativeButton("Inactivar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        aceptarLog();
                    }
                });
                dialogo.setPositiveButton("Aceptar",null);

                dialogo.show();

            }
        });
        sqlite.cerrar();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListActivosViewModel.class);
        // TODO: Use the ViewModel
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
    public void aceptarLog(){
        sqlite.abrir();
        editable = new SpannableStringBuilder(idusu);
        sqlite.EliminacionLog(editable);
        Toast.makeText(getContext(),"Registro eliminado lógicamente",Toast.LENGTH_LONG).show();
        sqlite.cerrar();


        //refrescar fragment y lista

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.attach(this);
        ft.commit();


    }
}