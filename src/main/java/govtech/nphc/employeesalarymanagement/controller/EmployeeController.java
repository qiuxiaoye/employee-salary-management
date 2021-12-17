package govtech.nphc.employeesalarymanagement.controller;

import govtech.nphc.employeesalarymanagement.model.Employee;
import govtech.nphc.employeesalarymanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    //    Get
    @GetMapping("/users")
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Boolean sortByNameAsec,
            @RequestParam(required = false) Date filterDateBefore,
            @RequestParam(required = false) Date filterDateAfter) {

        try {

            // minSalary
            if (minSalary != null && maxSalary != null) {
                List<Employee> betweenSalaryEmployees = employeeRepository.findAll().stream()
                        .filter(e -> e.getSalary().compareTo(minSalary) == 1
                                || e.getSalary().compareTo(minSalary) == 0)
                        .filter(e -> e.getSalary().compareTo(maxSalary) == -1)
                        .sorted(Comparator.comparingLong(Employee::getId))
                        .collect(Collectors.toList());
                if (betweenSalaryEmployees.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(betweenSalaryEmployees, HttpStatus.OK);
                }
            }


            // minSalary
            if (minSalary != null) {
                List<Employee> minSalaryEmployees = employeeRepository.findAll().stream()
                        .filter(e -> e.getSalary().compareTo(minSalary) == 1
                                || e.getSalary().compareTo(minSalary) == 0).collect(Collectors.toList());
                if (minSalaryEmployees.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(minSalaryEmployees, HttpStatus.OK);
                }
            }

            // maxSalary
            if (maxSalary != null) {
                List<Employee> maxSalaryEmployees = employeeRepository.findAll().stream()
                        .filter(e -> e.getSalary().compareTo(maxSalary) == -1).collect(Collectors.toList());
                if (maxSalaryEmployees.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(maxSalaryEmployees, HttpStatus.OK);
                }
            }

            // offset
            if (offset != null) {
                List<Employee> emps = employeeRepository.findAll();
                List<Employee> offsetEmployees = new ArrayList<>();

                if (emps.size() < offset) {
                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                for (int i = offset; i < emps.size(); i++) {
                    offsetEmployees.add(emps.get(i));
                }
                return new ResponseEntity<>(offsetEmployees, HttpStatus.OK);
            }

            if (limit != null) {
                List<Employee> emps = employeeRepository.findAll();
                List<Employee> limitEmployees = new ArrayList<>();

                if (emps.size() <= limit) {
                    return new ResponseEntity<>(emps, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                for (int i = 0; i < limit; i++) {
                    limitEmployees.add(emps.get(i));
                }
                return new ResponseEntity<>(limitEmployees, HttpStatus.OK);
            }

            //filtering (date before)
            if (filterDateBefore != null) {
                List<Employee> dateBeforeEmployees = employeeRepository.findAll().stream()
                        .filter(e -> e.getStartDate().before(filterDateBefore)).collect(Collectors.toList());
                if (dateBeforeEmployees.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(dateBeforeEmployees, HttpStatus.OK);
                }
            }

            //filtering (define your filters)
            if (filterDateAfter != null) {
                List<Employee> dateAfterEmployees = employeeRepository.findAll().stream()
                        .filter(e -> e.getStartDate().after(filterDateAfter)).collect(Collectors.toList());
                if (dateAfterEmployees.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(dateAfterEmployees, HttpStatus.OK);
                }
            }

//             sort by employee name
            if (sortByNameAsec != null) {
                List<Employee> sortEmployeeByName = employeeRepository.findAll().stream()
                        .sorted(Comparator.comparing(Employee::getName)).collect(Collectors.toList());
                if (sortEmployeeByName.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                else if (sortByNameAsec == true) {
                    return new ResponseEntity<>(sortEmployeeByName, HttpStatus.OK);
                }
            }

            List<Employee> employees = employeeRepository.findAll();

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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        try {

//            Employee employee1 = employeeRepository.getById(employee.getId());

//            if (getEmployeeById(employee.getId()) != null) {
//                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//            }



//            if (employeeRepository.getById(employee.getId())) {
//                return new ResponseEntity<>(postedEmployee, HttpStatus.CREATED);
//            }


//            ● {"message": "Successfully created"}
            //● {"message": "Employee ID already exists"}
            //● {"message": "Employee login not unique"}
            //● {"message": "Invalid salary"}
            //● {"message": "Invalid date"}



//            if (employee1 == null) {
//                return new ResponseEntity<>(employee, HttpStatus.CREATED);
//            }


            Employee postedEmployee = employeeRepository
                    .save(new Employee(
                            employee.getId(),
                            employee.getName(),
                            employee.getLogin(),
                            employee.getSalary(),
                            employee.getStartDate()));
            return new ResponseEntity<>(postedEmployee, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    Update
    @PutMapping("/users/{id}")
    public ResponseEntity<Employee> updateTutorial(@PathVariable("id") long id, @RequestBody Employee employee) {
        Optional<Employee> employeeData = employeeRepository.findById(id);

        if (employeeData.isPresent()) {
            Employee emp = employeeData.get();
            emp.setLogin(employee.getLogin());
            emp.setName(employee.getName());
            emp.setSalary(employee.getSalary());
            emp.setStartDate(employee.getStartDate());
            return new ResponseEntity<>(employeeRepository.save(emp), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //    Delete
    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable("id") long id) {
        try {
            employeeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
