package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prueba.Objetos.ServicioNotificaciones;
import com.example.prueba.Objetos.Solicitud;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.example.prueba.Objetos.Usuario3;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import net.glxn.qrgen.android.QRCode;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MiPerfil extends AppCompatActivity {

    private String username_mod;
    private String telefono_mod;

    private TextView tituloActivity;
    private ImageView closeActivity;

    private ImageView btn_qr;
    private ImageView btn_ubi;

    private Button btn_modificar;
    private String funcion_mod="mod";
    private Button btn_contrasena;


    private TextView username_superior;
    private CircleImageView foto_perfil;


    private EditText username;
    private EditText telefono;
    private EditText correo;
    private EditText password;
    private Dialog qr_dialog;

    private FirebaseDatabase base;
    private DatabaseReference referencia;
    private static final int IMAGE_REQUEST=1;
    private StorageReference storageReference;
    private Uri imageUri;
    private StorageTask uploadTask;

    private boolean salida=true;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Usuario3 usuario3;
    private Location location;
    private String ref;
    private ValueEventListener listener;

    private void setSalida (Boolean salida){
        this.salida=salida;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        qr_dialog = new Dialog(this);
        qr_dialog.setContentView(R.layout.popup_qr);
        qr_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_qr=findViewById(R.id.miperfil_btn_qr);
        btn_ubi=findViewById(R.id.miperfil_btn_ubi);

        btn_modificar=findViewById(R.id.miperfil_btn_modificar);
        btn_contrasena=findViewById(R.id.miperfil_btn_password);

        username_superior=findViewById(R.id.miperfil_usuario);
        foto_perfil=findViewById(R.id.miperfil_profile_image);

        username=findViewById(R.id.miperfil_edit_nombreusuario);
        telefono=findViewById(R.id.miperfil_edit_telefono);
        correo=findViewById(R.id.miperfil_edit_correo);

        tituloActivity=findViewById(R.id.titulo_activity);
        tituloActivity.setText("Mi Perfil");

        closeActivity=findViewById(R.id.close_activity);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference("fotosPerfil");

        base=FirebaseDatabase.getInstance();
        referencia=base.getReference("Usuarios");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();



        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });


        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ImageView image= (ImageView) qr_dialog.findViewById(R.id.qr_image);
                Bitmap bitmap = QRCode.from(usuario3.getId()).withSize(400, 400).bitmap();
                image.setImageBitmap(bitmap);
                qr_dialog.show();
            }
        });

        btn_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),CambioContrasena.class);
                intent.putExtra("email",usuario3.getEmail());
                startActivity(intent);
            }
        });
        btn_ubi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_ubi.getTag().equals("1")){
                    usuario3.setLocalizable(false);
                    referencia.child(ref).setValue(usuario3);
                }
                else{
                    localizar();
                    if(location!=null) {
                        usuario3.setLocalizable(true);
                        usuario3.setLatitud(location.getLatitude());
                        usuario3.setLongitud(location.getLongitude());
                        referencia.child(ref).setValue(usuario3);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Localización no disponible por el momento",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btn_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(funcion_mod.equals("mod")){
                    telefono_mod=telefono.getText().toString();
                    btn_contrasena.setVisibility(View.INVISIBLE);
                    btn_modificar.setBackgroundColor(Color.rgb(10,210,149));
                    btn_modificar.setText("Confirmar cambios");

                    telefono.setEnabled(true);
                    funcion_mod="conf";
                }
                else{

                    //Si no se han realizado cambios
                    if( telefono.getText().toString().equals(telefono_mod)){
                        salida=true;
                        Toast.makeText(MiPerfil.this, "No se ha realizado ningun cambio", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (telefono.getText().equals("")){
                            Toast.makeText(MiPerfil.this, "Campo incompleto", Toast.LENGTH_SHORT).show();
                            salida=false;
                        }
                        else{
                            if( validCellPhone(telefono.getText().toString()) && telefono.getText().toString().length()==9 ){
                                referencia.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            Usuario usuario = data.getValue(Usuario.class);
                                            listaUsuarios.add(usuario);
                                        }
                                        for (Usuario usu : listaUsuarios) {
                                            if (usu.getTelefono().equals(telefono.getText().toString())){
                                               salida=false;
                                                Toast.makeText(MiPerfil.this, "Número de teléfono ya registrado", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        if(salida==true){
                                            usuario3.setTelefono(telefono.getText().toString());
                                            referencia.child(ref).setValue(usuario3);
                                            btn_contrasena.setVisibility(View.VISIBLE);

                                            btn_modificar.setBackgroundColor(Color.rgb(33,150,244));

                                            btn_modificar.setText("MODIFICAR MIS DATOS");

                                            telefono.setEnabled(false);
                                            funcion_mod="mod";
                                        }
                                        else{
                                            telefono.setText(telefono_mod);
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                });

                            }
                            else{
                                Toast.makeText(MiPerfil.this, "Campo no valido", Toast.LENGTH_SHORT).show();
                                salida=false;
                            }
                        }
                    }
                    if(salida==false){
                        telefono.setText(telefono_mod);
                    }



                }
            }

            public boolean validCellPhone(String number) { return android.util.Patterns.PHONE.matcher(number).matches(); }
        });
        leerDatos();
    }
    public void leerDatos(){
        referencia.addValueEventListener(listener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Usuario3 usuarioo = data.getValue(Usuario3.class);

                    if ( user.getUid().equals(usuarioo.getId())) {
                        usuario3=usuarioo;
                        ref=data.getKey();
                        if(usuario3.isLocalizable()!=true){
                            btn_ubi.setTag("2");
                            btn_ubi.setImageResource(R.drawable.ic_location_off);
                        }
                        else{
                            btn_ubi.setTag("1");
                            btn_ubi.setImageResource(R.drawable.ic_location_on);
                        }
                        username_superior.setText(usuario3.getNombre_usuario());
                        username.setText(usuario3.getNombre_usuario());
                        correo.setText(usuario3.getEmail());
                        telefono.setText(usuario3.getTelefono());
                        if(usuario3.getUrl_imagen().equals("default")){
                            foto_perfil.setImageResource(R.drawable.profile);
                        }
                        else{
                            Glide.with(MiPerfil.this).load(usuario3.getUrl_imagen()).into(foto_perfil);

                        }

                    }
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void localizar() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1000);
        } else {
            LocationManager  ubicacion_usuario = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = ubicacion_usuario.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    else {
                        LocationManager ubicacion_usuario = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        location = ubicacion_usuario.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    }
                }
                else{

                }
            }

        }
    }

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Estableciendo foto de perfil");
        pd.show();
        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot>task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        usuario3.setUrl_imagen(mUri);
                        referencia.child(ref).setValue(usuario3);
                        pd.dismiss();
                    }else{
                        Toast.makeText(MiPerfil.this, "error", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MiPerfil.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else{
            Toast.makeText(this, "No ha seleccionado imagen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(this, "Upload en progreso", Toast.LENGTH_SHORT).show();

            } else{
                uploadImage();
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        referencia.removeEventListener(listener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, ServicioNotificaciones.class));
    }


}
