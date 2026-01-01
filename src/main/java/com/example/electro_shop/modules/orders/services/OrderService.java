package com.example.electro_shop.modules.orders.services;

import com.example.electro_shop.modules.auth.models.User;
import com.example.electro_shop.modules.carts.models.Cart;
import com.example.electro_shop.modules.carts.services.CartService;
import com.example.electro_shop.modules.orders.dtos.CheckoutRequest;
import com.example.electro_shop.modules.orders.dtos.OrderResponse;
import com.example.electro_shop.modules.orders.dtos.OrderSummary;
import com.example.electro_shop.modules.orders.enums.OrderStatus;
import com.example.electro_shop.modules.orders.mappers.OrderMapper;
import com.example.electro_shop.modules.orders.models.Order;
import com.example.electro_shop.modules.orders.models.OrderItem;
import com.example.electro_shop.modules.orders.repositories.OrderRepository;
import com.stripe.Stripe;
import com.stripe.net.Webhook;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;
    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, CartService cartService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public String createCheckoutSession(User user, CheckoutRequest request) throws Exception {
        Cart cart = cartService.getOrCreateCart(user);
        if (cart.getItems().isEmpty()) throw new RuntimeException("El carrito está vacío");

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(cartItem.getProduct());
            oi.setQuantity(cartItem.getQuantity());
            oi.setPriceAtPurchase(cartItem.getProduct().getPrice());
            return oi;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        order.setTotalAmount(orderItems.stream()
                .map(i -> i.getPriceAtPurchase().multiply(new BigDecimal(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        orderRepository.save(order);

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "/checkout/success?order_id=" + order.getOrderId())
                .setCancelUrl(frontendUrl + "/cart")
                .setClientReferenceId(order.getOrderId().toString())
                .addAllLineItem(orderItems.stream().map(this::mapToStripeItem).toList())
                .build();

        Session session = Session.create(params);
        order.setStripePaymentId(session.getId());
        orderRepository.save(order);

        return session.getUrl();
    }

    private SessionCreateParams.LineItem mapToStripeItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(item.getQuantity().longValue())
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmount(item.getPriceAtPurchase().multiply(new BigDecimal(100)).longValue())
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(item.getProduct().getName()).build())
                        .build())
                .build();
    }

    @Transactional
    public void handleWebhook(String payload, String sigHeader) throws Exception {
       com.stripe.model.Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();
            UUID orderPublicId = UUID.fromString(session.getClientReferenceId());

            Order order = orderRepository.findByOrderId(orderPublicId)
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
            cartService.clearCart(order.getUser());
        }
    }


    public OrderResponse getOrderDetails(UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    public List<OrderSummary> getMyOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderRepository.findByUserOrderByOrderDateDesc(user)
                .stream().map(orderMapper::toOrderSummary).collect(Collectors.toList());
    }
}
