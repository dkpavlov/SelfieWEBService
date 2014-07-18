package com.mkyong.repository;

import com.mkyong.model.Gender;
import com.mkyong.model.Selfie;
import com.mkyong.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dkpavlov
 * Date: 6/16/14
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 */
public interface SelfieRepository extends PagingAndSortingRepository<Selfie, Long> {
    public Page<Selfie> findByGenderAndIdLessThan(Gender gender, Long id, Pageable pageable);
    public Page<Selfie> findByGenderAndIdGreaterThan(Gender gender, Long id, Pageable pageable);
    public Page<Selfie> findByGenderAndTypeAndIdLessThan(Gender gender, Type type,  Long id, Pageable pageable);
    public Page<Selfie> findByGenderAndTypeAndIdGreaterThan(Gender gender, Type type, Long id, Pageable pageable);
    public Page<Selfie> findByGender(Gender gender, Pageable pageable);

    @Query(value = "select s.id from Selfie s where s.gender = ?1 and s.type = ?2 order by s.id DESC")
    public List<Long> getAllSelfiesIdByGenderAndType(Gender gender, Type type, Pageable pageable);

    @Query(value = "select s.id from Selfie s where s.gender = ?1 and s.type = ?2 and s.id <> ?3 order by s.id DESC")
    public List<Long> getAllSelfiesIdByGenderAndTypeAndIdNot(Gender gender, Type type, Long id);




}
