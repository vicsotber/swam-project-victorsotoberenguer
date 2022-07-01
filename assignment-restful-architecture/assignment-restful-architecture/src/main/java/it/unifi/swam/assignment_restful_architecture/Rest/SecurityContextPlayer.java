package it.unifi.swam.assignment_restful_architecture.Rest;

import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

public class SecurityContextPlayer implements javax.ws.rs.core.SecurityContext{
	static final String DB_URL = "jdbc:mysql://localhost:3306/assignment-restful-architecture?serverTimezone=UTC";
	static final String USER = "java-client";
	static final String PASS = "password";
	
	private String principalUsername;
	private String principalEmail;
	private ContainerRequestContext requestContext;
	
	public SecurityContextPlayer(String principalUsername, String principalEmail, ContainerRequestContext requestContext) {
		this.principalUsername = principalUsername;
		this.principalEmail = principalEmail;
		this.requestContext = requestContext;
	}
	
	public SecurityContextPlayer(String principalEmail, ContainerRequestContext requestContext) {
		this.principalEmail = principalEmail;
		this.requestContext = requestContext;
	}
	
	@Override
	public boolean isUserInRole(String role) {
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			    Statement stmt = conn.createStatement();
			 ) {	
				System.out.println(principalEmail);
				String sql = "SELECT * FROM Playeraccount WHERE email=" + principalEmail;
				ResultSet rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					String userRole = "User";
					System.out.println(userRole);
					System.out.println(role);
					if(!userRole.equals(role)) {
						return false;
					}else if(userRole.equals(role)) {
						return true;
					}
				}
				
			System.out.println("test");
			return false;
			  
			} catch (SQLException e) {
			    e.printStackTrace();
			    return false;
			} 
	}
	
	@Override
	public boolean isSecure() {
		return requestContext.getSecurityContext().isSecure();
	}
	
	@Override
	public Principal getUserPrincipal() {
		return new Principal() {
			@Override
			public String getName() {
				return principalUsername;
			}
			
			public String getEmail() {
				return principalEmail;
			}
		};
	}
	
	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.CLIENT_CERT_AUTH;
	}
}
