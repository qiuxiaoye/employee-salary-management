package govtech.nphc.employeesalarymanagement.repository;

import govtech.nphc.employeesalarymanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
