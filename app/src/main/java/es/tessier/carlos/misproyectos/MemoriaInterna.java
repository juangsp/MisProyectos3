package es.tessier.carlos.misproyectos;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MemoriaInterna extends Activity {
    EditText texto;
    public static final int READ_BLOCK_SIZE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memoria_interna);
        texto=(EditText)findViewById(R.id.editText);
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
        try {
            FileOutputStream fOut = openFileOutput("textfile.txt",0);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(text);
            osw.flush();
            osw.close();

        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(),"Archivo no encontrado",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Error de E/S",
                    Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getApplicationContext(),"Texto guardado correctamente",
                Toast.LENGTH_SHORT).show();
        texto.setText(" ");
    }

    public void cargar(View v){
        FileInputStream fIn = null;
        try {
            fIn = openFileInput("textfile.txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            char [] buffer=new char[READ_BLOCK_SIZE ];
            String s ="";
            int charRead;
            while((charRead=isr.read(buffer))>0){
                String readString =String.copyValueOf(buffer, 0,charRead);
                s += readString;
                buffer = new char[READ_BLOCK_SIZE];

            }

              texto.setText(s);
              isr.close();

        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(),"Archivo no encontrado",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Error de E/S",
                    Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getApplicationContext(),"Texto guardado correctamente",
                Toast.LENGTH_SHORT).show();

    }
}
