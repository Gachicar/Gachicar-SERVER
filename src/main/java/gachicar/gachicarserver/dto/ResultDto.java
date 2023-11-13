package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.exception.HttpStatusCode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class ResultDto<D> {
    private final int code;
    private final String message;
    private final D data;

    public static <D> ResultDto<D> of(HttpStatusCode httpStatusCode, String message, D data) {
        return new ResultDto<>(httpStatusCode.getCode(), message, data);
    }
}
