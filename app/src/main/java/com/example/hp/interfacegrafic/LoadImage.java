package com.example.hp.interfacegrafic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class LoadImage extends AppCompatActivity
{
    private Context btncasa;

    private final String CARPETA_RAIZ ="misImagenes/";
    private final String RUTA_IMAGEN =CARPETA_RAIZ+"misFotos";
    ImageView image;
    String path;
    final int CODE_SELECCIONA = 10;
    final int CODE_FOTO = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        image = (ImageView)this.findViewById(R.id.imageLoad);

        btncasa = this;
        loadComponents();

    }

    private void loadComponents() {
        Button btnUser = (Button)this.findViewById(R.id.btn_regCasa); //En el id se coloca el boton que se va a usar
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent us = new Intent(btncasa, FormCasas.class); //Se señala el class al cual se va a enlazar
                btncasa.startActivity(us);
            }
        });

    }

    public void onClick(View view)
    {
        cargarImagen();
    }

    private void cargarImagen()
    {
        final  CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(LoadImage.this);
        alertOpciones.setTitle("Seleccione una opcion");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(opciones[i].equals("Tomar Foto"))
                {
                    tomarFotografía();
                }
                else if(opciones[i].equals("Cargar Imagen"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent, "Selecciona la app de su gusto"),CODE_SELECCIONA);
                }
                else if(opciones[i].equals("Cancelar"))
                {
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();


    }

    private  void tomarFotografía()
    {
        File fileImagen = new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        String nombreImagen="";

        if(isCreada==false)
        {
            isCreada=fileImagen.mkdir();
        }
        if(isCreada)
        {
            nombreImagen = (System.currentTimeMillis()/1000)+".jpg";
        }

        path = Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen = new File(path);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        startActivityForResult(intent,CODE_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            switch (requestCode)
            {
                case CODE_SELECCIONA:
                {
                    Uri mipath = data.getData();
                    image.setImageURI(mipath);
                    break;
                }
                case CODE_FOTO:
                {
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("Ruta de almacenamiento","Path: "+path);
                        }
                    });

                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    image.setImageBitmap(bitmap);

                    break;
                }
            }

        }
        else
        {
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        }
    }
}
