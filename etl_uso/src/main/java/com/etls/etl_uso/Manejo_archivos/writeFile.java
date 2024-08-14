package com.etls.etl_uso.Manejo_archivos;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;

import com.etls.etl_uso.EtlUsoApplication;
import com.etls.etl_uso.Static_Value.table_static;
import com.etls.etl_uso.Static_Value.value_static;


public class writeFile {

    File files = new File("../tabla_conf.txt");

     /**
    *Escribe solamente en la primera linea del archivo csv a los usuarios destino y origen
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *@param
    * value_static vs
    */
    public void escribirUsuarios(value_static vs){

        try {
            if(!this.comprobarExistencia()){
                files.createNewFile();
            }
            
            FileWriter fw = new FileWriter(files);                                            
            
            fw.write(vs.getUser_o() + ',' + vs.getUser_d() + '\n');
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error a la hora de guardar los datos");
        }

    }

     /**
    *Escribe la configuracion establecida de las tablas, con la relacion entre ellas y sus campos
    *
    *
    *@autor
    *Gerardo Antonio Rodriguez Contreras
    *@correo    
    *g_rodriguez51@outlook.com
    *
    *@param
    * table_static t
    * boolean decision
    *
    */
    public void escribirDatosTablas(table_static ts, boolean decision){
        try {
            
            FileWriter fw = new FileWriter(files, true);
            StringBuilder sb = new StringBuilder();

            sb.append(decision + ",,");
            sb.append(ts.getTablas_ogn() + ",," + ts.getTablas_dtn() + ",,");

            for (Integer i = 0; i < ts.getColumna_destino().size(); i++) {
                if (i == ts.getColumna_destino().size()-1) {
                    sb.append(ts.getColumna_origen().get(i));
                    sb.append(",," + ts.getColumna_destino().get(i) + "\n");
                }else{
                    sb.append(ts.getColumna_origen().get(i));
                    sb.append(",," + ts.getColumna_destino().get(i) + ",,");
                }
            }

            fw.append(sb.toString());

            fw.close();

            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Ha ocurrido un error al guardar la tablas");
        }
    }

    private boolean comprobarExistencia(){

        if (files.exists()){
            return true;
        }
        return false;
    }
    
    

}
