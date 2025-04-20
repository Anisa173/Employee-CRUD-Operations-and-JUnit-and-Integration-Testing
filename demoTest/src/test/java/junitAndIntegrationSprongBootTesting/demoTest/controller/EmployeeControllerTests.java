package junitAndIntegrationSprongBootTesting.demoTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import junitAndIntegrationSprongBootTesting.demoTest.domain.Employee;
import junitAndIntegrationSprongBootTesting.demoTest.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTests {
    @MockBean
    private EmployeeService eService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

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
        BDDMockito.given(eService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

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
        Employee employee1 = eService.saveEmployee(employee);

        BDDMockito.given(eService.getEmployeeById(employee1.getEmplId())).willReturn(Optional.of(employee1));

        //when - operation to be executed
        ResultActions response = mockMvc.perform(get("/api/employee/{employeeId}", employee1.getEmplId()));

        //then - the action's effect to be verified
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.employeeId", employee1.getEmplId()))
                .andExpect((ResultMatcher) jsonPath("$.firstName", employee1.getFirstName()))
                .andExpect((ResultMatcher) jsonPath("$.lastName", employee1.getLastName()))
                .andExpect((ResultMatcher) jsonPath("$.email", employee1.getEmail()));

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
        Employee employee1 = eService.saveEmployee(employee);

        BDDMockito.given(eService.getEmployeeById(employee1.getEmplId())).willReturn(empty());

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
        eService.saveEmployee(employee);
        Employee employee1 = Employee.builder()
                .firstName("Anisa")
                .lastName("Çela")
                .email("celaanisa07@outlook.com")
                .build();
        eService.saveEmployee(employee1);
        List<Employee> employeeList = List.of(employee, employee1);

        BDDMockito.given(eService.findAllEmployees()).willReturn(employeeList);

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
        Employee employeeSaved = eService.saveEmployee(employee);
        Employee employeeFetched = eService.getEmployeeById(employeeSaved.getEmplId()).get();
        employeeFetched.setEmail("celaanisa08@outlook.com");
        BDDMockito.given(eService.getEmployeeById(employeeSaved.getEmplId()).get()).willReturn(employeeFetched);
        BDDMockito.given(eService.updatedEmployee(employeeFetched, employeeFetched.getEmplId())).willAnswer((invocation) -> invocation.getArgument(0));
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
        Employee employeeSaved = eService.saveEmployee(employee);
        Employee employeeFetched = eService.getEmployeeById(employeeSaved.getEmplId()).get();
        employeeFetched.setEmail("celaanisa08@outlook.com");
        BDDMockito.given(eService.getEmployeeById(employeeSaved.getEmplId())).willReturn(Optional.empty());
        BDDMockito.given(eService.updatedEmployee(employeeFetched, employeeFetched.getEmplId())).willAnswer((invocation) -> invocation.getArgument(0));
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
        employee = eService.saveEmployee(employee1);
        willDoNothing().given(eService).deleteEmployee(employee.getEmplId());
        //when -- the RestAPI to be processed
        ResultActions response = mockMvc.perform(delete("/api/employee/{emplId}", employee.getEmplId()));

        //then -- verify the result
        response.andDo(print())
                .andExpect(status().isOk());
    }

}


