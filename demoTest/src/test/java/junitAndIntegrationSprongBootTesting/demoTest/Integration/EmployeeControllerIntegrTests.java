package junitAndIntegrationSprongBootTesting.demoTest.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import junitAndIntegrationSprongBootTesting.demoTest.domain.Employee;
import junitAndIntegrationSprongBootTesting.demoTest.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll();
    }

    //JUnit tests case to create a new Employee Record
    @DisplayName("JUnit tests case to create a new Employee Record")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployee() throws Exception {
        //given - precondition or setUp
        employee = Employee.builder()
                .firstName("Anisa")
                .lastName("Çela")
                .email("celaanisa07@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when -- operations to be executed
        ResultActions response = mockMvc.perform(post("/api/employee/add/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then -- result of the operation to be verified
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
    }

    //JUnit Test Case to fetch an EmployRecord based on employeeId --Positive Scenario
    @DisplayName("JUnit Test Case to fetch an EmployRecord based on employeeId")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployee() throws Exception {
        //given - preconditions or setUp
        employee = Employee.builder()
                .firstName("Anisa")
                .lastName("Çela")
                .email("celaanisa07@gmail.com")
                .build();
        Employee employee1 = employeeRepository.save(employee);

        //when - operation to be executed
        ResultActions response = mockMvc.perform(get("/api/employee/{employeeId}", employee1.getEmplId()));

        //then - the action's effect to be verified
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(employee1.getEmplId())))
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())));

    }

    //JUnit Test Case to fetch an EmployRecord based on employeeId --Negative Scenario
    @DisplayName("JUnit Test Case not to fetch an EmployeeRecord based on employeeId")
    @Test
    public void givenEmployeeObject_whenNotFound_thenVerifyResult() throws Exception {
        //given - preconditions or setUp
        employee = Employee.builder()
                .firstName("Anisa")
                .lastName("Çela")
                .email("celaanisa07@gmail.com")
                .build();
        Employee employee1 = employeeRepository.save(employee);

        //when - operation to be executed
        ResultActions response = mockMvc.perform(get("/api/employee/{employeeId}", employee1.getEmplId()));

        //then - the action's effect to be verified
        response.andDo(print())
                .andExpect(status().isNotFound());

    }

    //JUnit Tests Case to getAll Employees from List
    @DisplayName("JUnit Tests Case to getAll Employees from List")
    @Test
    public void givenListOfEmployee_whenFindAllEmployees_thenReturnListOfEmployee() throws Exception {
        //given - preconditions or setUp
        employee = Employee.builder()
                .firstName("Anisa")
                .lastName("Çela")
                .email("celaanisa07@gmail.com")
                .build();
        employeeRepository.save(employee);
        Employee employee1 = Employee.builder()
                .firstName("Anisa")
                .lastName("Çela")
                .email("celaanisa07@outlook.com")
                .build();
        employeeRepository.save(employee1);
        List<Employee> employeeList = List.of(employee, employee1);

        //when -- GetAllEmloyees operation to be executed
        ResultActions response = mockMvc.perform(get("/api/employee/all"));


        //then -- Operation Results that we are going to verify
        response.andDo(print())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));

    }

    //JUnit Test case for update an Employee Record --Positive Scenario
    @DisplayName("JUnit Test case for update an Employee Record")
    @Test
    public void givenEmployeeRecord_whenUpdateEmployeeObject_thenReturnUpdatedEmployee() throws Exception {
        //given - preconditions or setUp
        employee = Employee.builder()
                .firstName("Anisa")
                .lastName("Çela")
                .email("celaanisa07@gmail.com")
                .build();
        Employee employeeSaved = employeeRepository.save(employee);
        Employee employeeFetched = employeeRepository.findById(employeeSaved.getEmplId()).get();
        employeeFetched.setEmail("celaanisa08@outlook.com");

        //when - update operation to be executed
        ResultActions response = mockMvc.perform(put("/api/employee/{employeeId}", employeeFetched.getEmplId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeFetched)));

        //then - Update Record Result to be verified
        response.andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.firstName", is(employeeFetched.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employeeFetched.getLastName())))
                .andExpect(jsonPath("$.email", is(employeeFetched.getEmail())));

    }

    //JUnit Test case for update an Employee Record --Negative Scenario
    @DisplayName("JUnit Test case for update an Employee Record")
    @Test
    public void givenEmployeeRecord_whenUpdateEmployeeObject_thenReturn404() throws Exception {
        //given - preconditions or setUp
        employee = Employee.builder()
                .firstName("Anisa")
                .lastName("Çela")
                .email("celaanisa07@gmail.com")
                .build();
        Employee employeeSaved = employeeRepository.save(employee);
        Employee employeeFetched = employeeRepository.findById(employeeSaved.getEmplId()).get();
        employeeFetched.setEmail("celaanisa08@outlook.com");

        //when - update operation to be executed
        ResultActions response = mockMvc.perform(put("/api/employee/{employeeId}", employeeFetched.getEmplId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeFetched)));

        //then - Update Record Result to be verified
        response.andDo(print())
                .andExpect(status().isNotFound());

    }

    //JUnit Test case for delete an Employee Record
    @DisplayName("JUnit Test case for delete an Employee Record")
    @Test
    public void givenEmployeeObject_whenDeleteById_thenVerifyRemovedEmployee() throws Exception {
        //given -- preconditions or setUp
        Employee employee1 = Employee.builder()
                .firstName("Anisa")
                .lastName("Cela")
                .email("celaanisa07@outlook.com")
                .build();
        employee = employeeRepository.save(employee1);

        //when -- the RestAPI to be processed
        ResultActions response = mockMvc.perform(delete("/api/employee/{emplId}", employee.getEmplId()));

        //then -- verify the result
        response.andDo(print())
                .andExpect(status().isOk());
    }


}
