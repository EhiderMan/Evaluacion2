package com.proyecto.monadoptions.ui.buscar;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.proyecto.monadoptions.R;
import com.proyecto.monadoptions.bd.SQLite;

import java.io.File;

public class BuscarFragment extends Fragment  implements View.OnClickListener{

    private BuscarViewModel mViewModel;

    private Button btnBusca,btnLimpia;
    private EditText etID;
    private TextView tvNombre,tvEspecie,tvHabitat,tvSexo,tvFecha,tvEdoG,tvPeso,tvSts;
    private ImageView ivFoto;
    Uri photo;
    public SQLite sqLite;

    static String i,esp,hbt,n,s,f,edoG,ps,sts;
    static  int bnd=0,idp;

    public static String currentPhotoPath,img="";

    public static BuscarFragment newInstance() {
        return new BuscarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_buscar, container, false);
        sqLite =new SQLite(getContext());

        Componentes(root);

        return root;
    }
    private void Componentes(View root){
        EditTextComponentes(root);
        TextViewComponentes(root);
        ButtonComponentes(root);
        ImageViewComponentes(root);
    }
    private void ImageViewComponentes(View root) {
        ivFoto=root.findViewById(R.id.iVFotoBus);
    }

    private void EditTextComponentes(View root){
        etID=root.findViewById(R.id.tIEtIDBus);
    }
    private void TextViewComponentes (View root){
        tvEspecie=root.findViewById(R.id.tVEspecieBus);
        tvHabitat=root.findViewById(R.id.tVHabitatBus);
        tvNombre=root.findViewById(R.id.tVNombrepBus);
        tvSexo=root.findViewById(R.id.tVSexoBus);
        tvFecha=root.findViewById(R.id.tVFechaBus);
        tvEdoG=root.findViewById(R.id.tVEdoBus);
        tvPeso=root.findViewById(R.id.tVPesoBus);
        tvSts=root.findViewById(R.id.tVStatusBus);
    }
    private void ButtonComponentes(View root){
        btnBusca=root.findViewById(R.id.btnBuscarBus);
        btnLimpia=root.findViewById(R.id.btnLimpiarBus);

        btnLimpia.setOnClickListener(this);
        btnBusca.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBuscarBus:
                if(etID.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Ingrese ID del Animal",Toast.LENGTH_LONG).show();
                    bnd=0;
                }else{
                    sqLite.abrir();
                    idp=Integer.parseInt(etID.getText().toString());
                    if(sqLite.getValorall(idp).getCount()==1){
                        Cursor cursor=sqLite.getValorall(idp);
                        if(cursor.moveToFirst()){
                            do {
                                i = etID.getText().toString();
                                esp = cursor.getString(1);
                                hbt = cursor.getString(2);
                                n = cursor.getString(3);
                                s = cursor.getString(4);
                                f = cursor.getString(5);
                                edoG = cursor.getString(6);
                                ps = cursor.getString(7);
                                sts = cursor.getString(8);
                                img = cursor.getString(9);
                            }while(cursor.moveToNext());

                            tvEspecie.setText("Especie: "+esp);
                            tvHabitat.setText("Doctor: "+hbt);
                            tvNombre.setText("Nombre: "+n);
                            tvSexo.setText("Sexo: "+s);
                            tvFecha.setText("Fecha de ingreso: "+f);
                            tvEdoG.setText("Estado General: "+edoG);
                            tvPeso.setText("Peso: "+ps);
                            tvSts.setText("Status: "+sts);
                            cargarImagen(img,ivFoto);
                            bnd=1;

                        }

                    }else{
                        Toast.makeText(getContext(),"El ID del Animal: "+etID.getText().toString()+"No existe",Toast.LENGTH_LONG).show();
                        bnd=0;
                    }

                    sqLite.cerrar();
                }
                break;

            case R.id.btnLimpiarBus:
                mLimpiar();
                break;


        }
    }
    public void mLimpiar(){
        etID.setText("");
        tvEspecie.setText("Especie: ");
        tvHabitat.setText("Habitat: ");
        tvNombre.setText("Nombre: ");
        tvSexo.setText("Sexo: ");
        tvFecha.setText("Fecha de ingreso: ");
        tvEdoG.setText("Edo. General: ");
        tvPeso.setText("Peso: ");
        tvSexo.setText("Status:");
        ivFoto.setImageResource(R.drawable.ic_menu_camera);
        bnd=0;

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
}