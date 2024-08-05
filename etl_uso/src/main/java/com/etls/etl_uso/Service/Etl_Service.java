package com.etls.etl_uso.Service;

import java.util.List;
import java.util.Map;

import com.etls.etl_uso.Static_Value.table_static;
import com.etls.etl_uso.Static_Value.value_static;

public interface Etl_Service {
    
    void obtener_tablasOrigen();

    void obtener_tablasDestino();

    void obtenerUsuarioOrigen();

    void obtenerUsuarioDestino();

    void decisionSentTabla();

    void camposTableOrigen();

    void trasladarData();

    Integer obtenerCant_tbl_dest();

    void guardarUsuarios();

    void guardadDatosTabla();

    void migrarDatos(table_static ts_m, boolean dn, value_static vs_m);

    void limpiarTablas();

}
