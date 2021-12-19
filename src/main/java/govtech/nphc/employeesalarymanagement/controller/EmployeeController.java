package govtech.nphc.employeesalarymanagement.controller;

import govtech.nphc.employeesalarymanagement.message.ResponseMessage;
import govtech.nphc.employeesalarymanagement.model.Employee;
import govtech.nphc.employeesalarymanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static govtech.nphc.employeesalarymanagement.utils.Utils.isContainLogin;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    //Get

    /** getAllEmployees Fetch List of Employees
     *
     * @param minSalary Minimum salary, returns employee with more salary.
     * @param maxSalary Maximum salary, returns employee with less salary.
     * @param offset Starting offset of results to return. Default is 0 at start.
     * @param limit - Max number of results to return. Default is 0 no limit.
     * @param sortByNameAsec
     * @param filterDateBefore
     * @param filterDateAfter
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "0") Integer limit,
            @RequestParam(required = false) Boolean sortByNameAsec,
            @RequestParam(required = false) Date filterDateBefore,
            @RequestParam(required = false) Date filterDateAfter) {

        try {
            // MinSalary & maxSalary combined search.
            if (minSalary != null && maxSalary != null) {
                if (maxSalary.compareTo(BigDecimal.valueOf(0)) == -1) {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                List<Employee> betweenSalaryEmployees = employeeRepository.findAll().stream()
                        .filter(e -> e.getSalary().compareTo(minSalary) == 1
                                || e.getSalary().compareTo(minSalary) == 0)
                        .filter(e -> e.getSalary().compareTo(maxSalary) == -1)
                        .sorted(Comparator.comparingLong(Employee::getId))
                        .collect(Collectors.toList());
                if (betweenSalaryEmployees.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity<>(betweenSalaryEmployees, HttpStatus.OK);
                }
            }

            // minSalary search
            if (minSalary != null) {
                if (maxSalary.compareTo(BigDecimal.valueOf(0)) == -1) {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                List<Employee> minSalaryEmployees = employeeRepository.findAll().stream()
                        .filter(e -> e.getSalary().compareTo(minSalary) == 1
                                || e.getSalary().compareTo(minSalary) == 0).collect(Collectors.toList());
                if (minSalaryEmployees.isEmpty()) {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity<>(minSalaryEmployees, HttpStatus.OK);
                }
            }

            // maxSalary search
            if (maxSalary != null) {
                List<Employee> maxSalaryEmployees = employeeRepository.findAll().stream()
                        .filter(e -> e.getSalary().compareTo(maxSalary) == -1).collect(Collectors.toList());
                if (maxSalaryEmployees.isEmpty()) {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity<>(maxSalaryEmployees, HttpStatus.OK);
                }
            }

            // offset
            if (offset != 0) {
                if (offset < 0) {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                List<Employee> emps = employeeRepository.findAll();
                List<Employee> offsetEmployees = new ArrayList<>();

                if (emps.size() < offset) {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                for (int i = offset; i < emps.size(); i++) {
                    offsetEmployees.add(emps.get(i));
                }
                return new ResponseEntity<>(offsetEmployees, HttpStatus.OK);
            }

            // limit
            if (limit != 0) {
                if (limit < 0) {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                List<Employee> employees = employeeRepository.findAll();
                List<Employee> limitEmployees = new ArrayList<>();

                if (employees.size() > limit) {
                    for (int i = 0; i < limit; i++) {
                        limitEmployees.add(employees.get(i));
                    }
                    return new ResponseEntity<>(limitEmployees, HttpStatus.OK);
                }
                return new ResponseEntity<>(employees, HttpStatus.OK);
            }

            //filtering (date before)
//            if (filterDateBefore != null) {
//                List<Employee> dateBeforeEmployees = employeeRepository.findAll().stream()
//                        .filter(e -> e.getStartDate().before(filterDateBefore)).collect(Collectors.toList());
//                if (dateBeforeEmployees.isEmpty()) {
//                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//                } else {
//                    return new ResponseEntity<>(dateBeforeEmployees, HttpStatus.OK);
//                }
//            }
//
//            //filtering (define your filters)
//            if (filterDateAfter != null) {
//                List<Employee> dateAfterEmployees = employeeRepository.findAll().stream()
//                        .filter(e -> e.getStartDate().after(filterDateAfter)).collect(Collectors.toList());
//                if (dateAfterEmployees.isEmpty()) {
//                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//                } else {
//                    return new ResponseEntity<>(dateAfterEmployees, HttpStatus.OK);
//                }
//            }

//             sort by employee name
            if (sortByNameAsec != null) {
                List<Employee> sortEmployeeByName = employeeRepository.findAll().stream()
                        .sorted(Comparator.comparing(Employee::getName)).collect(Collectors.toList());
                if (sortEmployeeByName.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                else if (sortByNameAsec == true) {
                    return new ResponseEntity<>(sortEmployeeByName, HttpStatus.OK);
                }
            }

            List<Employee> employees = employeeRepository.findAll();
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * CRUD Get user by id
     * @param id Long id.
     * @return ResponseEntity<Employee>
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id) {
        Optional<Employee> employeeData = employeeRepository.findById(id);

        if (employeeData.isPresent()) {
            return new ResponseEntity<>(employeeData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * CRUD Create
     * @param employee
     * @return ResponseEntity<ResponseMessage>
     */
    @PostMapping("/users")
    public ResponseEntity<ResponseMessage> createEmployee(@RequestBody Employee employee) {


        if (employeeRepository.findById(employee.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Employee ID already exists"));
        }

        if (isContainLogin(employeeRepository.findAll(), employee.getLogin())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Employee login not unique"));
        }

        if (employee.getSalary().compareTo(BigDecimal.valueOf(0)) == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Invalid salary"));
        }

        if (employee.getStartDate().isAfter(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Invalid date"));
        }

        Employee postedEmployee = employeeRepository
                .save(new Employee(
                        employee.getId(),
                        employee.getName(),
                        employee.getLogin(),
                        employee.getSalary(),
                        employee.getStartDate()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Successfully created"));

    }

    //    Update
    @PutMapping("/users/{id}")
    public ResponseEntity<ResponseMessage> updateEmployee(@PathVariable("id") long id, @RequestBody Employee employee) {
        Optional<Employee> employeeData = employeeRepository.findById(id);

        if (employeeData.isPresent()) {
            Employee emp = employeeData.get();

            // TODO: BUG, NEED TO SEARCH ALL RECORDS EXCEPT UPDATED RECORD
            if (isContainLogin(employeeRepository.findAll(), employee.getLogin())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Employee login not unique"));
            }
            emp.setLogin(employee.getLogin());

            emp.setName(employee.getName());

            if (employee.getSalary().compareTo(BigDecimal.valueOf(0)) == -1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Invalid salary"));
            }
            emp.setSalary(employee.getSalary());

            if (employee.getStartDate().isAfter(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Invalid date"));
            }
            emp.setStartDate(employee.getStartDate());

            employeeRepository.save(emp);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Successfully updated"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("No such employee"));
        }
    }

    //    Delete
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ResponseMessage> deleteEmployee(@PathVariable("id") long id) {
        if(employeeRepository.findById(id).isPresent()) {
            employeeRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Successfully deleted"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("No such employee"));
    }
}
