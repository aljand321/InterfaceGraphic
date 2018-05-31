package com.example.hp.interfacegrafic;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class FormCasas extends AppCompatActivity {

    EditText tipo, estado, precio, ciudad, region, ubicacion, descripcion,
            cantidadCuartos, cantidadBaños, garage, superficie, correo;

    Button guardarcasa;
    HttpClient cliente;
    HttpPost post;
    List<NameValuePair> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_casas);

       tipo = (EditText) findViewById(R.id.txt_tipo);
        estado = (EditText)findViewById(R.id.txt_estado);
        precio= (EditText)findViewById(R.id.txt_precio);
        ciudad = (EditText)findViewById(R.id.txt_ciudad);
        region = (EditText)findViewById(R.id.txt_Region);
        ubicacion = (EditText)findViewById(R.id.txt_ubicacion);
        descripcion = (EditText)findViewById(R.id.txt_Descripcion);
        cantidadCuartos = (EditText)findViewById(R.id.txt_cuartos);
        cantidadBaños = (EditText)findViewById(R.id.txt_baño);
        garage = (EditText)findViewById(R.id.txt_garje);
        superficie = (EditText)findViewById(R.id.txt_superficie);
        correo = (EditText) findViewById(R.id.txt_correo);


        guardarcasa = (Button)findViewById(R.id.btn_enviarcasa);
        guardarcasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipo.getText().toString().equals("")){
                    Toast.makeText(FormCasas.this, "Nombre no puede ser vacio", Toast.LENGTH_LONG).show();
                }else {
                    new FormCasas.EnviarDatos(FormCasas.this).execute();
                }
            }
        });
    }

    class EnviarDatos extends AsyncTask<String, String, String > {


        private Activity contexto;
        EnviarDatos(Activity context){
            this.contexto = context;
        }
        @Override
        protected String doInBackground(String... strings) {

            if(datos ()){
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(contexto, "Datos enviados Existosamente", Toast.LENGTH_SHORT).show();
                        tipo.setText("");
                        estado.setText("");
                        precio.setText("");
                        ciudad.setText("");
                        region.setText("");
                        ubicacion.setText("");
                        descripcion.setText("");
                        cantidadCuartos.setText("");
                        cantidadBaños.setText("");
                        garage.setText("");
                        superficie.setText("");
                        correo.setText("");

                    }
                });


            }
            else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(contexto, "Datos no enviados", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            return null;
        }
    }

    private boolean datos (){
        cliente = new DefaultHttpClient();
        post = new HttpPost("http://10.10.1.150:7777/api/v1.0/inmuebles");
        lista = new ArrayList<NameValuePair>(11);
        lista.add(new BasicNameValuePair("tipo", tipo.getText().toString().trim()));
        lista.add(new BasicNameValuePair("estado",estado.getText().toString().trim()));
        lista.add(new BasicNameValuePair("precio",precio.getText().toString().trim()));
        lista.add(new BasicNameValuePair("ciudad",ciudad.getText().toString().trim()));
        lista.add(new BasicNameValuePair("region",region.getText().toString().trim()));
        lista.add(new BasicNameValuePair("ubicacion",ubicacion.getText().toString().trim()));
        lista.add(new BasicNameValuePair("descripcion",descripcion.getText().toString().trim()));
        lista.add(new BasicNameValuePair("cantidadCuartos",cantidadCuartos.getText().toString().trim()));
        lista.add(new BasicNameValuePair("cantidadBaños",cantidadBaños.getText().toString().trim()));
        lista.add(new BasicNameValuePair("garage",garage.getText().toString().trim()));
        lista.add(new BasicNameValuePair("superficie",superficie.getText().toString().trim()));
        lista.add(new BasicNameValuePair("correo",correo.getText().toString().trim()));
        try{
            post.setEntity(new UrlEncodedFormEntity(lista));
            cliente.execute(post);
            return true;

        }catch (UnsupportedEncodingException e){

            e.printStackTrace();

        }catch (ClientProtocolException e){

            e.printStackTrace();

        }catch (IOException e){

            e.printStackTrace();
        }

        return false;
    }
}
