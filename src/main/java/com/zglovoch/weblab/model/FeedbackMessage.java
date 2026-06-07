package com.zglovoch.weblab.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя обязательно")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Введите корректный email")
    @Column(nullable = false)
    private String email;

    private String phone;

    @NotBlank(message = "Сообщение обязательно")
    @Size(min = 10, max = 2000, message = "Сообщение должно быть от 10 до 2000 символов")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private boolean read = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
