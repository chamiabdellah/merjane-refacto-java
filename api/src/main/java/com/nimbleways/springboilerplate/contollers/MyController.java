package com.nimbleways.springboilerplate.contollers;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;

import java.time.LocalDate;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class MyController {

    // TODO : rename the dependencies names => more readable
    @Autowired
    private ProductService productService;

    // TODO : move the DAO layer to the Service
    @Autowired
    private ProductRepository productRepository;

    // TODO : the OrderRepository should be called from the Service layer
    @Autowired
    private OrderRepository orderRepository;

    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @PostMapping("{orderId}/processOrder")
    // TODO : return a 'ResponseEntity', the response is not always 'OK'
    //@ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<ProcessOrderResponse> processOrder(@PathVariable Long orderId) {
        // TODO : move the logic to the service
        // DONE : if the article was not found an exception should be thrown
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException(orderId));
        // DONE : replace the println with a logger
        logger.info("Processing the order with the id : " + orderId);
        // DONE : delete the 'ids', they are not used anywhere in the code (dead code)
        // TODO : What if the order doesn't contain any product ??? => return an OK, because the set is empty
        Set<Product> products = order.getItems();
        // DONE : rename the "p" variable to "product"
        for (Product product : products) {
            // TODO : replace the if statements with 'switch' statements

            switch (product.getType()){
                case "NORMAL" :
                    productService.handleNormalProduct(product);
                    break;
                case "SEASONAL" :
                    productService.handleSeasonalProduct(product);
                    break;
                case "EXPIRABLE" :
                    productService.handleExpiredProduct(product);
                    break;
                case "FLASHSALE" :
                    productService.handleFlashsaleProduct(product);
                    break;
                default:
                    // throw : BAD_REQUEST exception with a comment
            }
            // TODO : add the 'FLASHSALE' products in a new switch (use the existing methods from old products)
            // TODO : add a 'default' case if none types match
        }

        return ResponseEntity.ok(new ProcessOrderResponse(order.getId()));
    }
}
