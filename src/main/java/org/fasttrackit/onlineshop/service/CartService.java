package org.fasttrackit.onlineshop.service;


import org.fasttrackit.onlineshop.domain.Cart;
import org.fasttrackit.onlineshop.domain.Customer;
import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.CartRepository;
import org.fasttrackit.onlineshop.transfer.cart.AddProductToCartRequest;
import org.fasttrackit.onlineshop.transfer.cart.CartResponse;
import org.fasttrackit.onlineshop.transfer.product.ProductInCartResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

@Service
public class CartService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, CustomerService customerService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    @Transactional
    public void addProductToCart(AddProductToCartRequest request) {
        LOGGER.info("Adding product to cart: {}", request);

        Cart cart = cartRepository.findById(request.getCustomerId())
                .orElse(new Cart());

        if (cart.getCustomer() == null) {
            LOGGER.debug("Cart doesn't exist. Retrieving customer to create a new cart.");
            Customer customer = customerService.getCustomer(request.getCustomerId());
            cart.setCustomer(customer);
        }

        Product product = productService.getProduct(request.getProductId());

        cart.addToCart(product);

        cartRepository.save(cart);
    }

    @Transactional
    public CartResponse getCart(Long customerId) {
        LOGGER.info("Retrieving cart for customer {}", customerId);
        Cart cart = cartRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "There is no cart for customer " + customerId));

        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cart.getId());

        Iterator<Product> iterator = cart.getProducts().iterator();

        while (iterator.hasNext()) {
            Product product = iterator.next();

            ProductInCartResponse productInCartResponse = new ProductInCartResponse();
            productInCartResponse.setId(product.getId());
            productInCartResponse.setName(product.getName());
            productInCartResponse.setPrice(product.getPrice());

            cartResponse.getProducts().add(productInCartResponse);
        }

        return cartResponse;
    }
}
