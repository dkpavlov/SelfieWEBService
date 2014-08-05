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

    private static final String CURRENT_BASE = BASE_LINUX;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    SelfieRepository selfieRepository;

    private static Long LAST_PICTURE_ID = null;
    private static List<Long> maleSFWIds = null;
    private static List<Long> maleNSFWIds = null;
    private static List<Long> womanSFWIds = null;
    private static List<Long> womanNSFWIds = null;

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
                pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "id");
                selfiePage = selfieRepository
                        .findByGenderAndTypeAndIdLessThan(gender, type, Long.valueOf(lastImageId), pageRequest);
                break;
            case DOWN:
                pageRequest = new PageRequest(0, 1, Sort.Direction.ASC, "id");
                selfiePage = selfieRepository
                        .findByGenderAndTypeAndIdGreaterThan(gender, type, Long.valueOf(lastImageId), pageRequest);
                break;
        }

        if(!selfiePage.getContent().isEmpty()){
            selfie = selfiePage.getContent().get(0);
        } else {
            selfie = selfieRepository.findOne(Long.valueOf(lastImageId));
        }

        file = new File(CURRENT_BASE + selfie.getPictureName());
        Response.ResponseBuilder response = Response.ok(file);
        response.type(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        response.header("Content-Disposition", "attachment; filename=" + selfie.getPictureName());
        response.header("Picture-id", selfie.getId().toString());
        response.header("Picture-score", selfie.getScore().toString());
        response.header("Picture-comments", selfie.getCommentCount());
        response.header("Picture-favorite", selfie.getFavoritCount());
        return response.build();
    }

    @GET
    @Path("/random/{gender}/{type}/{oldId}")
    public Response getRandomImage(@PathParam("gender") Gender gender,
                                   @PathParam("type") Type type,
                                   @PathParam("oldId") String oldId){
        checkAndInitLists();
        List<Long> list = getListByGenderAndType(gender, type);
        Long randID = getRandomId(list);
        while (randID.equals(Long.valueOf(oldId))){
            randID = getRandomId(list);
        }
        Selfie selfie = selfieRepository.findOne(randID);
        File file = new File(CURRENT_BASE + selfie.getPictureName());
        Response.ResponseBuilder response = Response.ok(file);
        response.type(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        response.header("Content-Disposition", "attachment; filename=" + selfie.getPictureName());
        response.header("Picture-id", selfie.getId().toString());
        response.header("Picture-score", selfie.getScore().toString());
        response.header("Picture-comments", selfie.getCommentCount());
        response.header("Picture-favorite", selfie.getFavoritCount());
        return response.build();
    }

    @GET
    @Path("/new/order/{gender}/{type}")
    public Response getNewestPicture(@PathParam("gender") Gender gender,
                                     @PathParam("type") Type type){
        checkAndInitLists();
        PageRequest request = new PageRequest(0, 1, Sort.Direction.DESC, "id");
        Selfie selfie = selfieRepository.findAll(request).getContent().get(0);
        File file = new File(CURRENT_BASE + selfie.getPictureName());
        Response.ResponseBuilder response = Response.ok(file);
        response.type(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        response.header("Content-Disposition", "attachment; filename=" + selfie.getPictureName());
        response.header("Picture-id", selfie.getId().toString());
        response.header("Picture-score", selfie.getScore().toString());
        response.header("Picture-comments", selfie.getCommentCount());
        response.header("Picture-favorite", selfie.getFavoritCount());
        return response.build();
    }

    @GET
    @Path("/img/{id}")
    public Response getImg(@PathParam("id") String id) {
        Selfie selfie = selfieRepository.findOne(Long.valueOf(id));
        File file = new File(CURRENT_BASE + selfie.getPictureName());
        Response.ResponseBuilder response = Response.ok(file);
        response.type(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        response.header("Content-Disposition", "attachment; filename=" + selfie.getPictureName());
        response.header("Picture-id", id);
        response.header("Picture-score", selfie.getScore().toString());
        response.header("Picture-comments", selfie.getCommentCount());
        response.header("Picture-favorite", selfie.getFavoritCount());
        return response.build();
    }

    @GET
    @Path("/img/favorite/{id}")
    public Response getImgForFavorite(@PathParam("id") String id) {
        Selfie selfie = selfieRepository.findOne(Long.valueOf(id));
        selfie.setFavoritCount(selfie.getFavoritCount() + 1);
        selfieRepository.save(selfie);
        File file = new File(CURRENT_BASE + selfie.getPictureName());
        Response.ResponseBuilder response = Response.ok(file);
        response.type(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        response.header("Content-Disposition", "attachment; filename=" + selfie.getPictureName());
        response.header("Picture-id", id);
        return response.build();
    }

    @POST
    @Path("/favorite")
    public Response favoriteSelfie(@FormParam("id") String id){
        Selfie selfie = selfieRepository.findOne(Long.valueOf(id));
        selfie.setFavoritCount(selfie.getFavoritCount() + 1);
        selfieRepository.save(selfie);
        Response.ResponseBuilder response = Response.ok();
        return response.build();
    }

    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@FormParam("gender") Gender gender,
                               @FormParam("type") Type type,
                               @FormParam("base46jpg") String base46jpg,
                               @FormParam("hashedEmail") String hashedEmail){
        if(LAST_PICTURE_ID == null){
            initLastPicturePointer();
        }

        String pictureName = String.valueOf(LAST_PICTURE_ID) + ".jpg";

        byte[] b = Base64.decodeBase64(base46jpg);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(CURRENT_BASE + pictureName);
            fos.write(b);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Selfie selfie = new Selfie();
        selfie.setPictureName(pictureName);
        selfie.setGender(gender);
        selfie.setType(type);
        selfie.setHashedEmail(hashedEmail);
        Selfie selfie1 = selfieRepository.save(selfie);

        Response.ResponseBuilder response = Response.ok();
        response.header("Picture-id", selfie1.getId().toString());
        initAllLists();
        initLastPicturePointer();
        return response.build();
    }

    @GET
    @Path("/allSelfieIds/{hashedEmail}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Long> getAllSelfiesIds(@PathParam("hashedEmail") String hashedEmail){
        return selfieRepository.getAllSelfiesIdByHashedMail(hashedEmail);
    }

    @GET
    @Path("/delete/{id}")
    public Response deleteSelfie(@PathParam("id") String id){
        Selfie selfie = selfieRepository.findOne(Long.valueOf(id));
        selfieRepository.delete(selfie);
        Response.ResponseBuilder response = Response.ok();
        return response.build();
    }

    @GET
    @Path("/test")
    public Response testConnection(){
        Response.ResponseBuilder response = Response.ok();
        return response.build();
    }

   public List<Long> getListByGenderAndType(Gender gender, Type type){
       if(gender.equals(Gender.FEMALE)){
           if(type.equals(Type.NSFW)){
               return womanNSFWIds;
           } else {
               return womanSFWIds;
           }
       } else {
           if(type.equals(Type.NSFW)){
               return maleNSFWIds;
           } else {
               return maleSFWIds;
           }
       }
   }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static Long getRandomId(List<Long> idList){
        return idList.get(randInt(0, idList.size() - 1));
    }

    public void initLastPicturePointer(){
        PageRequest pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "id");
        Page<Selfie> selfiePage = selfieRepository.findAll(pageRequest);
        if(selfiePage.getContent().isEmpty()){         https://i.imgur.com/aF446I7.jpg
            LAST_PICTURE_ID = 0L;
        } else {
            LAST_PICTURE_ID = selfiePage.getContent().get(0).getId();
        }
    }

    public void initAllLists(){
        PageRequest pageRequest = new PageRequest(0, 1000, Sort.Direction.DESC, "id");
        maleSFWIds = selfieRepository.getAllSelfiesIdByGenderAndType(Gender.MALE, Type.SFW, pageRequest);
        maleNSFWIds = selfieRepository.getAllSelfiesIdByGenderAndType(Gender.MALE, Type.NSFW, pageRequest);
        womanSFWIds = selfieRepository.getAllSelfiesIdByGenderAndType(Gender.FEMALE, Type.SFW, pageRequest);
        womanNSFWIds = selfieRepository.getAllSelfiesIdByGenderAndType(Gender.FEMALE, Type.NSFW, pageRequest);
    }

    public void checkAndInitLists(){
        if(maleNSFWIds == null || maleNSFWIds == null || womanNSFWIds == null || womanSFWIds == null){
            initAllLists();
        }
    }

}
