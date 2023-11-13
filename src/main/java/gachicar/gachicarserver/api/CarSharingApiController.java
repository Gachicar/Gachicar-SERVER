package gachicar.gachicarserver.api;

import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.*;
import gachicar.gachicarserver.exception.HttpStatusCode;
import gachicar.gachicarserver.rccar.dto.RCCarConnectRequestDto;
import gachicar.gachicarserver.service.CarSharingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.messaging.simp.stomp.StompHeaders.SESSION;

/**
 * 카셰어링 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CarSharingApiController {

    private final CarSharingService carSharingService;

    @MessageMapping("/hello")   // '/app/hello' 경로로 받음.
    @SendTo("/topic/greetings") // '/topic/greetings' 경로로 보냄. (이 경로를 구독하는 사용자들에게 전달)
    public Greeting greeting(HelloMessage message, SimpMessageHeaderAccessor headerAccessor) {
        // client 식별
        String sessionId = headerAccessor.getSessionId();
        log.info("Received message from session: " + sessionId);
        log.info("message: {}", message);

        return new Greeting("Hello, " + message.getName() + "!");
    }

    @MessageMapping("/connect/{carId}")  // 실질 경로: pub/connect/{carId}
    @SendTo("/sub/carSharing/{carId}")
    public ResultDto getCarConnection(@DestinationVariable Long carId) {
        log.info("getCarConnection 수행, 아이디 = {}", carId);
        if (carSharingService.connectCar(carId, new User("yeonsu", "d1234@gmail.com"))) {
            return ResultDto.of(HttpStatusCode.OK, " 공유차량 연결 성공", null);
        } else {
            return ResultDto.of(HttpStatusCode.BAD_REQUEST, "공유차량 연결 실패", null);
        }
    }

    /**
     * 현재 공유차량을 사용 중인 사용자를 가져오는 메서드
     */
    @MessageMapping("nowUser/{carId}")
    @SendTo("/sub/carSharing/{carId}")
    public String getNowUser(@DestinationVariable Long carId, CarRequestDto carRequestDto) {
//        User user = messageHeaderAccessor.getSessionAttributes().get(SESSION).get(USER_SESSION_KEY);
        String content = carRequestDto.getContent();
        Long nowUser = carSharingService.getNowUser(carId);
        log.info("nowUser id = {}, content = {}", nowUser, content);
        return nowUser.toString() + content;
    }

    /**
     * 공유차량 제어
     */
    @MessageMapping("/control")
    @SendToUser("/queue/responses")
    public CarMessageDto controlCar(CarControlMessageDto carControlMessageDto) {
        // RC카를 제어하는 로직 구현
        // controlMessage에 RC카 제어 정보가 포함됨.
        return carSharingService.controlCar(carControlMessageDto);
    }

}

