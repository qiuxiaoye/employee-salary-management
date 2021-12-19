package govtech.nphc.employeesalarymanagement.utils;

import govtech.nphc.employeesalarymanagement.model.Employee;

import java.util.List;

public class Utils {
    public static boolean isContainLogin(List<Employee> employees, String login) {
        for (Employee e : employees) {
            if (e.getLogin().equals(login)) return true;
        }
        return false;
    }

    public static boolean isEmptyOrWhiteSpace(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean isNullEmployee(Employee employee) {
        return (employee.getId() == 0
                || isEmptyOrWhiteSpace(employee.getName())
                || isEmptyOrWhiteSpace(employee.getLogin())
                || employee.getSalary() == null
                || employee.getStartDate() == null);
    }
}
