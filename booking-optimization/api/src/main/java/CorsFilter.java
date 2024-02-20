// package gr.amplifier.optimization.booking;

import java.io.IOException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 *
 * @author amplifier
 */
@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static boolean isPreflightRequest(ContainerRequestContext request) {
        return request.getHeaderString("Origin") != null && request.getMethod().equalsIgnoreCase("OPTIONS");
    }
    
    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        if (isPreflightRequest(request)) {
            request.abortWith(Response.ok().build());
            return;
        }
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        if (request.getHeaderString("Origin") == null) {
            return;
        }
        if (isPreflightRequest(request)) {
            response.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, OPTIONS");
            response.getHeaders().add("Access-Control-Allow-Headers",
                "Content-Type");
        }
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
    }
}