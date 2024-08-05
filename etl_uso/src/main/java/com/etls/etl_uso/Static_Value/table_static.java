package com.etls.etl_uso.Static_Value;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class table_static {
    
    private String tablas_ogn;

    private String tablas_dtn;

    private String SQL_Sentence;

    private String SQL_Sentence_Values;

    private String campos_tabla_origen;

    private List<String> tablas_origen;

    private List<String> tablas_destino;

    private List<String> columnas_tabla_or;

    private List<String> columnas_tabla_de;

    private Map<Integer, String> columna_origen = null;

    private Map<Integer, String> columna_destino = null;

    
}
