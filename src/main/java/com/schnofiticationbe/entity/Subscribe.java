package com.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "subscribe")
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //아이디


    @Column
    private String category;


    @Column(nullable = false)
    private String device;


    @ElementCollection
    @CollectionTable(name = "subscribe_include_keywords", joinColumns = @JoinColumn(name = "subscribe_id"))
    @Column(name = "keyword")
    private List<String> includeKeywords = new ArrayList<>();


    @ElementCollection
    @CollectionTable(name = "subscribe_exclude_keywords", joinColumns = @JoinColumn(name = "subscribe_id"))
    @Column(name = "keyword")
    private List<String> excludeKeywords = new ArrayList<>();


    @Column
    private String department;


    @Column
    private Integer year;

    @CreationTimestamp
    private LocalDateTime createdDate;
}
