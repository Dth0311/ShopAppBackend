package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.OrderdDetailRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderRepository orderRepository;
    private final OrderdDetailRepository orderdDetailRepository;
    private final ProductRepository productRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()->new DataNotFoundException("Cannot find order with id: " + orderDetailDTO.getOrderId()));

        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()->new DataNotFoundException("Cannot find product with id: " + orderDetailDTO.getProductId()));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProduct())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .price(orderDetailDTO.getPrice())
                .build();
         return orderdDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderdDetailRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot find orderdetail with id: " + id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail orderDetail = orderdDetailRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find orderDetail with id:" + id));

        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()->new DataNotFoundException("Cannot find order with id: " + orderDetailDTO.getOrderId()));

        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()->new DataNotFoundException("Cannot find product with id: " + orderDetailDTO.getProductId()));

        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setColor(orderDetailDTO.getColor());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        orderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProduct());
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        return orderdDetailRepository.save(orderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderdDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderdDetailRepository.findByOrderId(orderId);
    }
}
