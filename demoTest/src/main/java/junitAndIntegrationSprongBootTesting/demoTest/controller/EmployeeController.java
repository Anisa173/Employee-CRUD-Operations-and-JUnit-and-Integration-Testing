package junitAndIntegrationSprongBootTesting.demoTest.controller;

import junitAndIntegrationSprongBootTesting.demoTest.domain.Employee;
import junitAndIntegrationSprongBootTesting.demoTest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Profile("rest")
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService eService;

    public EmployeeController(EmployeeService eService) {
        this.eService = eService;
    }

    @PostMapping("/add/employee")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) throws Exception {
        return ResponseEntity.ok(eService.saveEmployee(employee));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeByEmployeeId(@PathVariable Long employeeId) throws Exception {
        return eService.getEmployeeById(employeeId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<Optional<Employee>> updateEmployee(@PathVariable long employeeId, @RequestBody Employee employee) throws Exception {

        return ResponseEntity.ok(eService.updatedEmployee(employee, employeeId));
    }

    @DeleteMapping("/{emplId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable long emplId) {
        return new ResponseEntity<String>("Employee is removed successfully", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> findAllEmployees() throws Exception {
        return ResponseEntity.ok(eService.findAllEmployees());
    }

    @GetMapping("/all/fName_lName")
    public ResponseEntity<List<Employee>> findByFirstNameAndLastName(@RequestParam String firstName, @RequestParam String lastName) throws Exception {
        return ResponseEntity.ok(eService.findByFirstNameAndLastName(firstName, lastName));
    }

    @GetMapping("/all/lName")
    public ResponseEntity<List<Employee>> findEmployeesByLastName(@RequestParam String lastName) throws Exception {
        return ResponseEntity.ok(eService.findEmployeesByLastName(lastName));
    }

    @GetMapping("/email_lastName")
    public ResponseEntity<Employee> findByEmailAndLastName(@RequestParam String email, @RequestParam String lastName) throws Exception {
        return eService.findByEmailAndLastName(email, lastName).map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.notFound().build());
    }

    @GetMapping("/email_fName")
    public ResponseEntity<Optional<Employee>> findEmployeesByEmailAndByFirstName(@RequestParam String email, @RequestParam String firstName) throws Exception {
        return ResponseEntity.ok(eService.findEmployeesByEmailAndByFirstName(email, firstName));
    }


}
