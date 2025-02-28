package org.example.carrental.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 에러 반환
public class CarAlreadyRentedException extends RuntimeException {
  public CarAlreadyRentedException(Long carId) {
    super("렌탈 중인 차량입니다. (ID: " + carId + ")");
  }
}
