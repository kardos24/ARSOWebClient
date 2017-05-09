package cliente;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import modelo.Favoritos;
import modelo.ListadoProgramas;

import servicio.tipos.Programa;

public class Cliente {
	
	private static final String IP = "http://localhost";
	private static final String PORT = "8080";
	private static final String URL_SERVER = IP + ":" + PORT;
	private static final String URL_SERVICE = URL_SERVER + "/ServicioWebArSo/rest";
	private static final String URL_SERVICE_PROGRAM = URL_SERVICE + "/programas";

	public static void main(String[] args) {
		Client cliente = new Client();
		
//		Obtener el listado de programas en XML. Muestra por la consola los nombres.		
		WebResource resource = cliente.resource(URL_SERVICE_PROGRAM);
		ClientResponse respuesta = resource.method("GET", ClientResponse.class);
		ListadoProgramas listProgram = respuesta.getEntity(ListadoProgramas.class);
		System.out.println("Numero de programa: " +  listProgram.getProgramList().size());
		listProgram.getProgramList().stream().map(p->p.getTitulo()).forEach((titulo)->{
			System.out.println("	- Programa: " +  titulo);
		});
		
//		Recuperar el programa “Acacias 38” (acacias-38) en formato XML. Muestra por la consola el número de emisiones.
		resource = cliente.resource(URL_SERVICE_PROGRAM + "/acacias-38");
		respuesta = resource.method("GET", ClientResponse.class);
		Programa program = respuesta.getEntity(Programa.class);
		System.out.println("Programa de acacias: " +  program.getNombre());
		program.getEmision().stream().map(p->p.getTitulo()).forEach((titulo)->{
			System.out.println("	- Emisión: " +  titulo);
		});
		
//		Recuperar el programa filtrado “Acacias 38” (acacias-38) en formato XML.
//		Muestra por la consola el número de emisiones.
		String filtro = "6";
		resource = cliente.resource(URL_SERVICE_PROGRAM + "/acacias-38/filtro?titulo="+filtro);
		respuesta = resource.method("GET", ClientResponse.class);
		Programa programFiltra = respuesta.getEntity(Programa.class);
		System.out.println("Programa de acacias filtrado: " +  programFiltra.getNombre());
		programFiltra.getEmision().stream().map(p->p.getTitulo()).forEach((titulo)->{
			System.out.println("	- Emisión filtrada por " + filtro + ": " +  titulo);
		});
		
//		Crear un documento de favoritos. Registrar en el documento de favoritos los programas “Acacias 38” (acacias-38) y “Águila Roja” (aguila-roja).
		resource = cliente.resource(URL_SERVICE_PROGRAM);
		respuesta = resource.method("POST", ClientResponse.class);
		String[] arrayUrl = respuesta.getLocation().getPath().toString().split("/");
		String idFavorito = arrayUrl[arrayUrl.length-1];
		System.out.println("Favorito creado: " +  idFavorito + " con código retorno " + respuesta.getStatus());
		
		resource = cliente.resource(URL_SERVICE_PROGRAM + "/favoritos/" + idFavorito + "/programa/acacias-38");
		respuesta = resource.method("POST", ClientResponse.class);
		System.out.println("Añadido acacias-38 a favorito: " +  idFavorito + " con código retorno " + respuesta.getStatus());

		resource = cliente.resource(URL_SERVICE_PROGRAM + "/favoritos/" + idFavorito + "/programa/aguila-roja");
		respuesta = resource.method("POST", ClientResponse.class);
		System.out.println("Añadido aguila-roja a favorito: " +  idFavorito + " con código retorno " + respuesta.getStatus());
		
//		Recuperar el documento de favoritos en XML y muestra su contenido por la consola.
		resource = cliente.resource(URL_SERVICE_PROGRAM + "/favoritos/"+idFavorito);
		respuesta = resource.method("GET", ClientResponse.class);
		Favoritos favorito = respuesta.getEntity(Favoritos.class);
		System.out.println("Favorito recuperado: " +  favorito.getId());
		favorito.getProgramList().stream().map(p->p.getTitulo()).forEach((titulo)->{
			System.out.println("	- Programa favorito: " +  titulo);
		});
				
//		Borrar de los favoritos “Águila Roja” (aguila-roja).
		resource = cliente.resource(URL_SERVICE_PROGRAM + "/favoritos/" + idFavorito + "/programa/aguila-roja");
		respuesta = resource.method("DELETE", ClientResponse.class);
		System.out.println("Favorito borrado: " +  idFavorito + " del programa aguila-roja con código retorno " + respuesta.getStatus());
		
//		Recuperar y mostrar de nuevo el documento de favoritos.
		resource = cliente.resource(URL_SERVICE_PROGRAM + "/favoritos/"+idFavorito);
		respuesta = resource.method("GET", ClientResponse.class);
		Favoritos favorito2 = respuesta.getEntity(Favoritos.class);
		System.out.println("Favorito recuperado por segunda vez: " +  favorito2.getId());
		favorito2.getProgramList().stream().map(p->p.getTitulo()).forEach((titulo)->{
			System.out.println("	- Programa favorito: " +  titulo);
		});
		
//		Obtener el listado de programas en JSON. Nótese que en este caso la información se recupera como un String.	
		resource = cliente.resource(URL_SERVICE_PROGRAM);
		respuesta = resource.accept(MediaType.APPLICATION_JSON).method("GET", ClientResponse.class);
		String listProgramJSON = respuesta.getEntity(String.class);
		System.out.println("Listado de programas en JSON: " +  listProgramJSON);
		
		// Prueba atom
		resource = cliente.resource(URL_SERVICE_PROGRAM + "/aguila-roja/atom");
		respuesta = resource.accept(MediaType.APPLICATION_ATOM_XML).method("GET", ClientResponse.class);
		String resultadoAtom = respuesta.getEntity(String.class);
		System.out.println("\tResultado ATOM\n");
		System.out.println(resultadoAtom);
	}

}
