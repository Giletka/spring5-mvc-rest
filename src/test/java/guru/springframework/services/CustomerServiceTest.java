package guru.springframework.services;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.domain.Customer;
import guru.springframework.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private static final long ID = 1L;
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Smith";
    private static final String CUSTOMER_URL = "/api/v1/customers/1";

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(CustomerMapper.INSTANCE, customerRepository);
    }

    @Test
    void getAllCustomers() {
        List<Customer> customers = Arrays.asList(new Customer(), new Customer(), new Customer());

        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerDTO> customerDTOs = customerService.getAllCustomers();
        assertEquals(3, customerDTOs.size());
    }

    @Test
    void getCustomerById() {
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstname(FIRSTNAME);
        customer.setLastname(LASTNAME);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        CustomerDTO customerDTO = customerService.getCustomerById(ID);
        assertEquals(FIRSTNAME, customerDTO.getFirstname());
        assertEquals(LASTNAME, customerDTO.getLastname());
        assertEquals(CUSTOMER_URL, customerDTO.getCustomerUrl());
    }

    @Test
    void getCustomerByIdIfEmpty() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customerService.getCustomerById(ID));
    }

    @Test
    void createNewCustomers() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Jim");

        Customer savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setFirstname(customerDTO.getFirstname());
        savedCustomer.setLastname(customerDTO.getLastname());

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerDTO savedDTO = customerService.createNewCustomer(customerDTO);
        assertEquals(customerDTO.getFirstname(), savedDTO.getFirstname());
        assertEquals("/api/v1/customers/1", savedDTO.getCustomerUrl());
    }

    @Test
    void saveCustomerByDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Jim");

        Customer savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setFirstname(customerDTO.getFirstname());
        savedCustomer.setLastname(customerDTO.getLastname());

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerDTO savedDTO = customerService.saveCustomerByDTO(1L, customerDTO);
        assertEquals(customerDTO.getFirstname(), savedDTO.getFirstname());
        assertEquals("/api/v1/customers/1", savedDTO.getCustomerUrl());
    }

    @Test
    void deleteCustomerById() {
        customerRepository.deleteById(ID);
        verify(customerRepository, times(1)).deleteById(anyLong());
    }
}