package generar_bd;

import com.example.nfcook.R;
import com.example.nfcook.R.layout;

import baseDatos.Handler;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.Toast;

public class BaseDatosLogin extends Activity {	   
	public Handler sql;
	public SQLiteDatabase db;
	
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.base_datos_generada);
        
        // Importamos la base de datos donde vamos a hacer la carga de los platos de los restaurantes
        try{
        	sql=new Handler(getApplicationContext(),"Login.db"); 
        	db=sql.open();
        }catch(SQLiteException e){
        	Toast.makeText(getApplicationContext(),"NO EXISTE",Toast.LENGTH_SHORT).show();
     		
        } 

        ContentValues usuario = new ContentValues();
        usuario.put("Nombre", "Abel");
        usuario.put("Contrase�a", "1234");
     	db.insert("Camareros", null, usuario);
     	           
    	
     	usuario= new ContentValues();
     	usuario.put("Nombre", "Carlos");
     	usuario.put("Contrase�a", "1234");
     	db.insert("Camareros", null, usuario);
     	
     	usuario = new ContentValues();
     	usuario.put("Nombre", "Javi");
     	usuario.put("Contrase�a", "1234");
     	db.insert("Camareros", null, usuario);
     	
     	usuario = new ContentValues();
     	usuario.put("Nombre", "Dani");
     	usuario.put("Contrase�a", "1234");
     	db.insert("Camareros", null, usuario);
     	
     	usuario = new ContentValues();
     	usuario.put("Nombre", "Rober");
     	usuario.put("Contrase�a", "1234");
     	db.insert("Camareros", null, usuario);
     	
     	usuario = new ContentValues();
     	usuario.put("Nombre", "AlexM");
     	usuario.put("Contrase�a", "1234");
     	db.insert("Camareros", null, usuario);
     	
     	usuario = new ContentValues();
     	usuario.put("Nombre", "AlexV");
     	usuario.put("Contrase�a", "1234");
     	db.insert("Camareros", null, usuario);
     	
     	usuario = new ContentValues();
     	usuario.put("Nombre", "JuanDiego");
     	usuario.put("Contrase�a", "1234");
     	db.insert("Camareros", null, usuario);
     	
     	usuario = new ContentValues();
     	usuario.put("Nombre", "Alvaro");
     	usuario.put("Contrase�a", "1234");
     	db.insert("Camareros", null, usuario);
     	
     	usuario = new ContentValues();
     	usuario.put("Nombre", "admin");
     	usuario.put("Contrase�a", "admin");
     	db.insert("Camareros", null, usuario);

        // Cerramos la base de datos
     	sql.close();
    }
 }