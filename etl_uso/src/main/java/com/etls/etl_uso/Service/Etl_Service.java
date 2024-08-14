package com.etls.etl_uso.Service;

import java.util.List;
import java.util.Map;

import com.etls.etl_uso.Static_Value.table_static;
import com.etls.etl_uso.Static_Value.value_static;

public interface Etl_Service {
    
    /**
    *establece la tabla destino y lo guarda en la variable tabla_ftn de la clase table_static
    *
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param Ninguno
    *
    *@return Ninguno
    *
    *@ejemplo
    *this.obtener_tablasOrigen();
    *
    *
    */
    void obtener_tablasOrigen();

    /**
    *establece la tabla origen y lo guarda en la variable tabla_ogn de la clase table_static
    *
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param Ninguno
    *
    *@return Ninguno
    *
    *@ejemplo
    *this.obtener_tablasDestino();
    *
    *
    */
    void obtener_tablasDestino();

    /**
    *establece el usuario que va a ser el origen y lo guarda en la variable user_o de la clase value_static
    *
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param Ninguno
    *
    *@return Ninguno
    *
    *@ejemplo
    *this.obtenerUsuarioDestino();
    *
    *
    */
    void obtenerUsuarioOrigen();

    /**
    *establece el usuario que va a ser el destino y lo guarda en la variable user_d de la clase value_static
    *
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param Ninguno
    *
    *@return Ninguno
    *
    *@ejemplo
    *this.obtenerUsuarioDestino();
    *
    *
    */ 
    void obtenerUsuarioDestino();

    /**
    *Guarda la decision tomada, entre ser una sentencia sql o ser un tabla de la base destino escogida
    *
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param Ninguno
    *
    *@return Ninguno
    *
    *@ejemplo
    *this.decisionSentTabla();
    *
    *
    */ 
    void decisionSentTabla();

    /**
    *Esta funcion se encarga de emparejar los campos de la tabla origen y destino, colocando
    *dos mapas en las varibales de table_static siendo Columna_origen y destino
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param Ninguno
    *
    *@return Ninguno
    *
    *@ejemplo
    *this.camposTableOrigen();
    *
    *@consideraciones
    *- Asegurarse que se hayan ingresado los datos correspondiente en el dto de table_static
    *- Necesita haber valores en la variable tablas_dtn, User_d, User_o, columnas_tablas_de y columnas_tablas_o
    */  
    void camposTableOrigen();

    /**
    *Se encarga de leer las configuraciones establecidas anteriormente para lograr la migracion de datos
    *a la base destino, por medio de un archivo csv.
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param Ninguno
    *
    *@return Ninguno
    *
    *@ejemplo
    *this.trasladarData();
    *
    *@consideraciones
    *- haber establecido la configuracion previa a la lectura
    */
    void trasladarData();

    /**
    *Obtiene la cantidad de tablas que le pertenecen al usaurio destino
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param Ninguno
    *
    *@return Integer
    *
    *@ejemplo
    *this.obtenerCant_tbl_dest();
    *
    */
    Integer obtenerCant_tbl_dest();

    /**
    *Almacena en las respectivas variables los usaurios origen y destino con que se va a trabajar,
    *los almacena en las variables de la clase value_static para su uso posterior
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param Ninguno
    *
    *@return Ninguno
    *
    *@ejemplo
    *this.guardarUsuarios();
    *
    *
    */
    void guardarUsuarios();

    /**
    *Escirbe la configuracion establecida en las clases value_static y table_static en un archivo de texto
    *de tipo csv.
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param Ninguno
    *
    *@return Ninguno
    *
    *@ejemplo
    *this.guardarDatosTabla();
    *
    */
    void guardadDatosTabla();

    /**
    *Toma la decision de que hacer en caso de que fuera establecida una sentecia sql o 
    *una tabla para su respectiva relacion
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param 
    * table_static
    * boolean
    * value_static
    *
    *@return Ninguno
    *
    *@ejemplo
    *this.migrarDatos();
    *
    *
    */
    void migrarDatos(table_static ts_m, boolean dn, value_static vs_m);

    /**
    *Limpia la tabla destino 
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *
    *@param 
    * Ninguno
    *
    *@return
    * Ninguno
    *
    *@ejemplo
    *this.limpiarTablas();
    *
    *@consideraciones
    *- Haber declarado los usuarios destino y origen
    */
    void limpiarTablas();

}
