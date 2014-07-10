package com.mkyong.rest;

import com.mkyong.model.*;
import com.mkyong.repository.CommentRepository;
import com.mkyong.repository.SelfieRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: dkpavlov
 * Date: 6/14/14
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
@Component
@Path("/img")
public class GetImage {

    private static final String BASE_PATH_WINDOWS = "D:\\selfies\\";
    private static final String BASE_PATH_OSX = "/Users/dkpavlov/Desktop/Android/selfies/";
    private static final String BASE_LINUX = "/usr/share/glassfish3_old/selfies/";

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    SelfieRepository selfieRepository;

    public static Long LAST_PICTURE_ID = null;

    @GET
    @Path("/list/{gender}/{type}")
    @Produces("application/json")
    public List<Long> ge(@PathParam("gender") Gender gender,
                         @PathParam("type") Type type){
        LastKnownImage image = new LastKnownImage();
        List<Long> l = selfieRepository.getAllSelfiesIdByGenderAndType(gender, type);
        image.setList(l);
        return l;
    }

    @GET
    @Path("/order/{gender}/{type}/{direction}/{last}")
    public Response getOrderImage(@PathParam("gender") Gender gender,
                                  @PathParam("type") Type type,
                                  @PathParam("direction")Direction direction,
                                  @PathParam("last") String lastImageId){
        Selfie selfie = null;
        PageRequest pageRequest = null;
        Page<Selfie> selfiePage = null;
        File file = null;
        switch (direction){
            case UP:
                pageRequest = new PageRequest(0, 1, Sort.Direction.ASC, "id");
                selfiePage = selfieRepository
                        .findByGenderAndTypeAndIdGreaterThan(gender, type, Long.valueOf(lastImageId), pageRequest);
                break;
            case DOWN:
                pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "id");
                selfiePage = selfieRepository
                        .findByGenderAndTypeAndIdLessThan(gender, type, Long.valueOf(lastImageId), pageRequest);
                break;
        }

        if(!selfiePage.getContent().isEmpty()){
            selfie = selfiePage.getContent().get(0);
        } else {
            selfie = selfieRepository.findOne(Long.valueOf(lastImageId));
        }

        file = new File(BASE_PATH_WINDOWS + selfie.getPictureName());
        Response.ResponseBuilder response = Response.ok(file);
        response.type(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        response.header("Content-Disposition", "attachment; filename=" + selfie.getPictureName());
        response.header("Picture-id", selfie.getId().toString());

        return response.build();
    }

    @GET
    @Path("/random/{gender}/{type}/{oldId}")
    public Response getRandomImage(@PathParam("gender") Gender gender,
                                   @PathParam("type") Type type,
                                   @PathParam("oldId") String oldId){
        List<Long> l = selfieRepository.getAllSelfiesIdByGenderAndTypeAndIdNot(gender, type, Long.valueOf(oldId));
        Long randomId = l.get(randInt(0, l.size() - 1));

        Selfie selfie = selfieRepository.findOne(randomId);
        File file = new File(BASE_PATH_WINDOWS + selfie.getPictureName());
        Response.ResponseBuilder response = Response.ok(file);
        response.type(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        response.header("Content-Disposition", "attachment; filename=" + selfie.getPictureName());
        response.header("Picture-id", selfie.getId().toString());

        return response.build();
    }

    @GET
    @Path("/new/order/{gender}/{type}")
    public Response getNewestPicture(@PathParam("gender") Gender gender,
                                     @PathParam("type") Type type){
        PageRequest pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "id");
        Page<Selfie> selfiePage = selfieRepository.findAll(pageRequest);
        Selfie selfie = selfiePage.getContent().get(0);
        File file = new File(BASE_PATH_WINDOWS + selfie.getPictureName());
        Response.ResponseBuilder response = Response.ok(file);
        response.type(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        response.header("Content-Disposition", "attachment; filename=" + selfie.getPictureName());
        response.header("Picture-id", selfie.getId().toString());
        return response.build();
    }

    @GET
    @Path("/img/{id}")
    public Response getImg(@PathParam("id") String id) {
        Selfie selfie = selfieRepository.findOne(Long.valueOf(id));
        File file = new File(BASE_PATH_WINDOWS + selfie.getPictureName());
        Response.ResponseBuilder response = Response.ok(file);
        response.type(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        response.header("Content-Disposition", "attachment; filename=" + selfie.getPictureName());
        return response.build();
    }

    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@FormParam("gender") Gender gender,
                               @FormParam("type") Type type,
                               @FormParam("base46jpg") String base46jpg){
        if(LAST_PICTURE_ID == null){
            initLastPicturePointer();
        }

        String pictureName = String.valueOf(LAST_PICTURE_ID) + ".jpg";

        byte[] b = Base64.decodeBase64(base46jpg);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("D:\\selfies\\" + pictureName);
            fos.write(b);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Selfie selfie = new Selfie();
        selfie.setPictureName(pictureName);
        selfie.setGender(gender);
        selfie.setType(type);

        selfieRepository.save(selfie);

        Response.ResponseBuilder response = Response.ok();
        return response.build();
    }



    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public void initLastPicturePointer(){
        PageRequest pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "id");
        Page<Selfie> selfiePage = selfieRepository.findAll(pageRequest);
        if(!selfiePage.getContent().isEmpty()){
            LAST_PICTURE_ID = 0L;
        } else {
            LAST_PICTURE_ID = selfiePage.getContent().get(0).getId();
        }
    }

}
