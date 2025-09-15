package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.InternalNotice;
import lombok.Getter;
import lombok.Setter;

public class KakaoMessageDto {

    @Getter
    @Setter
    public static class KakaoMessageQueueResponse {
        private InternalNotice internalNotice;

        public KakaoMessageQueueResponse(InternalNotice internalNotice) {
            this.internalNotice = internalNotice;
        }
    }
}


