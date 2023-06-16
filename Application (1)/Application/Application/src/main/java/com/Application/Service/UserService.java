package com.Application.Service;

import com.Application.DTO.EmployeeDTO;
import com.Application.Model.Employee;
import com.Application.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(employee.getUsername(), employee.getPassword(),
                new ArrayList<>());
    }

    public Employee save(EmployeeDTO user) {
        Employee newEmployee = new Employee();
        newEmployee.setUsername(user.getUsername());
        newEmployee.setPassword(bcryptEncoder.encode(user.getPassword()));
        newEmployee.setAge(user.getAge());
        newEmployee.setName(user.getName());
        newEmployee.setType(user.getType());
        newEmployee.setSalary(user.getSalary());

        return employeeRepository.save(newEmployee);
    }


    public String removeEmployee(int id) {

        employeeRepository.deleteById(id);

        return "Data has been deleted";
    }

    public Optional<Employee> findEmployee(int id) {

        Optional <Employee> emp = employeeRepository.findById(id);

        if(emp.isPresent()){
            return emp;
        }else {

            return null;
        }
    }

    public List<Employee> getAllEmployee() {

        List <Employee> list_employee = employeeRepository.findAll();

        return list_employee;
    }

    public String updateEmployee(Employee employee) {
        Optional<Employee> emp = employeeRepository.findById(employee.getId());

        if (emp.isPresent()) {
            Employee existingEmployee = emp.get();
            existingEmployee.setName(employee.getName());
            existingEmployee.setAge(employee.getAge());
            existingEmployee.setType(employee.getType());
            existingEmployee.setSalary(employee.getSalary());

            employeeRepository.save(existingEmployee);
            return "Employee updated successfully";
        } else {
            return "Employee not found";
        }
    }
}
