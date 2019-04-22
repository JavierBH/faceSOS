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
    private static final String HOST = "http://localhost:8080/";
	private ClientConfig config;
	private Client client; 
	private WebTarget target;
	public Cliente() {
		this.config = new ClientConfig();
		this.client = ClientBuilder.newClient(config);
		this.target=this.client.target(getBaseURI());
	}
	public static void main(String[] args) throws SQLException, NumberFormatException, IOException {
				new Cliente().init();
			
	}
	private void init() throws NumberFormatException, IOException, SQLException {
  	    crearUsuario();
        getUsers();
        crearPost();
        editarPost();
        eliminarPost();
        getPosts();
        getUser();
        añadirAmigo();
        getFriends();
        getFriendsPosts();
        eliminarAmigo();
        getFriends();
        enviarChat();
        editarUsuario();
        eliminarUsuario();
		System.out.println("Fin...");
		System.exit(0);
	
	}
	private void getUsers() {
		System.out.println("Obtener los usuario que tiene la aplicación:");
		System.out.println(this.target.path("user").request().accept(MediaType.APPLICATION_JSON).get(String.class));
		System.out.println(this.target.path("user").queryParam("page","2").request().accept(MediaType.APPLICATION_JSON).get(String.class));
	}
	private void getUser() {
		System.out.println("Obtener la información del usuario 1:");
		System.out.println(this.target.path("user").path("1").request().accept(MediaType.APPLICATION_JSON).get(String.class));
	}
	private void getFriends() {
		System.out.println("Obtener los amigos del usuario 1:");
		System.out.println(this.target.path("user").path("2").path("friends").request().accept(MediaType.APPLICATION_JSON).get(String.class));
	}
	private void eliminarAmigo() {
		System.out.println(this.target.path("user").path("2").path("friends").path("4").request().accept(MediaType.APPLICATION_JSON).delete(String.class));
	}
	private void añadirAmigo() {
		System.out.println("El usuario 1 añade como amigo al usuario 2:");
		System.out.println(this.target.path("user").path("1").path("friends").path("2").request().accept(MediaType.APPLICATION_JSON).post(null));
		
		System.out.println("El usuario 1 añade como amigo al usuario 3:");
		System.out.println(this.target.path("user").path("1").path("friends").path("3").request().accept(MediaType.APPLICATION_JSON).post(null));
	
	}
	private void enviarChat() throws IOException {
		System.out.println("El usuario 1 envia un chat al usuario 4:");
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Introduzca contenido: \n> ");
		String content = buffer.readLine();
		PostResource post = new PostResource();
		post.setContent(content);
		Response res = this.target.path("user").path("chat").queryParam("from", "1").queryParam("to","4")
				.request().accept(MediaType.APPLICATION_JSON).post(Entity.json(post), Response.class);
		System.out.println(res.getStatus());
		System.out.println(res);
	}
	private void getFriendsPosts() {
		System.out.println("Obtener los post de los amigos del usuario 1:");
		System.out.println(this.target.path("user").path("1").path("posts").path("friends").request().accept(MediaType.APPLICATION_JSON).get(String.class));
	}
	private void getPosts() {
		System.out.println("Obtener los posts del usuario 3");
		System.out.println(this.target.path("user").path("3").path("posts").request().accept(MediaType.APPLICATION_JSON).get(String.class));
	}
	private void editarPost() throws IOException {
		System.out.println("El usuario 3 edita el post 2:");
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Introduzca contenido: \n> ");
		String content = buffer.readLine();
		PostResource post = new PostResource(content);
		Response res = this.target.path("user").path("3").path("post").path("2").request().
				accept(MediaType.APPLICATION_JSON).put(Entity.json(post), Response.class);
		System.out.println(res);
	}
	private void eliminarPost() {
		System.out.println("El usuario 3 elimina el post 1:");
		System.out.println(this.target.path("user").path("4").path("post").path("5").request().accept(MediaType.APPLICATION_JSON).delete(String.class));
	}
	private void crearPost() throws IOException {
		System.out.println("El usuario 3 escribe un post");
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Introduzca contenido: \n> ");
		String content = buffer.readLine();
		PostResource post = new PostResource(content);
		Response res = this.target.path("user").path("3").path("post").request().
				accept(MediaType.APPLICATION_JSON).post(Entity.json(post), Response.class);
		System.out.println("Status: "+res.getStatus());
		System.out.println("Respuesta: "+res);
		
		System.out.println("El usuario 3 escribe un post");
		System.out.print("Introduzca contenido: \n> ");
		content = buffer.readLine();
		post = new PostResource(content);
		res = this.target.path("user").path("3").path("post").request().
				accept(MediaType.APPLICATION_JSON).post(Entity.json(post), Response.class);
		System.out.println("Status: "+res.getStatus());
		System.out.println("Respuesta: "+res);
		
		System.out.println("El usuario 2 escribe un post");
		System.out.print("Introduzca contenido: \n> ");
		content = buffer.readLine();
		post = new PostResource(content);
		res = this.target.path("user").path("2").path("post").request().
				accept(MediaType.APPLICATION_JSON).post(Entity.json(post), Response.class);
		System.out.println("Status: "+res.getStatus());
		System.out.println("Respuesta: "+res);
		
	}
	
	private void editarUsuario() throws IOException {
		System.out.println("Editar usuario 2:");
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
		System.out.println("Status: "+res.getStatus());
		System.out.println("Respuesta: "+res);
		
	}
	private void eliminarUsuario() {
		System.out.println("Eliminar usuario 1");
		System.out.println(this.target.path("user").path("1").request().accept(MediaType.APPLICATION_JSON).delete(String.class));
	}
	private void crearUsuario() throws SQLException, IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Crear usuario 1:");
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
		System.out.println("Status: "+res.getStatus());
		System.out.println("Respuesta: "+res);
		
		System.out.println("Crear usuario 2:");
		System.out.print("Introduzca username: \n> ");
		username = buffer.readLine();
		System.out.print("Introduzca name: \n> ");
		name = buffer.readLine();
		System.out.print("Introduzca email: \n> ");
		email = buffer.readLine();
		System.out.print("Introduzca biography: \n> ");
		bio = buffer.readLine();
		user= new UserResource(username, name, email, bio);
		res = this.target.path("user").request().accept(MediaType.APPLICATION_JSON).post(Entity.json(user), Response.class);
		System.out.println("Status: "+res.getStatus());
		System.out.println("Respuesta: "+res);
		
		System.out.println("Crear usuario 3:");
		System.out.print("Introduzca username: \n> ");
		username = buffer.readLine();
		System.out.print("Introduzca name: \n> ");
		name = buffer.readLine();
		System.out.print("Introduzca email: \n> ");
		email = buffer.readLine();
		System.out.print("Introduzca biography: \n> ");
		bio = buffer.readLine();
		user= new UserResource(username, name, email, bio);
		res = this.target.path("user").request().accept(MediaType.APPLICATION_JSON).post(Entity.json(user), Response.class);
		System.out.println("Status: "+res.getStatus());
		System.out.println("Respuesta: "+res);
		
		System.out.println("Crear usuario 4:");
		System.out.print("Introduzca username: \n> ");
		username = buffer.readLine();
		System.out.print("Introduzca name: \n> ");
		name = buffer.readLine();
		System.out.print("Introduzca email: \n> ");
		email = buffer.readLine();
		System.out.print("Introduzca biography: \n> ");
		bio = buffer.readLine();
		user= new UserResource(username, name, email, bio);
		res = this.target.path("user").request().accept(MediaType.APPLICATION_JSON).post(Entity.json(user), Response.class);
		System.out.println("Status: "+res.getStatus());
		System.out.println("Respuesta: "+res);
	
		
	}
	private static URI getBaseURI() {
		return UriBuilder.fromUri(HOST).build();
	}

}