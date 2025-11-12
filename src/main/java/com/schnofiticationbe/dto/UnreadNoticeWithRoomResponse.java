package com.schnofiticationbe.dto;

import com.schnofiticationbe.dto.InternalNoticeDto.InternalNoticeListResponse;
import com.schnofiticationbe.dto.KakaoRoomInfoDto.KakaoRoomInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(requiredProperties = {"notice", "kakaoRooms"})
public class UnreadNoticeWithRoomResponse {
    private InternalNoticeListResponse notice;
    private List<KakaoRoomInfoResponse> kakaoRooms;
}
