package govtech.nphc.employeesalarymanagement.repository;

import govtech.nphc.employeesalarymanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
//    Optional<List<Employee>> findByLogin(String login);
}
