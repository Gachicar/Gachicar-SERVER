package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.domain.User;
import lombok.Data;

/**
 * 음성인식 서버 --> 현재 서버 로 보내는 차량에게 내리는 명령 메시지
 */
@Data
public class CarControlMessageDto {
    private Long carId;
    private User userId;
    private CarAction carAction;
    private String dest;
}
