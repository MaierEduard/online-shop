package org.fasttrackit.onlineshop.web;


import org.fasttrackit.onlineshop.service.CartService;
import org.fasttrackit.onlineshop.transfer.cart.AddProductToCartRequest;
import org.fasttrackit.onlineshop.transfer.cart.CartResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/carts")
public class CartController {


    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PutMapping
    public ResponseEntity addProductToCart(
            @RequestBody @Valid AddProductToCartRequest request) {
        cartService.addProductToCart(request);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCart(@PathVariable("id") long customerId) {
        CartResponse cart = cartService.getCart(customerId);
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }


}
