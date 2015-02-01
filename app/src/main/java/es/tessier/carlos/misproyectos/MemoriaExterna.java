package es.tessier.carlos.misproyectos;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MemoriaExterna extends Activity {
    EditText texto;
    public static final int READ_BLOCK_SIZE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memoria_externa);
        texto=(EditText)findViewById(R.id.editText2);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_memoria_externa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void guardar(View v){
        String text=texto.getText().toString();
        File sdCard = Environment.getExternalStorageDirectory();
        String f="fichero_prueba.txt";

            if(isExternalStorageWritable()) {
                File directory=new File(sdCard.getAbsolutePath(),"ficheros");
                Log.d("MemoriaExterna", directory.getAbsolutePath());
                if(!directory.mkdirs()){
                    Toast.makeText(getApplicationContext(), "directorio no creado",
                            Toast.LENGTH_SHORT).show();
                }
                if(directory.exists()) {
                    try {
                        File file = new File(directory, f);
                        FileOutputStream fOut = new FileOutputStream(file);
                        OutputStreamWriter osw = new OutputStreamWriter(fOut);
                        osw.write(text);
                        osw.flush();
                        osw.close();

                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "Archivo no encontrado",
                                Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Error E/S",
                                Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(), "Texto guardado correctamente",
                            Toast.LENGTH_SHORT).show();
                    texto.setText(" ");
                }
            }else{
                Toast.makeText(getApplicationContext(), "La tarjeta sd no se puede escribir",
                        Toast.LENGTH_SHORT).show();

            }

    }



    public void cargar(View v){
        File sdCard = Environment.getExternalStorageDirectory();
        String f="fichero_prueba.txt";
        File directory = new File (sdCard.getAbsolutePath() +"/ficheros");
        FileInputStream fIn = null;
        if(isExternalStorageReadable()) {
            try {
                File file = new File(directory, f);
                fIn = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fIn);
                char[] buffer = new char[READ_BLOCK_SIZE];
                String s = "";
                int charRead;
                while ((charRead = isr.read(buffer)) > 0) {
                    String readString = String.copyValueOf(buffer, 0, charRead);
                    s += readString;
                    buffer = new char[READ_BLOCK_SIZE];

                }

                texto.setText(s);
                isr.close();

            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Archivo no encontrado",
                        Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error de E/S",
                        Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(getApplicationContext(), "Texto guardado correctamente",
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "La tarjeta sd no se puede leer",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
