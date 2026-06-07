package com.zglovoch.weblab.service;

import com.zglovoch.weblab.model.FeedbackMessage;
import com.zglovoch.weblab.repository.FeedbackMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для FeedbackService.
 */
@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackMessageRepository repository;

    @InjectMocks
    private FeedbackService feedbackService;

    private FeedbackMessage msg1;
    private FeedbackMessage msg2;

    @BeforeEach
    void setUp() {
        msg1 = new FeedbackMessage();
        msg1.setId(1L);
        msg1.setName("Иван Петров");
        msg1.setEmail("ivan@example.com");
        msg1.setMessage("Очень уютное место, обязательно вернусь!");
        msg1.setRead(false);
        msg1.setCreatedAt(LocalDateTime.now().minusHours(1));

        msg2 = new FeedbackMessage();
        msg2.setId(2L);
        msg2.setName("Мария Сидорова");
        msg2.setEmail("maria@example.com");
        msg2.setMessage("Капучино просто великолепный!");
        msg2.setRead(true);
        msg2.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("findAll() возвращает сообщения в порядке убывания даты")
    void findAll_returnsMostRecentFirst() {
        when(repository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(msg2, msg1));

        List<FeedbackMessage> result = feedbackService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Мария Сидорова");
    }

    @Test
    @DisplayName("save() сохраняет новое сообщение")
    void save_persistsMessage() {
        when(repository.save(any(FeedbackMessage.class))).thenReturn(msg1);

        FeedbackMessage saved = feedbackService.save(msg1);

        assertThat(saved.getEmail()).isEqualTo("ivan@example.com");
        assertThat(saved.isRead()).isFalse();
        verify(repository, times(1)).save(msg1);
    }

    @Test
    @DisplayName("countUnread() возвращает число непрочитанных")
    void countUnread_returnsCorrectCount() {
        when(repository.countByReadFalse()).thenReturn(3L);

        long count = feedbackService.countUnread();

        assertThat(count).isEqualTo(3L);
    }

    @Test
    @DisplayName("markAsRead() меняет флаг read на true и сохраняет")
    void markAsRead_setsReadFlagAndSaves() {
        when(repository.findById(1L)).thenReturn(Optional.of(msg1));
        when(repository.save(any(FeedbackMessage.class))).thenReturn(msg1);

        feedbackService.markAsRead(1L);

        // Перехватываем объект, который передали в save()
        ArgumentCaptor<FeedbackMessage> captor =
                ArgumentCaptor.forClass(FeedbackMessage.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().isRead()).isTrue();
    }

    @Test
    @DisplayName("markAsRead() не вызывает save() для несуществующего ID")
    void markAsRead_doesNothingForUnknownId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        feedbackService.markAsRead(99L);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("deleteById() вызывает repository.deleteById()")
    void deleteById_callsRepository() {
        doNothing().when(repository).deleteById(1L);

        feedbackService.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("findById() возвращает Optional с сообщением")
    void findById_returnsMessage() {
        when(repository.findById(2L)).thenReturn(Optional.of(msg2));

        Optional<FeedbackMessage> result = feedbackService.findById(2L);

        assertThat(result).isPresent();
        assertThat(result.get().isRead()).isTrue();
    }
}
