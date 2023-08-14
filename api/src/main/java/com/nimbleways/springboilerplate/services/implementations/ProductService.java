package com.nimbleways.springboilerplate.services.implementations;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;

@Service
public class ProductService {

    // TODO : create reusable methods to avoid code duplication

    @Autowired
    ProductRepository productRepository;

    @Autowired
    NotificationService notificationService;

    public void notifyDelay(int leadTime, Product p) {
        p.setLeadTime(leadTime);
        productRepository.save(p);
        notificationService.sendDelayNotification(leadTime, p.getName());
    }

    // TODO : change the code structure to create reusable methods ('indisponible', 'normal vente') to increase
    //  code reusability and readability

    // FIXME : When  we moved the business logic from the controller to the Service, the new methods are more visible,
    //  FIXME : The code duplication is more clearer, but because of the time I can't do it right now
    public void handleSeasonalProduct(Product product) {
        if ((LocalDate.now().isAfter(product.getSeasonStartDate()) && LocalDate.now().isBefore(product.getSeasonEndDate())
                && product.getAvailable() > 0)) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else {
            //productService.handleSeasonalProduct(product);

            if (LocalDate.now().plusDays(product.getLeadTime()).isAfter(product.getSeasonEndDate())) {
                notificationService.sendOutOfStockNotification(product.getName());
                product.setAvailable(0);
                productRepository.save(product);
            } else if (product.getSeasonStartDate().isAfter(LocalDate.now())) {
                notificationService.sendOutOfStockNotification(product.getName());
                productRepository.save(product);
            } else {
                notifyDelay(product.getLeadTime(), product);
            }
        }
    }

    public void handleExpiredProduct(Product product) {
        if (product.getAvailable() > 0 && product.getExpiryDate().isAfter(LocalDate.now())) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else {
            //productService.handleExpiredProduct(product);

            if (product.getAvailable() > 0 && product.getExpiryDate().isAfter(LocalDate.now())) {
                product.setAvailable(product.getAvailable() - 1);
                productRepository.save(product);
            } else {
                notificationService.sendExpirationNotification(product.getName(), product.getExpiryDate());
                product.setAvailable(0);
                productRepository.save(product);
            }
        }
    }

    public void handleNormalProduct(Product product) {
        if (product.getAvailable() > 0) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else {
            int leadTime = product.getLeadTime();
            if (leadTime > 0) {
                notifyDelay(leadTime, product);
            }
        }
    }

    public void handleFlashsaleProduct(Product product) {

    }

    // Create internal methods to manage the requests : Goal move operations to those methods and keep the conditions
    // on the service handlers


}