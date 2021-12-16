package govtech.nphc.employeesalarymanagement.controller;

import govtech.nphc.employeesalarymanagement.model.Employee;
import govtech.nphc.employeesalarymanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    //    Get
    @GetMapping("/users")
    public ResponseEntity<List<Employee>> getAllTutorials(
            @RequestParam(required = false) BigDecimal minSalary, BigDecimal maxSalary, int offset, int limit) {
        try {
            List<Employee> employees = new ArrayList<Employee>();

//            employeeRepository.findByMinSalary(minSalary);
//            employeeRepository.findByMaxSalary(maxSalary);
//            employeeRepository.findByOffSet(offset);
//            employeeRepository.findByLimit(limit);

//            if (minSalary == null)
//
//                employeeRepository.findAll().forEach(employees::add);
//            else
//                employeeRepository.findByTitleContaining(title).forEach(employees::add);

            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id) {
        Optional<Employee> employeeData = employeeRepository.findById(id);

        if (employeeData.isPresent()) {
            return new ResponseEntity<>(employeeData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        try {
//            Employee postedEmployee = employeeRepository
//                    .save(new Employee(employee.getTitle(), tutorial.getDescription(), false));

//            if (employeeRepository.getById(employee.getId())) {
//                return new ResponseEntity<>(postedEmployee, HttpStatus.CREATED);
//            }


//            ● {"message": "Successfully created"}
                //● {"message": "Employee ID already exists"}
                //● {"message": "Employee login not unique"}
                //● {"message": "Invalid salary"}
                //● {"message": "Invalid date"}


            Employee employee1 = employeeRepository.getById(employee.getId());
//            if (employee1.getLogin().equals(employee.getLogin())) {
//                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//            if (employee1 == null) {
//                return new ResponseEntity<>(employee, HttpStatus.CREATED);
//            }


            return new ResponseEntity<>(employee, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    Create
//    Update
//    Delete
}
