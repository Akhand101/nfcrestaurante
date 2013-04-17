/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basesDeDatos;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import sockets.ClienteFichero;

public class Operaciones extends Conexion{
	private String nombreDB;
	private final int puerto = 5000;
	private final String servidor = "nfcook.no-ip.org";
	/**
     * Constructor for objects of class Operaciones
     * @param string 
     */
    public Operaciones(String nombreDB)
    {
        super(nombreDB);
        this.nombreDB = nombreDB;
    }
    
    public boolean insertar(String sql, boolean socket){
        boolean valor = true;
        if (socket) // enviamos la consulta de insercion por socket 
        	enviarConsultaSocket(sql);
        conectar();
        try {
            consulta.executeUpdate(sql);
        } catch (SQLException e) {
                valor = false;
                JOptionPane.showMessageDialog(null, e.getMessage());
            }      
        finally{  
            try{    
                 consulta.close();  
                 conexion.close();  
             }catch (Exception e){                 
                 e.printStackTrace();  
             }  
        }
        return valor;
    }
    public ResultSet consultar(String sql){
    	conectar();
        ResultSet resultado = null;
        try {
            resultado = consulta.executeQuery(sql);

        } catch (SQLException e) {
                System.out.println("Mensaje:"+e.getMessage());
                System.out.println("Estado:"+e.getSQLState());
                System.out.println("Codigo del error:"+e.getErrorCode());
                JOptionPane.showMessageDialog(null, ""+e.getMessage());
            }
        return resultado;
    }
    
    public void cerrarBaseDeDatos(){
    	try{
    		consulta.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    private void enviarConsultaSocket(String sql){
    	ClienteFichero cliente = new ClienteFichero();
        cliente.enviaConsulta(nombreDB, servidor, puerto, sql);
    }
    
/*
    public void guardarUsuario(Persona persona){
        insertar("insert into Persona values("+persona.getId()
                    +",'"+persona.getPrimer_nombre()
                    +"','"+persona.getSegundo_nombre()
                    +"','"+persona.getPrimer_apellido()
                    +"','"+persona.getSegundo_apellido()+"')");
    } 
  
    public void totalPersonas(DefaultTableModel tableModel){
        ResultSet resultado = null;
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        String sql = "select * from Persona";
        try {
            resultado = consultar(sql);
            if(resultado != null){
                int numeroColumna = resultado.getMetaData().getColumnCount();
                for(int j = 1;j <= numeroColumna;j++){
                    tableModel.addColumn(resultado.getMetaData().getColumnName(j));
                }
                while(resultado.next()){
                    Object []objetos = new Object[numeroColumna];
                    for(int i = 1;i <= numeroColumna;i++){
                        objetos[i-1] = resultado.getObject(i);
                    }
                    tableModel.addRow(objetos);
                }
            }
        }catch(SQLException e){
        }

        finally
     {
         try
         {
             consulta.close();
             conexion.close();
             if(resultado != null){
                resultado.close();
             }
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
     }
    }
  */  
}