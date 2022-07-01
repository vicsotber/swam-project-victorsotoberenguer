package it.unifi.swam.assignment_restful_architecture.Rest;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityRequestFilter implements ContainerRequestFilter{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if(authorizationHeader == null || authorizationHeader.isEmpty()) {
			if(requestContext.getUriInfo().getPath().contains("login")) {
				System.out.println("Performing login");
			}else if(requestContext.getUriInfo().getPath().contains("signup")) {
				System.out.println("Performing signup");
			}else {
				//Without and AUTHORIZATION Header, a client is considered UNAUTHORIZED
				requestContext.abortWith(
						Response.status(Response.Status.UNAUTHORIZED)
						.entity("This Request is UNAUTHORIZED!")
						.type("text/plain")
						.build()
				);
			}
			
		}else {
			String email;
			if(authorizationHeader.startsWith("Bearer") || authorizationHeader.startsWith("BEARER")) {
				String token = authorizationHeader.substring("Bearer".length()).trim();
				JWTService jwtService = new JWTService();
				Boolean verified = jwtService.verifyJWTToken(token);
				
				if(verified) {
					email = jwtService.getClaimFromToken(token, "email");
					String role = jwtService.getClaimFromToken(token, "role");
					System.out.println(role);
					System.out.println(role.equals("User"));
					System.out.println(role=="User");
					System.out.println(role.toLowerCase().trim().equals("User".toLowerCase().trim()));
					System.out.println(role.replace('"', ' ').trim().equals("User".replace('"', ' ').trim()));
					if(role.replace('"', ' ').trim().equals("User".replace('"', ' ').trim())) {
						System.out.println("test encuentra al usuario y lo mete en context");
						requestContext.setSecurityContext(new SecurityContextPlayer(email, requestContext));
					}else {
						requestContext.setSecurityContext(new SecurityContextWorker(email, requestContext));
					}
				}else {
					requestContext.abortWith(
							Response.status(Response.Status.UNAUTHORIZED)
							.entity("This Request is UNAUTHORIZED!")
							.type("text/plain")
							.build());
				}
			}else {
				requestContext.abortWith(
						Response.status(Response.Status.UNAUTHORIZED)
						.entity("This Request is UNAUTHORIZED!")
						.type("text/plain")
						.build());
			}
		}
	}

}
