package com.example.customerapi.repository;

import com.example.customerapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // TODO: Add method to find by customer code
    
    // TODO: Add method to find by email
    
    // TODO: Add method to check if customer code exists
    
    // TODO: Add method to check if email exists
    
    // TODO: Add method to find by status
}
