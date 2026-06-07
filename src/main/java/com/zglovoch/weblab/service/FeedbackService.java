package com.zglovoch.weblab.service;

import com.zglovoch.weblab.model.FeedbackMessage;
import com.zglovoch.weblab.repository.FeedbackMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackMessageRepository repository;

    public List<FeedbackMessage> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<FeedbackMessage> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public FeedbackMessage save(FeedbackMessage message) {
        return repository.save(message);
    }

    @Transactional
    public void markAsRead(Long id) {
        repository.findById(id).ifPresent(msg -> {
            msg.setRead(true);
            repository.save(msg);
        });
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public long countUnread() {
        return repository.countByReadFalse();
    }
}
