package database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.naming.NamingContext;

public class Conexion {
	@Context
	protected UriInfo uriInfo;

	protected DataSource ds;
	protected Connection conn;
	
	// TODO In views, add try catch to return INTERNAL_ERROR_SERVER in case of error without exception
	// for example: Tables are not created in server and user make a request, it returns an error from Tomcat
	
	public Conexion() {
		try {
			InitialContext ctx = new InitialContext();
			NamingContext envCtx = (NamingContext) ctx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/faceSOS");
			conn = ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}