package com.etls.etl_uso.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.etls.etl_uso.DataStatic.valueSttc;
import com.etls.etl_uso.Static_Value.table_static;
import com.etls.etl_uso.Static_Value.value_static;


public class Etl_Repository {

    Connection conn;
    
    //Obtener los usuarios de la base de datos creados
    public List<String> getUsernames(){
        try {
            List<String> usuarios = new LinkedList<>();
            Connection conn = DriverManager.getConnection(valueSttc.url, valueSttc.user, valueSttc.password);
            PreparedStatement ps = conn.prepareStatement("SELECT username FROM dba_users");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                usuarios.add(rs.getString("username"));
            }

            return usuarios;
        } catch (Exception e) {
            System.out.println("No se han podido mostrar los usuarios, debido a la exception: " + e.getMessage());
            return null;
        }
    }

    //Obtener los tablas de los usuarios
    public List<String> getTablesUsers(String user){
        try {
            List<String> tablas = new LinkedList<>();
            Connection conn = DriverManager.getConnection(valueSttc.url, valueSttc.user, valueSttc.password);
            PreparedStatement ps = conn.prepareStatement("SELECT table_name FROM all_tables WHERE owner = ? ORDER BY table_name");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tablas.add(rs.getString("table_name"));
            }

            return tablas;
        } catch (Exception e) {
            System.out.println("Error obteniendo las tablas del usuario " + user);
            return null;
        }
    }

    public Integer cantTabla_destino(String user){
        try {
            List<String> tablas = new LinkedList<>();
            Connection conn = DriverManager.getConnection(valueSttc.url, valueSttc.user, valueSttc.password);
            PreparedStatement ps = conn.prepareStatement("SELECT table_name FROM all_tables WHERE owner = ? ORDER BY table_name");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tablas.add(rs.getString("table_name"));
            }

            return tablas.size();
        } catch (Exception e) {
            return null;
        }
    }

    //Obtener los campos de la tabla
    public List<String> camposTabla(String tabla, String user){
        try {
            List<String> tablas_columna = new LinkedList<>();
            Connection conn = DriverManager.getConnection(valueSttc.url, valueSttc.user, valueSttc.password);
            
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + user + "." + tabla );
        
            ResultSet rs = ps.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int column_count = metaData.getColumnCount();

            for (int i = 1; i <= column_count; i++){
                tablas_columna.add(metaData.getColumnName(i));
            }

            conn.close();

            return tablas_columna;

        } catch (Exception e) {
            System.out.println("No se ha podido obtener la columna de la tabla : "  + tabla + " del usuario: " + user);
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<String> camposSentencia(String sentence_SQL){
        try {
            List<String> campos_Sentencia = new LinkedList<>();            

            Connection conn = DriverManager.getConnection(valueSttc.url, valueSttc.user, valueSttc.password);
            PreparedStatement ps = conn.prepareStatement(sentence_SQL);
            ResultSet rs = ps.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int column_count  = metaData.getColumnCount();
                        
            for (int i = 1; i <= column_count; i++){
                campos_Sentencia.add(metaData.getColumnLabel(i));
            }

            return campos_Sentencia;
            
        } catch (Exception e) {
            System.out.println("Sentencia no reconocida, salto la exception: ");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void limpiarTablas(String user){
        try {
            List<String> tablas = this.getTablesUsers(user);

            Connection conn = DriverManager.getConnection(valueSttc.url, valueSttc.user, valueSttc.password);
            conn.setAutoCommit(false);
                            
            for (String tabla : tablas) {                
                String sql = String.format("DELETE FROM %s.%s", user, tabla);
                try {
                    PreparedStatement ps = conn.prepareStatement(sql);
                

                    ps.executeQuery();
                } catch (Exception e) {
                    conn.rollback();
                }
                
                conn.commit();
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error al momento de limpiar las tablas");
            System.err.println("Exception: " + e.getMessage());
        }
    }

    //Migrar los datos con una sentencia
    public void migrarDatosSnt(table_static ts, value_static vs){


        try {
            PreparedStatement ps;
            ResultSet rs;
            PreparedStatement ps_dest;
            conn = DriverManager.getConnection(valueSttc.url, valueSttc.user, valueSttc.password);
            conn.setAutoCommit(false); // Deshabilitar el auto-commit para manejar la transacción manualmente

            ps = conn.prepareStatement(ts.getSQL_Sentence());
            rs = ps.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();
            
            
            

            while (rs.next()) {
                StringBuilder insertSQL = this.generarInsert(ts, vs);
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    
                    String columnName = rsm.getColumnName(i);
                    String valor = rs.getString(i);

                    if (ts.getColumna_origen().containsValue(columnName)) {
                        
                        
                        if(this.indiceMapa(ts.getColumna_origen(), columnName) == ts.getColumna_destino().size()){
                            if (rsm.getColumnType(i) == 1 || rsm.getColumnType(i) == 12) {
                                insertSQL.append("'" + valor.replace("'", "`") + "')");
                            }else{
                                insertSQL.append(valor.replace("'", "`") + ")");
                            }
                        }
                        else{
                            if (rsm.getColumnType(i) == 1 || rsm.getColumnType(i) == 12) {
                                insertSQL.append("'" + valor.replace("'", "`") + "'," );
                            }else{
                                insertSQL.append(valor.replace("'", "`") + ",");
                            }
                        }
                        
                    }
                }
                String sql = insertSQL.toString();
                ps_dest = conn.prepareStatement(sql);
                ps_dest.executeQuery();
                    
            }

            
            conn.commit(); 
            System.out.println("Datos insertados correctamente");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException se) {
                    System.err.println("Rollback failed: " + se.getMessage());
                }
            }
        }

    }

    //Migrar los datos de una tabla
    public void migrarDatosTbl(table_static ts, value_static vs){
        try {

            Connection conn = DriverManager.getConnection(valueSttc.url, valueSttc.user, valueSttc.password);
            PreparedStatement ps = conn.prepareStatement(this.obtenerDatosOgn(ts, vs));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                
            }
            

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private String obtenerDatosOgn(table_static ts, value_static vs){
        StringBuilder sb = new StringBuilder();

            sb.append("SELECT ");
            
            for(int i = 1; i <= ts.getColumna_origen().size(); i++){
                if(i == ts.getColumna_origen().size()){
                    sb.append(ts.getColumna_origen().get(i) + " ");
                }else{
                    sb.append(ts.getColumna_origen().get(i) + ", ");
                }
            }

            sb.append(String.format("from %s.%s", vs.getUser_o(), ts.getTablas_ogn()));

            return sb.toString();
    }

    private StringBuilder generarInsert(table_static ts, value_static vs){

       StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(vs.getUser_d()).append(".").append(ts.getTablas_dtn()).append(" VALUES (");

       
        return sb;

    }
    
    private Integer indiceMapa(Map<Integer, String> mapa, String value) {
        for (Map.Entry<Integer, String> entry : mapa.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
}
