package com.etls.etl_uso;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.etls.etl_uso.Manejo_archivos.writeFile;
import com.etls.etl_uso.Service.Impl.Etl_Service_Imp;

@SpringBootApplication
public class EtlUsoApplication {

	private void opcionesInicio(){
		Scanner entrada = new Scanner(System.in);
		
		Etl_Service_Imp esi = new Etl_Service_Imp(); 				
		
		System.out.println("Que desea hacer: \n 1. Ingresar nueva relacion \n 2. Leer relacion establecida \n 3.Limpiar base");
		String value = entrada.nextLine();
		
		try {
			switch (value) {
				case "1":
						esi.obtenerUsuarioOrigen();
						esi.obtenerUsuarioDestino();
						esi.limpiarTablas();
						
						
						
						esi.guardarUsuarios();
						for(int i = 1; i <= esi.obtenerCant_tbl_dest(); i++){
							esi.decisionSentTabla();
							esi.camposTableOrigen();
							esi.guardadDatosTabla();
						} 
						
	
						esi.trasladarData(); 
					break;
				case "2":
						esi.obtenerUsuarioOrigen();
						esi.obtenerUsuarioDestino();
						esi.limpiarTablas();
	
						esi.trasladarData();
						break;
				case "3":
						esi.obtenerUsuarioOrigen();
						esi.obtenerUsuarioDestino();
						esi.limpiarTablas();
						break;

				default:
						throw new IllegalAccessException("Opcion no disponible");					
			}
		} catch (IllegalArgumentException e) {
			System.out.println();
			System.err.println(e.getMessage());
			this.opcionesInicio();
		}catch (Exception eP){
			System.out.println("Ha ocurrido un error al momento de dictar que hacer");
			this.opcionesInicio();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(EtlUsoApplication.class, args);		
		EtlUsoApplication eua = new EtlUsoApplication();
		
		eua.opcionesInicio();


		 System.exit(0);
		 
	}
		 

}
