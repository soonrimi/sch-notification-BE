package com.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("INTERNAL")
public class InternalAttachment extends Attachment{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "internal_notice_id")
    private InternalNotice internalNotice;

    public InternalAttachment(String fileName, String fileUrl) {
        super(fileName, fileUrl);
    }
}
