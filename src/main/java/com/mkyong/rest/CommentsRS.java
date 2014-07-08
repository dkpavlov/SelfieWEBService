package com.mkyong.rest;

import com.mkyong.model.Comment;
import com.mkyong.model.Selfie;
import com.mkyong.repository.CommentRepository;
import com.mkyong.repository.SelfieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dkpavlov
 * Date: 6/16/14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
@Component
@Path("/comment")
public class CommentsRS {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    SelfieRepository selfieRepository;

    @GET
    @Path("/get/{id}")
    @Produces("application/json")
    public List<Comment> getCommentsForSelfie(@PathParam("id") String id){
        try{
            PageRequest pageRequest = new PageRequest(0, 1000, Sort.Direction.DESC, "id");
            Page<Comment> commentsPage = commentRepository.findBySelfieId(Long.valueOf(id), pageRequest);
            List<Comment> list = commentsPage.getContent();
            return list;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @POST
    @Path("/add/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> addComment(@PathParam("id") String id,
                                    @FormParam("comment") String commentText,
                                    @FormParam("user") String user){

        Selfie selfie = selfieRepository.findOne(Long.valueOf(id));
        Comment comment = new Comment();
        comment.setText(commentText);
        comment.setUser(user);
        comment.setSelfie(selfie);
        commentRepository.save(comment);
        PageRequest pageRequest = new PageRequest(0, 1000, Sort.Direction.DESC, "id");
        Page<Comment> commentsPage = commentRepository.findBySelfieId(Long.valueOf(id), pageRequest);
        return commentsPage.getContent();
    }
}
