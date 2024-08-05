package com.etls.etl_uso.Service.Impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.hibernate.internal.ExceptionConverterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ExceptionTypeFilter;

import com.etls.etl_uso.Manejo_archivos.readFile;
import com.etls.etl_uso.Manejo_archivos.writeFile;
import com.etls.etl_uso.Repository.Etl_Repository;
import com.etls.etl_uso.Service.Etl_Service;
import com.etls.etl_uso.Static_Value.table_static;
import com.etls.etl_uso.Static_Value.value_static;

@Service
public class Etl_Service_Imp implements Etl_Service{

    private Etl_Repository er = new Etl_Repository();

    private value_static vs = new value_static();

    private table_static ts = new table_static();

    private boolean decision = false;

    //Obtener la tabla Origen
    @Override
    public void obtener_tablasOrigen() {
        try {
            if(ts.getTablas_origen() == null){
                List<String> tablas_usuario = er.getTablesUsers(vs.getUser_o());
                ts.setTablas_origen(tablas_usuario);
            }
            String resultado = new String();
            Map<Integer, String> value_table = new HashMap<>();
            Integer value = 0;
            Scanner entrada = new Scanner(System.in);
    
            System.out.println("\n");
            for(String table : ts.getTablas_origen()){
                value += 1;
                System.out.println(value + ". " + table);
                value_table.put(value, table);
            }
    
            System.out.println("\n");
            System.out.println("Ingrese la tabla origen: ");
    
            resultado = entrada.nextLine();

            if (Integer.valueOf(resultado) < value_table.size()) {
                ts.setTablas_ogn(value_table.get(Integer.valueOf(resultado)));
                ts.getTablas_origen().remove(ts.getTablas_ogn());    
            }
            else{
                System.out.println("Tabla no existe");
                this.obtenerUsuarioOrigen();
            }
    
            
        } catch (Exception e) {
            System.out.println("No se ha podido mostrar las tablas del usuario: " + vs.getUser_o() + " por la exception: ");
            System.err.println(e.getMessage());
            this.obtener_tablasOrigen();
        }
    }

    //Obtener la tabla Destino
    @Override
    public void obtener_tablasDestino() {
        try {
            if(ts.getTablas_destino() == null){
                List<String> tablas_usuario = er.getTablesUsers(vs.getUser_d());
                ts.setTablas_destino(tablas_usuario);
            }
            String resultado = new String();
            Map<Integer, String> value_table = new HashMap<>();
            Integer value = 0;
            Scanner entrada = new Scanner(System.in);
    
            System.out.println("\n");
            for(String table : ts.getTablas_destino()){
                value += 1;
                System.out.println(value + ". " + table);
                value_table.put(value, table);
            }
    
            System.out.println("\n");
            System.out.println("Ingrese la tabla Destino: ");
    
            resultado = entrada.nextLine();
    
            if (Integer.valueOf(resultado) <= value_table.size()){
                ts.setTablas_dtn(value_table.get(Integer.valueOf(resultado)));
                ts.getTablas_destino().remove(ts.getTablas_dtn());
            }else{
                System.out.println("Tabla no encontrada");
                this.obtenerCant_tbl_dest();
            }
        } catch (Exception e) {
            System.out.println("Ha ocurrido una exception al llamar las tablas destino: ");
            System.err.println(e.getMessage());            
        }
    }

    //Obtener el usuario origen 
    @Override
    public void obtenerUsuarioOrigen() {
        List<String> usuarios = er.getUsernames();
        String resultado = new String();
        Map<Integer, String> value_user = new HashMap<>();
        Integer value = 0;
        Scanner entrada = new Scanner(System.in);

        System.out.println("Que usuario desea que sea el origen: \n");
        for (String user : usuarios ){
            if(user.matches("C##\\w+")){
                value += 1;
                System.out.println(value + ". " + user);
                value_user.put(value, user);
            }
        }

        System.out.println("\n");
        System.out.println("Ingrese su opcion de Origen: ");

        
        resultado = entrada.nextLine();
        

        vs.setUser_o(value_user.get(Integer.valueOf(resultado)));
        
    }

    //Obtener Usuario Destino
    @Override
    public void obtenerUsuarioDestino() {
        List<String> usuarios = er.getUsernames();
        String resultado = new String();
        Map<Integer, String> value_user = new HashMap<>();
        Integer value = 0;
        Scanner entrada = new Scanner(System.in);

        System.out.println("Que usuario desea que sea el destino: \n");
        for (String user : usuarios ){
            if(user.matches("C##\\w+")){
                value += 1;
                System.out.println(value + ". " + user);
                value_user.put(value, user);
            }
        }

        System.out.println("\n");
        System.out.println("Ingrese su opcion de Destino: ");

        resultado = entrada.nextLine();

        vs.setUser_d(value_user.get(Integer.parseInt(resultado)));
    }

    //Tomar una decision si usar una tabla origen o una sentencia
    @Override
    public void decisionSentTabla() {
        try {
            Scanner entrada = new Scanner(System.in);
        Integer i = 0;

        System.out.println("Tablas destinos: ");
        this.obtener_tablasDestino();
        System.out.println("\n Que desea hacer: \n 1. Ingresar la sentecia de la tbl destino \n 2. Escoger una base de datos");
        i = Integer.valueOf(entrada.nextLine());
        

        switch (i) {
            case 1:
            decision = true;
            System.out.println("\n Ingrese la sentencia SQL: ");
            ts.setSQL_Sentence(entrada.nextLine());
            
            
            this.prepararSentencia();
                break;
            case 2:
            decision = false;
            this.obtener_tablasOrigen();
                break;
            default:
                System.out.println("fuera del rango");
                this.decisionSentTabla();
                break;
        }
        } catch (Exception e){
            System.out.println("Error al momento de tomar la decision");
            System.err.println("Exception: " + e.getMessage());
            this.decisionSentTabla();
        }
        
        
    }

    @Override
    public void camposTableOrigen() {
       
        try {
            Map<Integer,String> columnas_opciones = new HashMap<>();
            Scanner entrada = new Scanner(System.in);
            String resultado = new String();
            Integer value_map = 0;            
    
            
            ts.setColumnas_tabla_de(er.camposTabla(ts.getTablas_dtn(), vs.getUser_d()));

            ts.setColumna_origen(null);
                       
    
            for (String tabla_destino: ts.getColumnas_tabla_de()){
                Map<Integer, String> columna_origen = new HashMap<>();
                Map<Integer, String> columna_destino = new HashMap<>();
                
    
                if (!decision) {
                
                    ts.setColumnas_tabla_or(er.camposTabla(ts.getTablas_ogn(), vs.getUser_o()));
                    
                    Integer value = 0;
                    System.out.println("\n Columna a relacionar de la tabla: " + ts.getTablas_ogn());
                    for (String columna : ts.getColumnas_tabla_or()) {
                        value += 1;
                        System.out.println(value + ". " + columna);
                        columnas_opciones.put(value, columna);
                    }
        
                    System.out.println("\n Tabla destino a relacionas es: " + ts.getTablas_dtn() + " Con el campo " + tabla_destino);
                    System.out.println("\n Tabla origen a relacionar: ");
                    resultado = entrada.nextLine();
        
                }
                //fin del if
                else{
                    
                    ts.setColumnas_tabla_or(er.camposSentencia(ts.getSQL_Sentence()));
                    
                    Integer value = 0;
    
                    System.out.println("\n Columna a relacionar de la sentecia dictada: ");
    
                    for (String columna : ts.getColumnas_tabla_or()) {
                        value += 1;
                        System.out.println(value + ". " + columna);
                        columnas_opciones.put(value, columna);
                    }
        
                    System.out.println("\n Tabla destino a relacionas es: " + ts.getTablas_dtn() + " Con el campo " + tabla_destino);
                    System.out.println("\n Tabla origen a relacionar: ");
                    resultado = entrada.nextLine();
    
                    ts.setTablas_ogn(ts.getSQL_Sentence());
    
                    
                }
                        
                    
                if (ts.getColumna_destino() == null || ts.getColumna_origen() == null){

                    columna_origen.put(value_map, columnas_opciones.get(Integer.valueOf(resultado)));
                    columna_destino.put(value_map, tabla_destino);
    
                    ts.setColumna_origen(columna_origen);
                    ts.setColumna_destino(columna_destino);
                }
                else{
                    ts.getColumna_origen().put(value_map, columnas_opciones.get(Integer.valueOf(resultado)));
                    ts.getColumna_destino().put(value_map, tabla_destino);
                }
                value_map ++;
    
                
    
            }
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error al momento de la asignacion");
            System.err.println("Exception: " + e.getMessage());
            this.camposTableOrigen();
        }

        
    }

    @Override
    public void trasladarData() {
       
        readFile rf = new readFile();

        rf.leerPrimeraLinea();
        rf.leerLineaDecisiones();

    }



    //Prepara la sentencia sql para su ejecucion
    private void prepararSentencia(){
        String oracion = ts.getSQL_Sentence();
        String[] resultado_partes = oracion.split("[f,F][r,R][o,O][m,M]");
        StringBuilder sb = new StringBuilder();

        if(ts.getSQL_Sentence().matches(".+[j,J][o,O][i,I][n,N].+")){

            for (String partes : resultado_partes) {
                if (partes.matches(".+[j,J][o,O][i,I][n,N].+")){
                    String[] sub_partes = partes.split("[j,J][o,O][i,I][n,N]");
                    
                    for (String partes_join : sub_partes) {
                        if(!sb.toString().matches(".+[f,F][r,R][o,O][m,M].+")){
                            sb.append(" from ").append(vs.getUser_o() + ".").append(partes_join.trim());	
                            
                        }
                        else{
                            sb.append(" join ").append(vs.getUser_o() + ".").append(partes_join.trim());
                            
                        }
                        
                        
                    }
                    
                }	
                else{
                    sb.append(partes);
                }
             }
        }
        else{
            for (String partes : resultado_partes) {
                if(partes.matches("[s,S][e,E][l,L][e,E][c,C][t,T].+")){
                    sb.append(partes);
                }
                else{
                    sb.append(" from ").append(vs.getUser_o() +".").append(partes);
                }
            }
        }
        //fin if principal

        ts.setSQL_Sentence(sb.toString());

        }

    @Override
    public Integer obtenerCant_tbl_dest() {
        try {
            return er.cantTabla_destino(vs.getUser_d());
        } catch (Exception e) {
            System.out.println("El usuario no tiene ninguna tabla asignada");
            System.err.println("Exception: " +e.getMessage());
            this.obtenerUsuarioDestino();
            return null;
        }
    }

    @Override
    public void guardarUsuarios() {
        
        writeFile wf = new writeFile();

        wf.escribirUsuarios(vs);

    }

    @Override
    public void guardadDatosTabla() {

        writeFile wf = new writeFile();

        wf.escribirDatosTablas(ts, decision);
    }

    @Override
    public void migrarDatos(table_static ts_m, boolean dn, value_static vs_m) {
     
        if(dn){
            er.migrarDatosSnt(ts_m, vs_m);
        }else{
            er.migrarDatosTbl(ts_m, vs_m);
        }

    }

    @Override
    public void limpiarTablas() {
        er.limpiarTablas(vs.getUser_d());
    }

    }
