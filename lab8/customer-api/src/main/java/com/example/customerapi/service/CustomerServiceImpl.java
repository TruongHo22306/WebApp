package com.example.customerapi.service;

import com.example.customerapi.dto.*;
import com.example.customerapi.entity.Customer;
import com.example.customerapi.entity.CustomerStatus;
import com.example.customerapi.exception.ResourceNotFoundException;
import com.example.customerapi.repository.CustomerRepository;
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
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .toList();
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
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
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
