package com.example.customerapi.service;

import com.example.customerapi.dto.*;
import com.example.customerapi.entity.Customer;
import com.example.customerapi.entity.CustomerStatus;
import com.example.customerapi.exception.ResourceNotFoundException;
import com.example.customerapi.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@SuppressWarnings("null")
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @Override
    public Page<CustomerResponseDTO> getAllCustomers(int page, int size, String sortBy, String sortDir) {
        String sortField = (sortBy != null && !sortBy.trim().isEmpty()) ? sortBy.trim() : "id";
        Sort sort = "desc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();
        Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(page, size, sort));
        return customerPage.map(this::convertToResponseDTO);
    }
    
    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return convertToResponseDTO(customer);
    }
    
    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        Customer customer = convertToEntity(requestDTO);
        Customer saved = customerRepository.save(customer);
        return convertToResponseDTO(saved);
    }
    
    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customer.setCustomerCode(requestDTO.getCustomerCode());
        customer.setFullName(requestDTO.getFullName());
        customer.setEmail(requestDTO.getEmail());
        customer.setPhone(requestDTO.getPhone());
        customer.setAddress(requestDTO.getAddress());
        if (requestDTO.getStatus() != null) {
            customer.setStatus(CustomerStatus.valueOf(requestDTO.getStatus()));
        }
        Customer updated = customerRepository.save(customer);
        return convertToResponseDTO(updated);
    }

    @Override
    public CustomerResponseDTO partialUpdateCustomer(Long id, CustomerUpdateDTO updateDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (updateDTO.getFullName() != null) {
            customer.setFullName(updateDTO.getFullName());
        }
        if (updateDTO.getEmail() != null) {
            customer.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getPhone() != null) {
            customer.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getAddress() != null) {
            customer.setAddress(updateDTO.getAddress());
        }

        Customer updated = customerRepository.save(customer);
        return convertToResponseDTO(updated);
    }
    
    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public List<CustomerResponseDTO> searchCustomers(String keyword) {
        String searchKeyword = keyword != null ? keyword.trim() : "";
        return customerRepository.searchCustomers(searchKeyword).stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public List<CustomerResponseDTO> getCustomersByStatus(String status) {
        try {
            CustomerStatus customerStatus = CustomerStatus.valueOf(status.trim().toUpperCase());
            return customerRepository.findByStatus(customerStatus).stream()
                    .map(this::convertToResponseDTO)
                    .toList();
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new ResourceNotFoundException("Invalid status: " + status);
        }
    }

    @Override
    public List<CustomerResponseDTO> advancedSearch(String name, String email, String status) {
        String nameFilter = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        String emailFilter = (email != null && !email.trim().isEmpty()) ? email.trim() : null;
        CustomerStatus statusFilter = null;
        if (status != null && !status.trim().isEmpty()) {
            try {
                statusFilter = CustomerStatus.valueOf(status.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new ResourceNotFoundException("Invalid status: " + status);
            }
        }
        return customerRepository.advancedSearch(nameFilter, emailFilter, statusFilter).stream()
                .map(this::convertToResponseDTO)
                .toList();
    }
    
    private CustomerResponseDTO convertToResponseDTO(Customer customer) {
        if (customer == null) return null;
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setCustomerCode(customer.getCustomerCode());
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setStatus(customer.getStatus() != null ? customer.getStatus().toString() : null);
        dto.setCreatedAt(customer.getCreatedAt());
        return dto;
    }
    
    private Customer convertToEntity(CustomerRequestDTO dto) {
        if (dto == null) return null;
        Customer customer = new Customer();
        customer.setCustomerCode(dto.getCustomerCode());
        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        return customer;
    }
}
