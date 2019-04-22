package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.SQLException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;
import resources.PostResource;
import resources.UserResource;


public class Cliente {
    
    private final static String HOST = "http://localhost:8080/";
  
	private ClientConfig config;
	private Client client; 
	private WebTarget target;
	
	
	public Cliente() {
		this.config = new ClientConfig();
		this.client = ClientBuilder.newClient(config);
		this.target=this.client.target(getBaseURI());
	}
	public static void main(String[] args) throws SQLException, NumberFormatException, IOException {
	  System.out.println("HHH");
				new Cliente().init();	
	}
	
	private void init() throws NumberFormatException, IOException, SQLException {
		int option=0;
		do {
			System.out.println("Bienvenido");
			System.out.println("Elija una de las siguientes opciones:");
			System.out.println("1: Crear un usuario");
			System.out.println("2: Eliminar un usuario");
			System.out.println("3: Editar un usuario");
			System.out.println("4: Obtener los usuarios de la base de datos");
			System.out.println("5: Crear un post");
			System.out.println("6: Eliminar un post");
			System.out.println("7: Editar un post");
			System.out.println("8: Obtener los posts de un usuario");
			System.out.println("9: Obtener los posts de los amigos de un usuario");
			System.out.println("10: Enviar un mensaje");
			System.out.println("11: Añadir un amigo");
			System.out.println("12: Eliminar un amigo");
			System.out.println("13: Obtener los amigos de un usuario");
			System.out.println("14: Obtener información personal de un usuario");
			System.out.println("15: Terminar");
			option=readInt();
			switch(option) {
				case 1:
					crearUsuario();
					break;
				case 2:
					eliminarUsuario();
					break;
				case 3:
					editarUsuario();
					break;
				case 4:
					getUsers();
					break;
				case 5:
					crearPost();
					break;
				case 6:
					eliminarPost();
					break;
				case 7:
					editarPost();
					break;
				case 8:
					getPosts();
					break;
				case 9:
					getFriendsPosts();
					break;
				case 10:
					enviarChat();
					break;
				case 11:
					añadirAmigo();
					break;
				case 12:
					eliminarAmigo();
					break;
				case 13:
					getFriends();
					break;
				case 14:
					getUser();
					break;
				case 15:
					option=15;
					break;
			}
		} while(option !=15);
		System.exit(0);
	}
	
	private void getUsers() {
		System.out.println(this.target.path("user").request().accept(MediaType.APPLICATION_JSON).get(String.class));
	}
	private void getUser() {
		System.out.println(this.target.path("user").path("1").request().accept(MediaType.APPLICATION_JSON).get(String.class));
	}
	private void getFriends() {
		System.out.println(this.target.path("user").path("2").path("friends").request().accept(MediaType.APPLICATION_JSON).get(String.class));
	}
	private void eliminarAmigo() {
		System.out.println(this.target.path("user").path("2").path("friends").path("4").request().accept(MediaType.APPLICATION_JSON).delete());
	}
	private void añadirAmigo() {
		System.out.println(this.target.path("user").path("2").path("friends").path("6").request().accept(MediaType.APPLICATION_JSON).post(null));
	}
	private void enviarChat() throws IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Introduzca contenido: \n> ");
		String content = buffer.readLine();
		PostResource post = new PostResource();
		post.setContent(content);
		System.out.println(post.getContent());
		Response res = this.target.path("user").path("chat").queryParam("from", "3").queryParam("to","4")
				.request().accept(MediaType.APPLICATION_JSON).post(Entity.json(post), Response.class);
		System.out.println(res.getStatus());
		System.out.println(res);
	}
	private void getFriendsPosts() {
		System.out.println(this.target.path("user").path("1").path("posts").path("friends").request().accept(MediaType.APPLICATION_JSON).get(String.class));
	}
	private void getPosts() {
		System.out.println(this.target.path("user").path("1").path("posts").request().accept(MediaType.APPLICATION_JSON).get(String.class));
	}
	private void editarPost() throws IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Introduzca contenido: \n> ");
		String content = buffer.readLine();
		PostResource post = new PostResource(content);
		System.out.println(post.getContent());
		Response res = this.target.path("user").path("4").path("post").path("6").request().
				accept(MediaType.APPLICATION_JSON).put(Entity.json(post), Response.class);
		System.out.println(res);
	}
	private void eliminarPost() {
		System.out.println(this.target.path("user").path("4").path("post").path("5").request().accept(MediaType.APPLICATION_JSON).delete(String.class));
	}
	private void crearPost() throws IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Introduzca contenido: \n> ");
		String content = buffer.readLine();
		PostResource post = new PostResource(content);
		System.out.println(post.getContent());
		Response res = this.target.path("user").path("3").path("post").request().
				accept(MediaType.APPLICATION_JSON).post(Entity.json(post), Response.class);
		System.out.println(res);
	
	}
	
	private void editarUsuario() throws IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Introduzca username: \n> ");
		String username = buffer.readLine();
		System.out.print("Introduzca name: \n> ");
		String name = buffer.readLine();
		System.out.print("Introduzca email: \n> ");
		String email = buffer.readLine();
		System.out.print("Introduzca biography: \n> ");
		String bio = buffer.readLine();
		UserResource user= new UserResource(username, name, email, bio);
		Response res = this.target.path("user").path("2").request().accept(MediaType.APPLICATION_JSON).put(Entity.json(user), Response.class);
		System.out.println(res.getStatus());
		System.out.println(res);
		
	}
	private void eliminarUsuario() {
		System.out.println(this.target.path("user").path("7").request().accept(MediaType.APPLICATION_JSON).delete(String.class));
	}
	private void crearUsuario() throws SQLException, IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Introduzca username: \n> ");
		String username = buffer.readLine();
		System.out.print("Introduzca name: \n> ");
		String name = buffer.readLine();
		System.out.print("Introduzca email: \n> ");
		String email = buffer.readLine();
		System.out.print("Introduzca biography: \n> ");
		String bio = buffer.readLine();
		UserResource user= new UserResource(username, name, email, bio);
		Response res = this.target.path("user").request().accept(MediaType.APPLICATION_JSON).post(Entity.json(user), Response.class);
		System.out.println("ESTATUS " + res.getStatus());
		System.out.println("RESPUESTA " + res);
	}
	
	private static int readInt() throws NumberFormatException, IOException {
		System.out.print("> ");
		return Integer.parseInt(new BufferedReader(
				new InputStreamReader(System.in)).readLine());
	}
	private static URI getBaseURI() {
		return UriBuilder.fromUri(Cliente.HOST).build();
	}

}
