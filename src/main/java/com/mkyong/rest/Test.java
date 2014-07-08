package com.mkyong.rest;

import javax.ws.rs.core.Response;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dpavlov
 * Date: 14-6-23
 * Time: 10:31
 * To change this template use File | Settings | File Templates.
 */
@Component
@Path("/payment")
public class Test {



    @GET
    @Path("/mkyong")
    public Response savePayment() {

        return Response.status(200).entity("test").build();

    }


}
