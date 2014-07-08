package com.mkyong.repository;

import com.mkyong.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created with IntelliJ IDEA.
 * User: dkpavlov
 * Date: 6/16/14
 * Time: 20:55
 * To change this template use File | Settings | File Templates.
 */
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    public Page<Comment> findBySelfieId(Long id, Pageable pageable);
}
