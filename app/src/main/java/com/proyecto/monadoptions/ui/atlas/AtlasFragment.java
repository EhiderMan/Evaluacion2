package com.proyecto.monadoptions.ui.atlas;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

public class AtlasFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener {

    private Button btnLimpia, btnGuarda;
    private ImageButton btnCalen;
    private EditText etId, etNombre, etFecha, etEstadoG, etPeso;
    private Spinner spnEspecie, spnHabt, spnsx;
    private ImageView ivFoto;

    DatePickerDialog dpd;
    Calendar c;
    private static int anio,mes,dia;

    private Uri photoURI;
    public static String currentPhotoPath, img="",esp,hbt,sex;
    public static final int REQUEST_TAKE_PHOTO=1;


    public SQLite sqlite;

    private AtlasViewModel mViewModel;

    public static AtlasFragment newInstance() {
        return new AtlasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_atlas, container, false);
        sqlite = new SQLite(getContext());

        Componentes(root);

        return (root);
    }

    private void Componentes(View root){

        EditTextComponent(root);
        Botones(root);
        SpinenrComponent(root);
    }

    private void EditTextComponent(View root){
       // etId=root.findViewById(R.id.tIEtIDna);
        etNombre=root.findViewById(R.id.tIEtNombrena);
        etFecha=root.findViewById(R.id.tIEtFechana);
        etEstadoG=root.findViewById(R.id.tIEtEdoNA);
        etPeso=root.findViewById(R.id.tIEtPesona);


    }

    private void SpinenrComponent(View root){

        ArrayAdapter<CharSequence> especieAdapter, habtAdapter, sexoAdapter;

        especieAdapter=ArrayAdapter.createFromResource(getContext(),R.array.op_especies,android.R.layout.simple_spinner_item);
        habtAdapter=ArrayAdapter.createFromResource(getContext(),R.array.op_habitats,android.R.layout.simple_spinner_item);
        sexoAdapter=ArrayAdapter.createFromResource(getContext(),R.array.sx,android.R.layout.simple_spinner_item);

        spnEspecie=root.findViewById(R.id.spnEspecieNA);
        spnEspecie.setAdapter(especieAdapter);

        spnHabt=root.findViewById(R.id.spnHabitatNA);
        spnHabt.setAdapter(habtAdapter);

        spnsx=root.findViewById(R.id.spnSexoNa);
        spnsx.setAdapter(sexoAdapter);

        spnEspecie.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spnHabt.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        spnsx.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);

    }
    private void Botones(View root){
        btnCalen = root.findViewById(R.id.ibtnFechaNA);
        btnGuarda = root.findViewById(R.id.btnGuardar);
        btnLimpia = root.findViewById(R.id.btnLimpiar);
        ivFoto =root.findViewById(R.id.iVFotoNA);

        btnCalen.setOnClickListener((View.OnClickListener) this);
        btnGuarda.setOnClickListener((View.OnClickListener) this);
        btnLimpia.setOnClickListener((View.OnClickListener) this);
        ivFoto.setOnClickListener((View.OnClickListener) this);

    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        etFecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtnFechaNA:
                c = Calendar.getInstance();
                    anio = c.get(Calendar.YEAR);
                mes=c.get(Calendar.MONTH);
                dia=c.get(Calendar.DAY_OF_MONTH);

                dpd=new DatePickerDialog(getContext(),this,anio,mes,dia);
                dpd.show();
                break;

            case R.id.btnLimpiar:
                mLimpiar();
                break;
            case R.id.iVFotoNA:
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
                break;
            case R.id.btnGuardar:
                //etId.getText().toString().equals("") ||
                if (
                        etNombre.getText().toString().equals("") || etFecha.getText().toString().equals("") ||
                                etEstadoG.getText().toString().equals("")|| etPeso.getText().toString().equals("")
                ){
                    Toast.makeText(getContext(),"Requiere llenar los campos",Toast.LENGTH_LONG).show();
                }
                else {
                    //Toast.makeText(getContext(),"Datos ok",Toast.LENGTH_LONG).show();
                    /*if (sqlite.addRegistroPaciente(
                            Integer.parseInt(etId.getText().toString()),
                            a,d,etNombre.getText().toString().toUpperCase(),
                            sex, etFecha.getText().toString(),etEdad.getText().toString().toUpperCase(),
                            etEstatura.getText().toString().toUpperCase(),etPeso.getText().toString().toUpperCase(),
                            img
                    )){
                        Toast.makeText(getContext(),"Datos almacenados",Toast.LENGTH_LONG).show();
                        mLimpiar();
                    }else {
                        Toast.makeText(getContext(),"Error de almacenamiento",Toast.LENGTH_LONG).show();
                    }
                    sqlite.cerrar();
                    */
                   // int id=Integer.parseInt(etId.getText().toString());
                    String nom= etNombre.getText().toString().toUpperCase();
                    String fech = etFecha.getText().toString();
                    String edoG = etEstadoG.getText().toString().toUpperCase();
                    String p=etPeso.getText().toString().toUpperCase();
                    sqlite.abrir();
                    if (sqlite.addRegistroAnimalzoo(esp,hbt,nom,sex,fech,edoG,p,img)){
                        Toast.makeText(getContext(),"Datos almacenados",Toast.LENGTH_LONG).show();
                        mLimpiar();

                    }else {
                        Toast.makeText(getContext(),"Error de almacenamiento",Toast.LENGTH_LONG).show();
                    }
                    sqlite.cerrar();
                }
                break;
        }

    }

    private File createImageFile() throws IOException{
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

    private void mLimpiar(){
        //etId.setText("");
        etNombre.setText("");
        etFecha.setText("");
        etEstadoG.setText("");
        etPeso.setText("");
        ivFoto.setImageResource(R.drawable.ic_menu_camera);

        ArrayAdapter<CharSequence> especieAdapter, habtAdapter, sexoAdapter;

        especieAdapter=ArrayAdapter.createFromResource(getContext(),R.array.op_especies,android.R.layout.simple_spinner_item);
        habtAdapter=ArrayAdapter.createFromResource(getContext(),R.array.op_habitats,android.R.layout.simple_spinner_item);
        sexoAdapter=ArrayAdapter.createFromResource(getContext(),R.array.sx,android.R.layout.simple_spinner_item);

        esp="";
        hbt="";
        sex="";

        spnEspecie.setAdapter(especieAdapter);
        spnHabt.setAdapter(habtAdapter);
        spnsx.setAdapter(sexoAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.spnEspecieNA:
                if (position!=0){
                    esp=parent.getItemAtPosition(position).toString();
                }
                else {
                    esp="";
                }
                break;
            case  R.id.spnHabitatNA:
                if (position!=0){
                    hbt=parent.getItemAtPosition(position).toString();
                }
                else {
                    hbt="";
                }
                break;
            case  R.id.spnSexoNa:
                if (position!=0){
                    sex = parent.getItemAtPosition(position).toString();
                }else {
                    sex="";

                }

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}