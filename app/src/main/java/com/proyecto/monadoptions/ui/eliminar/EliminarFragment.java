package com.proyecto.monadoptions.ui.eliminar;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class EliminarFragment extends Fragment implements View.OnClickListener{

    private EliminarViewModel mViewModel;

    private Button btnEliminaPerma,btnEliminaLog,btnBusca,btnLimpia;
    private EditText etID;
    private TextView tvNombre,tvEspecie,tvHabitat,tvSexo,tvFecha,tvEdoG,tvPeso,tvSts;
    private ImageView ivFoto;
    Uri photo;
    public SQLite sqLite;

    static String i,esp,hbt,n,s,f,edoG,ps,sts;
    static  int bnd=0,idp;

    public static String currentPhotoPath,img="";

    public static EliminarFragment newInstance() {
        return new EliminarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_eliminar, container, false);

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
        ivFoto=root.findViewById(R.id.iVFotoEL);
    }

    private void EditTextComponentes(View root){
        etID=root.findViewById(R.id.tIEtIDEL);
    }
    private void TextViewComponentes (View root){
        tvEspecie=root.findViewById(R.id.tVEspecieEL);
        tvHabitat=root.findViewById(R.id.tVHabitatEL);
        tvNombre=root.findViewById(R.id.tVNombrepEL);
        tvSexo=root.findViewById(R.id.tVSexoEL);
        tvFecha=root.findViewById(R.id.tVFechapEL);
        tvEdoG=root.findViewById(R.id.tVEdoEL);
        tvPeso=root.findViewById(R.id.tVPesoEL);
        tvSts=root.findViewById(R.id.tVStatusEL);
    }
    private void ButtonComponentes(View root){
        btnBusca=root.findViewById(R.id.btnBuscarEL);
        btnLimpia=root.findViewById(R.id.btnLimpiarEL);
        btnEliminaPerma=root.findViewById(R.id.btnEliminarPer);
        btnEliminaLog=root.findViewById(R.id.btnEliminarLog);


        btnLimpia.setOnClickListener(this);
        btnBusca.setOnClickListener(this);
        btnEliminaPerma.setOnClickListener(this);
        btnEliminaLog.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBuscarEL:
                if(etID.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Ingrese ID del Animal",Toast.LENGTH_LONG).show();
                    bnd=0;
                }else{
                    sqLite.abrir();
                    idp=Integer.parseInt(etID.getText().toString());
                    if(sqLite.getValorActivos(idp).getCount()==1){
                        Cursor cursor=sqLite.getValorActivos(idp);
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

            case R.id.btnLimpiarEL:
                mLimpiar();
                break;
            case R.id.btnEliminarPer:
                if(bnd==1){
                    View dialogView=LayoutInflater.from(getContext()).inflate(R.layout.dialog_animal,null);
                    ((TextView)dialogView.findViewById(R.id.tvInfoAnimalDialogo)).setText("¿Desea eliminar el Registro Permanentemente? \n"+
                            "ID ["+ i + "]\n"+
                            "Especie ["+esp+"]\n"+
                            "Habitat ["+hbt+"]\n"+
                            "Nombre ["+n+"]\n"+
                            "Sexo ["+s+"]\n"+
                            "Fecha de Ingreso ["+f+"]\n"+
                            "Estado General ["+edoG+"]\n"+
                            "Peso ["+ps+"]\n"+
                            "Status ["+sts+"]\n");
                    ImageView image=dialogView.findViewById(R.id.iVFotoDialogo);
                    cargarImagen(img,image);
                    AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
                    dialogo.setTitle("Importante");
                    dialogo.setView(dialogView);
                    dialogo.setCancelable(false);
                    dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            aceptarPermanente();
                            mLimpiar();;
                            bnd=0;
                        }
                    });
                    dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(),"Registro aun Activo",Toast.LENGTH_LONG).show();
                            bnd=1;
                        }
                    });
                    dialogo.show();
                }else{
                    Toast.makeText(getContext(),"Ingrese ID de paciente",Toast.LENGTH_LONG).show();
                    bnd=0;
                }
                break;
            case R.id.btnEliminarLog:
                if(bnd==1){
                    View dialogView2=LayoutInflater.from(getContext()).inflate(R.layout.dialog_animal,null);
                    ((TextView)dialogView2.findViewById(R.id.tvInfoAnimalDialogo)).setText("¿Desea eliminar el Registro Lógicamente? \n"+
                            "ID ["+ i + "]\n"+
                            "Especie ["+esp+"]\n"+
                            "Habitat ["+hbt+"]\n"+
                            "Nombre ["+n+"]\n"+
                            "Sexo ["+s+"]\n"+
                            "Fecha de Ingreso ["+f+"]\n"+
                            "Estado General ["+edoG+"]\n"+
                            "Peso ["+ps+"]\n"+
                            "Status ["+sts+"]\n");
                    ImageView image2=dialogView2.findViewById(R.id.iVFotoDialogo);
                    cargarImagen(img,image2);
                    AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
                    dialogo.setTitle("Importante");
                    dialogo.setView(dialogView2);
                    dialogo.setCancelable(false);
                    dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            aceptarLog();
                            mLimpiar();;
                            bnd=0;
                        }
                    });
                    dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(),"Registro aun Activo",Toast.LENGTH_LONG).show();
                            bnd=1;
                        }
                    });
                    dialogo.show();
                }else{
                    Toast.makeText(getContext(),"Ingrese ID de paciente",Toast.LENGTH_LONG).show();
                    bnd=0;
                }
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
    public void aceptarPermanente(){
        sqLite.abrir();
        sqLite.EliminarPermanente(etID.getText());
        Toast.makeText(getContext(),"Registro eliminado",Toast.LENGTH_LONG).show();
        sqLite.cerrar();
    }
    public void aceptarLog(){
        sqLite.abrir();
        sqLite.EliminacionLog(etID.getText());
        Toast.makeText(getContext(),"Registro eliminado lógicamente",Toast.LENGTH_LONG).show();
        sqLite.cerrar();
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