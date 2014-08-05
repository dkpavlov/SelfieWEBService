package com.mkyong.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created with IntelliJ IDEA.
 * User: dkpavlov
 * Date: 6/16/14
 * Time: 18:43
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Selfie extends BaseEntity {

    @Column
    private String pictureName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    private String hashedEmail;

    @Column
    private Integer score = 0;

    @Column
    private Integer commentCount = 0;

    @Column
    private Integer favoritCount = 0;

    public Integer getCommentCount() {
        return commentCount;
    }

    public String getHashedEmail() {
        return hashedEmail;
    }

    public void setHashedEmail(String hashedEmail) {
        this.hashedEmail = hashedEmail;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getFavoritCount() {
        return favoritCount;
    }

    public void setFavoritCount(Integer favoritCount) {
        this.favoritCount = favoritCount;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }
}
