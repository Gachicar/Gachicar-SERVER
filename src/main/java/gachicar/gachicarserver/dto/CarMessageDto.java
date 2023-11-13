package gachicar.gachicarserver.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 서버 --> 공유차량을 구독하는 사용자들에게 보내는 차량 정보 DTO
 */
@Data
public class CarMessageDto {

    public enum MessageType {
        ENTER, CONNECT, EXIT, DRIVE
    }

    private MessageType messageType;    // 메시지 타입
    private CarStatus carStatus;    // 공유차량 상태
    private Long carId;     // 공유차량 id
    private String dest; // 목적지

    @Builder
    public CarMessageDto(CarStatus carStatus, String dest) {
        this.carStatus = carStatus;
        this.dest = dest;
    }
}
