package br.com.unifor.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Provider
public class InvalidPayloadExceptionMapper implements ExceptionMapper<InvalidPayloadException> {
    @Override
    public Response toResponse(InvalidPayloadException exception) {
        JsonObject json = Json.createObjectBuilder()
            .add("error", exception.getMessage())
            .build();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(json)
                .build();
    }
}
