package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.CarAction;
import gachicar.gachicarserver.dto.CarControlMessageDto;
import gachicar.gachicarserver.dto.CarMessageDto;
import gachicar.gachicarserver.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static gachicar.gachicarserver.dto.CarStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarSharingService {

    private final CarRepository carRepository;

    /* 현재 공유차량을 사용 중인 사용자를 가져오는 메서드 */
    public Long getNowUser(Long carId) {
        Car car = carRepository.findById(carId);
        return car.getNowUser();
    }

    /* 공유차량을 사용할 수 있는 경우 연결하는 메서드 */
    public Boolean connectCar(Long carId, User user) {
        Car car = carRepository.findById(carId);
        // 사용 가능한 상태일 경우(FALSE) lock
        if (!car.getCarStatus()) {
            lock(car, user);
        }
        return car.getCarStatus();
    }

    private void lock(Car car, User user) {
        car.setCarStatus(Boolean.TRUE);
        car.setNowUser(user.getId());
    }

    /* 공유차량을 다 사용한 후 unlock 하는 메서드 */
    private void unlock(Car car) {
        car.setCarStatus(Boolean.FALSE);
    }

    /**
     * RC카 명령 제어 메서드
     */
    public CarMessageDto controlCar(CarControlMessageDto carControlMessageDto) {
        CarAction carAction = carControlMessageDto.getCarAction();
        String dest = carControlMessageDto.getDest();

        switch (carAction) {
            case GO_HOME:
                log.info("집으로 가라는 명령 실행");
                // RC카 제어 코드 구현
                return CarMessageDto.builder()
                        .carStatus(GOOD)
                        .dest(dest)
                        .build();
            case COME_HERE:
                log.info("여기로 오라는 명령 실행");
                // RC카 제어 코드 구현
                return CarMessageDto.builder()
                        .carStatus(GOOD)
                        .dest(dest)
                        .build();
        }
        return null;
    }

}
