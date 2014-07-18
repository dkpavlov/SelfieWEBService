package com.mkyong.repository;

import com.mkyong.model.Selfie;
import com.mkyong.model.Vote;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dpavlov
 * Date: 14-7-14
 * Time: 9:49
 * To change this template use File | Settings | File Templates.
 */
public interface VoteRepository extends PagingAndSortingRepository<Vote, Long> {
    public List<Vote> findByPictureIdAndEmail(String pictureId, String email);
}
