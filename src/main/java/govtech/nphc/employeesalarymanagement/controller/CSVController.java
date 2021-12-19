package govtech.nphc.employeesalarymanagement.controller;

import govtech.nphc.employeesalarymanagement.helper.CSVHelper;
import govtech.nphc.employeesalarymanagement.message.ResponseMessage;
import govtech.nphc.employeesalarymanagement.model.Employee;
import govtech.nphc.employeesalarymanagement.service.CSVService;
import govtech.nphc.employeesalarymanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class CSVController {

    @Autowired
    CSVService fileService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/users/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                List<Employee> updateEmployees = fileService.save(file);
                if (updateEmployees == null) {
                    message = "Uploaded the file successfully: " + file.getOriginalFilename();
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(message));
                }
                if (employeeService.updateAll(updateEmployees) == 0) {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Success but no data updated."));
                }
                return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Data created or uploaded."));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + " : " + e.getMessage() + "!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

}