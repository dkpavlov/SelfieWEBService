package com.mkyong.rest;

import com.mkyong.model.Gender;
import com.mkyong.model.Type;
import com.mkyong.repository.SelfieRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: dpavlov
 * Date: 14-7-10
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
@Component
@Path("/imgg")
public class PostImage {

    SelfieRepository selfieRepository;


}
