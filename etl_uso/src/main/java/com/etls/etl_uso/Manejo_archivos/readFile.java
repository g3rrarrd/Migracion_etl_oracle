package com.etls.etl_uso.Manejo_archivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.etls.etl_uso.Service.Impl.Etl_Service_Imp;
import com.etls.etl_uso.Static_Value.table_static;
import com.etls.etl_uso.Static_Value.value_static;

public class readFile {

    File file = new File("../tabla_conf.txt");
    Integer iteracion_cnt = 0;
    value_static vs = new value_static();
    boolean first_value = false;
    boolean decision;
    Integer value_split = 0;

    Etl_Service_Imp esi = new Etl_Service_Imp();

    public void leerPrimeraLinea(){

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            String[] valores;

            while ((linea = br.readLine()) != null){

                valores = linea.split(",");
                for (String acceso : valores) {
                    if (iteracion_cnt == 0) {
                        vs.setUser_o(acceso);
                        iteracion_cnt++;
                    }else{
                        vs.setUser_d(acceso);
                    }
                }
                break;

            }



        } catch (IOException e) {
            System.err.println("Error al leer el archivo");
        }

    }

    public void leerLineaDecisiones(){
        try {            
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            String[] valores;            
            iteracion_cnt = 0;
            

            while ((linea = br.readLine()) != null) {
                    
                    table_static ts = new table_static();
                    Map<Integer, String> columnas_origen = new HashMap<>();
                    Map<Integer, String> columnas_destino = new HashMap<>();
                
                    valores = linea.split(",,");

                                    

                    if(iteracion_cnt == 0){
                        iteracion_cnt = 1;
                    }
                    else{
                        iteracion_cnt = 1;
                        decision = valores[0].matches("[t,T][r,R][u,U][e,E]");                    
                        value_split = 0;

                        for (String value_read : valores) {

                            switch (value_split) {
                                case 0:
                                    value_split ++;
                                    break;
                                case 1:
                                    if(decision){
                                        ts.setSQL_Sentence(value_read);
                                    }else{
                                        ts.setTablas_ogn(value_read);
                                    }
                                    value_split ++;
                                    break;
                                case 2:
                                    ts.setTablas_dtn(value_read);
                                    value_split ++;
                                    break;                                
    
                                default:
                                    if ((value_split%2) == 0) {
                                        columnas_destino.put(iteracion_cnt, value_read);
                                        iteracion_cnt ++;
                                    }else{
                                        columnas_origen.put(iteracion_cnt, value_read);
                                    }
                                    value_split++;
                                    break;
                            }
                        
                        }

                        ts.setColumna_origen(columnas_origen);
                        ts.setColumna_destino(columnas_destino);

                        esi.migrarDatos(ts, decision, vs);
                    }

                    
            }
            

        } catch (IOException e) {
            System.out.println("Error leyendo el archivo");
        }
    }
    
}
