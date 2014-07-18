package com.mkyong.rest;

import com.mkyong.model.Gender;
import com.mkyong.model.Selfie;
import com.mkyong.model.Type;
import com.mkyong.model.Vote;
import com.mkyong.repository.SelfieRepository;
import com.mkyong.repository.VoteRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dpavlov
 * Date: 14-7-10
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
@Component
@Path("/voteUp")
public class VoteUp {

    @Autowired
    SelfieRepository selfieRepository;

    @Autowired
    VoteRepository voteRepository;

    @POST
    @Path("/vote")
    public Response voteUpImage(@FormParam("id") String id,
                                @FormParam("email") String email){

        List<Vote> voteList = voteRepository.findByPictureIdAndEmail(id, email);
        if(!voteList.isEmpty()){
            Response.ResponseBuilder response = Response.status(403);
            return response.build();
        }
        Vote vote = new Vote();
        vote.setPictureId(id);
        vote.setEmail(email);
        voteRepository.save(vote);

        Selfie selfie = selfieRepository.findOne(Long.valueOf(id));
        selfie.setScore(selfie.getScore() + 1);
        selfieRepository.save(selfie);
        Response.ResponseBuilder response = Response.ok();
        return response.build();
    }


}
