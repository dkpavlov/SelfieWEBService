package com.mkyong.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dkpavlov
 * Date: 6/16/14
 * Time: 20:51
 * To change this template use File | Settings | File Templates.
 */

@NamedNativeQueries({
        @NamedNativeQuery(
                name="Comment.getCommentCount",
                query="SELECT count(c) FROM comment c " +
                        "WHERE c.selfie_id = ?1"
        )
})
@Entity
public class Comment extends BaseEntity {

    @Column
    private String text;

    @Column
    private String user;

    @ManyToOne
    private Selfie selfie;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonIgnore
    public Selfie getSelfie() {
        return selfie;
    }

    public void setSelfie(Selfie selfie) {
        this.selfie = selfie;
    }
}
