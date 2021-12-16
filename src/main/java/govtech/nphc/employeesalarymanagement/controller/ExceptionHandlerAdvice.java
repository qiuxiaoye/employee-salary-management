package govtech.nphc.employeesalarymanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

//    @ExceptionHandler(EmployeeController.class)
//    public ResponseEntity handleException(ChekingCredentialsFailedException e) {
//        // log exception
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body("Error Message");
//    }
}