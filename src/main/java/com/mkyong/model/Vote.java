package com.mkyong.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: dpavlov
 * Date: 14-7-14
 * Time: 9:46
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Vote extends BaseEntity {

    @Column
    private String pictureId;

    @Column
    private String email;

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
