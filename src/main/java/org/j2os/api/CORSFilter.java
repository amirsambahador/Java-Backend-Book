package org.j2os.api;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
/*
    CORSFilter for Access-Control-Allow-Origin
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request,
                       ContainerResponseContext response) {

        response.getHeaders().add(
                "Access-Control-Allow-Origin", "*");

        response.getHeaders().add(
                "Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");

        response.getHeaders().add(
                "Access-Control-Allow-Credentials", "true");

        response.getHeaders().add(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}
