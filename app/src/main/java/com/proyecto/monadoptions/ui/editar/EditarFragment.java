package com.proyecto.monadoptions.ui.editar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.proyecto.monadoptions.R;
import com.proyecto.monadoptions.bd.SQLite;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditarFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener{

    private Button btnLimpia, btnGuarda,btnBusca;
    private ImageButton btnCalen;
    private EditText etId, etNombre, etFecha, etEstadoG, etPeso;
    private Spinner spnEspecie, spnHabt, spnsx,spnstatus;
    private ImageView ivFoto;

    DatePickerDialog dpd;
    Calendar c;
    private static int anio,mes,dia;

    private Uri photoURI;
    public static String currentPhotoPath, img="",esp,hbt,sex,sts;
    public static final int REQUEST_TAKE_PHOTO=1;

    static String i,n,s,f,edoG,ps;
    static  int bnd=0,idp,idpfinal;

    public SQLite sqLite;

    private EditarViewModel mViewModel;

    public static EditarFragment newInstance() {
        return new EditarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_editar, container, false);

        sqLite= new SQLite(getContext());

        Componentes(root);

        return root;
    }
    private void Componentes(View root){

        EditTextComponent(root);
        BotonesComponentes(root);
        SpinenrComponent(root);
    }
    private void EditTextComponent(View root){
        etId=root.findViewById(R.id.tIEtIDED);
        etNombre=root.findViewById(R.id.tIEtNombreED);
        etFecha=root.findViewById(R.id.tIEtFechaED);
        etEstadoG=root.findViewById(R.id.tIEtEdoED);
        etPeso=root.findViewById(R.id.tIEtPesoED);
    }

    private void SpinenrComponent(View root){

        ArrayAdapter<CharSequence> especieAdapter, habtAdapter, sexoAdapter,statusAdapter;

        especieAdapter=ArrayAdapter.createFromResource(getContext(),R.array.op_especies,android.R.layout.simple_spinner_item);
        habtAdapter=ArrayAdapter.createFromResource(getContext(),R.array.op_habitats,android.R.layout.simple_spinner_item);
        sexoAdapter=ArrayAdapter.createFromResource(getContext(),R.array.sx,android.R.layout.simple_spinner_item);
        statusAdapter=ArrayAdapter.createFromResource(getContext(),R.array.stats,android.R.layout.simple_spinner_item);

        spnEspecie=root.findViewById(R.id.spnEspecieED);
        spnEspecie.setAdapter(especieAdapter);

        spnHabt=root.findViewById(R.id.spnHabitatED);
        spnHabt.setAdapter(habtAdapter);

        spnsx=root.findViewById(R.id.spnSexoED);
        spnsx.setAdapter(sexoAdapter);

        spnstatus=root.findViewById(R.id.spnStatusED);
        spnstatus.setAdapter(statusAdapter);

        spnEspecie.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spnHabt.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        spnsx.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        spnstatus.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);

    }
    private void BotonesComponentes(View root){
        btnCalen = root.findViewById(R.id.ibtnFechaED);
        btnGuarda = root.findViewById(R.id.btnEditar);
        btnLimpia = root.findViewById(R.id.btnLimpiarED);
        btnBusca = root.findViewById(R.id.btnBuscarED);
        ivFoto =root.findViewById(R.id.iVFotoED);

        btnBusca.setOnClickListener((View.OnClickListener)this);
        btnCalen.setOnClickListener((View.OnClickListener) this);
        btnGuarda.setOnClickListener((View.OnClickListener) this);
        btnLimpia.setOnClickListener((View.OnClickListener) this);
        ivFoto.setOnClickListener((View.OnClickListener) this);

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG"+timeStamp+"...";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);

        currentPhotoPath=image.getAbsolutePath();
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode== REQUEST_TAKE_PHOTO && resultCode== Activity.RESULT_OK){
            ivFoto.setImageURI(photoURI);
            img = currentPhotoPath;
            Toast.makeText(getContext(),"Foto guardada en "+img, Toast.LENGTH_LONG).show();

        }
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

    private void mLimpiar(){
        etId.setText("");
        etNombre.setText("");
        etFecha.setText("");
        etEstadoG.setText("");
        etPeso.setText("");
        ivFoto.setImageResource(R.drawable.ic_menu_camera);

        ArrayAdapter<CharSequence> especieAdapter, habtAdapter, sexoAdapter,statusAdapter;

        especieAdapter=ArrayAdapter.createFromResource(getContext(),R.array.op_especies,android.R.layout.simple_spinner_item);
        habtAdapter=ArrayAdapter.createFromResource(getContext(),R.array.op_habitats,android.R.layout.simple_spinner_item);
        sexoAdapter=ArrayAdapter.createFromResource(getContext(),R.array.sx,android.R.layout.simple_spinner_item);
        statusAdapter=ArrayAdapter.createFromResource(getContext(),R.array.stats,android.R.layout.simple_spinner_item);

        esp="";
        hbt="";
        sex="";
        sts="";


        spnEspecie.setAdapter(especieAdapter);
        spnHabt.setAdapter(habtAdapter);
        spnsx.setAdapter(sexoAdapter);
        spnstatus.setAdapter(statusAdapter);
        ivFoto.setImageResource(R.drawable.ic_menu_camera);
        bnd=0;

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        etFecha.setText(dayOfMonth+"/"+(month)+"/"+year);
    }
    public static int obtenerPosicion(Spinner spinner, String item){
        int posicion=0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)){
                posicion=i;
            }
        }
        return posicion;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBuscarED:
                if(etId.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Ingrese ID del Animal",Toast.LENGTH_LONG).show();
                    bnd=0;
                }else{
                    sqLite.abrir();
                    idp=Integer.parseInt(etId.getText().toString());
                    if(sqLite.getValorall(idp).getCount()==1){
                        Cursor cursor=sqLite.getValorall(idp);
                        if(cursor.moveToFirst()){
                            do {
                                idpfinal =idp;
                                i = etId.getText().toString();
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

                            etNombre.setText(n);
                            etFecha.setText(f);
                            etEstadoG.setText(edoG);
                            etPeso.setText(ps);
                            spnEspecie.setSelection(obtenerPosicion(spnEspecie,esp));
                            spnHabt.setSelection(obtenerPosicion(spnHabt,hbt));
                            spnsx.setSelection(obtenerPosicion(spnsx,s));
                            spnstatus.setSelection(obtenerPosicion(spnstatus,sts));
                            cargarImagen(img,ivFoto);
                            bnd=1;

                        }

                    }else{
                        Toast.makeText(getContext(),"El ID del Animal: "+etId.getText().toString()+"No existe",Toast.LENGTH_LONG).show();
                        bnd=0;
                    }

                    sqLite.cerrar();
                }
                break;
            case R.id.btnLimpiarED:
                mLimpiar();
                break;
            case R.id.ibtnFechaED:
                if (bnd==1){
                    c = Calendar.getInstance();
                    anio = c.get(Calendar.YEAR);
                    mes=c.get(Calendar.MONTH);
                    dia=c.get(Calendar.DAY_OF_MONTH);
                    dpd=new DatePickerDialog(getContext(),this,anio,mes,dia);
                    dpd.show();
                }else {
                    Toast.makeText(getContext(),"Ingrese ID del Animal a editar",Toast.LENGTH_LONG).show();
                    bnd=0;
                }
                break;
            case R.id.iVFotoED:
                if (bnd==1){
                    Intent tomarFoto =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (tomarFoto.resolveActivity(getActivity().getPackageManager())!=null){
                        File photoFile=null;
                        try {
                            photoFile=createImageFile();
                        }catch (IOException ie){
                            Toast.makeText(getContext(),"Error en fotografia",Toast.LENGTH_LONG).show();
                        }
                        if (photoFile!=null){
                            photoURI= FileProvider.getUriForFile(getContext(),"com.proyecto.monadoptions",photoFile);
                            tomarFoto.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                            startActivityForResult(tomarFoto,REQUEST_TAKE_PHOTO);
                        }

                    }
                }else {
                    Toast.makeText(getContext(),"Ingrese ID del paciente a editar",Toast.LENGTH_LONG).show();
                    bnd=0;
                }
                break;
            case R.id.btnEditar:
                if (bnd==1) {
                    if (
                            etId.getText().toString().equals("") || etNombre.getText().toString().equals("") || etFecha.getText().toString().equals("") ||
                                    etEstadoG.getText().toString().equals("") || etPeso.getText().toString().equals("")
                    ) {
                        Toast.makeText(getContext(), "Requiere llenar los campos", Toast.LENGTH_LONG).show();
                    } else {

                        esp = esp.toUpperCase();
                        hbt = hbt.toUpperCase();
                        n = etNombre.getText().toString().toUpperCase();
                        sex = sex.toUpperCase();
                        f = etFecha.getText().toString();
                        edoG = etEstadoG.getText().toString().toUpperCase();
                        ps = etPeso.getText().toString().toUpperCase();
                        sts = sts.toUpperCase();
                        sqLite.abrir();
                        Toast.makeText(getContext(), sqLite.updateRegistroAnimal(idpfinal, esp, hbt, n, sex, f, edoG, ps, sts, img), Toast.LENGTH_LONG).show();
                        mLimpiar();
                        sqLite.cerrar();
                        bnd = 0;

                    }
                        }else {
                            Toast.makeText(getContext(),"Ingrese ID del paciente a editar",Toast.LENGTH_LONG).show();
                            bnd=0;
                        }
                    break;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spnEspecieED:
                if (position!=0){
                    esp=parent.getItemAtPosition(position).toString();
                }
                else {
                    esp="";
                }
                break;
            case  R.id.spnHabitatED:
                if (position!=0){
                    hbt=parent.getItemAtPosition(position).toString();
                }
                else {
                    hbt="";
                }
                break;
            case  R.id.spnSexoED:
                if (position!=0){
                    sex = parent.getItemAtPosition(position).toString();
                }else {
                    sex="";

                }
                break;
            case  R.id.spnStatusED:
                if (position!=0){
                    sts = parent.getItemAtPosition(position).toString();
                }else {
                    sts="";

                }
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}