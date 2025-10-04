package com.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "keyword_notification")
public class KeywordNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ElementCollection
    @CollectionTable(name = "keyword_includes", joinColumns = @JoinColumn(name = "keyword_notification_id"))
    @Column(name = "include_keyword", nullable = false)
    private List<String> includeKeywords = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "keyword_excludes", joinColumns = @JoinColumn(name = "keyword_notification_id"))
    @Column(name = "exclude_keyword", nullable = false)
    private List<String> excludeKeywords = new ArrayList<>();

    @Column(nullable = true)
    private String device;

    @CreationTimestamp
    private LocalDateTime createdDate;
}
