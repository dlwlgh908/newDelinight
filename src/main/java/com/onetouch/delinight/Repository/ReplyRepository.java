package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity , Long> {

    //질문 id가 특정 값인 뎃글들을 최신순으로 가져온다.
    public List<ReplyEntity> findByInquireEntity_Id(Long id);

}
