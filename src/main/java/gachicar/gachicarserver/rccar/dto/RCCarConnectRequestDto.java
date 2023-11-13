package gachicar.gachicarserver.rccar.dto;

import gachicar.gachicarserver.domain.Car;
import lombok.Data;

@Data
public class RCCarConnectRequestDto {
    private Long userId;
    private Long carId;
}
