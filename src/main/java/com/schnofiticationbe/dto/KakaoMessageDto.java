package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.InternalNotice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class KakaoMessageDto {

    @Getter
    @Setter
    @Schema(requiredProperties = {"internalNotice"})
    public static class KakaoMessageQueueResponse {
        private InternalNotice internalNotice;

        public KakaoMessageQueueResponse(InternalNotice internalNotice) {
            this.internalNotice = internalNotice;
        }
    }
}


