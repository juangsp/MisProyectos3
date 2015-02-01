package es.tessier.carlos.misproyectos;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class PracticaAssets extends Activity {
    TextView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practica_assets);
         imagen=(TextView)findViewById(R.id.textView2);
        leerArchivodeAssets();
    }

    public void leerArchivodeAssets() {
        AssetManager assetManager=getAssets();
        InputStream inputStream=null;
        try{
            inputStream=assetManager.open("canciones/la lola.txt");
            ByteArrayOutputStream byteStream= new ByteArrayOutputStream();
            byte[] bytes=new byte[4096];
            int len=0;

            while((len=inputStream.read(bytes))>0)
                byteStream.write(bytes, 0, len);

            String text=new String(byteStream.toByteArray(),"UTF8");

            imagen.setText(text);
        }catch(IOException e){
            imagen.setText("No se puede cargar el archivo");
        }finally{
            if(inputStream!=null){
                try{
                    inputStream.close();
                }catch(IOException E){
                 imagen.setText("No se puede cargar el archivo");
                }
            }
        }
    }


}
