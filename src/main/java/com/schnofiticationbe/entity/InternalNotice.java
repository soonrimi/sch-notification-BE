package com.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Schema(requiredProperties = {"writer_id", "targetYear", "targetDept", "sentToKakao", "InternalAttachment"})
@DiscriminatorValue("INTERNAL")
public class InternalNotice extends Notice {


    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private Admin writer; // 실제 작성자

    @Column(nullable = false)
    private TargetYear targetYear;

    @ManyToMany()
    @JoinTable(
            name = "internal_notice_target_dept",
            joinColumns = @JoinColumn(name = "internal_notice_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private Set<Department> targetDept;

    @Column(nullable = false, name = "sent_to_kakao")
    private Boolean sentToKakao = false;

    @OneToMany(mappedBy = "internalNotice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InternalAttachment> InternalAttachment = new ArrayList<>();

    public void addAttachment(InternalAttachment attachment) {
        this.InternalAttachment.add(attachment);
        attachment.setInternalNotice(this);
    }
}
