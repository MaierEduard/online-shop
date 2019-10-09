package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Customer;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.CustomerRepository;
import org.fasttrackit.onlineshop.transfer.customer.SaveCustomerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CustomerService.class);

    // IoC - Inversion of Control
    private final CustomerRepository customerRepository;

    // Dependency Injection
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(SaveCustomerRequest request) {
        LOGGER.info("Creating customer: {}", request);

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());

        return customerRepository.save(customer);
    }

    public Customer getCustomer(long id) {
        LOGGER.info("Retrieving customer {}", id);
        // using Optional
        return customerRepository.findById(id)
                // lambda expressions
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer " + id + " not found."));
    }

    public Customer updateCustomer(long id, SaveCustomerRequest request) {
        LOGGER.info("Updating customer {}: {}", id, request);

        Customer customer = getCustomer(id);

        BeanUtils.copyProperties(request, customer);

        return customerRepository.save(customer);
    }

}
