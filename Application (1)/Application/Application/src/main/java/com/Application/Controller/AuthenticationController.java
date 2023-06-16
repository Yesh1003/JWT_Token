package com.Application.Controller;

import com.Application.DTO.EmployeeDTO;
import com.Application.Model.Employee;
import com.Application.Payload.Request.JWTRequest;
import com.Application.Payload.Response.JWTResponse;
import com.Application.Security.JWTTokenUtil;
import com.Application.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public Object createAuthenticationToken(@RequestBody JWTRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JWTResponse(token));

    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody EmployeeDTO user) throws Exception {
        return ResponseEntity.ok(userService.save(user));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeEmployee(@PathVariable int id){

        userService.removeEmployee(id);

        return new ResponseEntity<String>("Removed Successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Optional<Employee>> findEmployee(@PathVariable int id){
        Optional<Employee> emps = userService.findEmployee(id);
        return new ResponseEntity<Optional<Employee>>(emps, HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> listofEmployees(){
        List<Employee> list = userService.getAllEmployee();
        return new ResponseEntity<List<Employee>>(list,HttpStatus.ACCEPTED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable int id, @RequestBody Employee updatedEmployee) {
        Optional<Employee> employeeOptional = userService.findEmployee(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();

            employee.setName(updatedEmployee.getName());
            employee.setAge(updatedEmployee.getAge());
            employee.setType(updatedEmployee.getType());
            employee.setSalary(updatedEmployee.getSalary());

            userService.updateEmployee(employee);
            return new ResponseEntity<String>("Employee details updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Employee not found", HttpStatus.NOT_FOUND);
        }
    }
}
