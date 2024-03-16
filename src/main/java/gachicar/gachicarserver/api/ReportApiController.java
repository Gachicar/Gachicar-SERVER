package gachicar.gachicarserver.api;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.ReportDto;
import gachicar.gachicarserver.dto.ResultDto;
import gachicar.gachicarserver.dto.UsageCountsDto;
import gachicar.gachicarserver.dto.UserDto;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.HttpStatusCode;
import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.DriveReportService;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 주행기록 관련 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportApiController {

    public final UserService userService;
    public final CarService carService;
    public final DriveReportService driveReportService;

    /**
     * 사용자의 가장 최근 주행 리포트 조회
     */
    @GetMapping
    public ResultDto<Object> getDriveReport() {
        try {
            // 주행기록 조회
            return ResultDto.of(HttpStatusCode.OK, "사용자의 주행 리포트 조회 성공", driveReportService.getRecentReport(1L));
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 그룹 내 최다 사용자 조회
     */
    @GetMapping("/most")
    public ResultDto<Object> getMostUserInGroup() {
        try {
            User user = userService.findUserById(1L);
            Car car = user.getGroup().getCar();
            UserDto mostUserInGroupReport = driveReportService.getMostUserInGroupReport(car.getId());

            return ResultDto.of(HttpStatusCode.OK, "그룹 내 최다 사용자 조회 성공", mostUserInGroupReport);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 그룹원별 공유차량 사용 횟수 조회
     */
    @GetMapping("/usage")
    public ResultDto<Object> getUsersUsageCounts() {
        try {
            User user = userService.findUserById(1L);
            Car car = user.getGroup().getCar();

            List<UsageCountsDto> userUsageCounts = driveReportService.getUserUsageCounts(car.getId());
            return ResultDto.of(HttpStatusCode.OK, "그룹원별 공유차량 사용 횟수 조회 성공", userUsageCounts);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 특정 사용자의 전체 주행기록 조회
     */
    @GetMapping("/{userId}")
    public ResultDto<Object> getUserReports(@PathVariable("userId") Long userId) {
        try {
            List<ReportDto> allReportsByUser = driveReportService.getAllReportsByUser(userId);
            return ResultDto.of(HttpStatusCode.OK, "사용자의 전체 주행기록 조회 성공", allReportsByUser);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

}
