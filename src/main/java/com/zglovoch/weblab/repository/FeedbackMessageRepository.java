package com.zglovoch.weblab.repository;

import com.zglovoch.weblab.model.FeedbackMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackMessageRepository extends JpaRepository<FeedbackMessage, Long> {
    List<FeedbackMessage> findAllByOrderByCreatedAtDesc();
    long countByReadFalse();
}
