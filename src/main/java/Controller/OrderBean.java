/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.OrderDAO;
import dao.OrderDAOImpl;
import dao.OrderItemDAO;
import dao.OrderItemDAOImpl;
import dao.ProductDAO;
import dao.ProductDAOImpl;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.Order;
import model.OrderItem;
import model.Product;
import model.User;

/**
 *
 * @author Admin
 */
@Named
@SessionScoped
public class OrderBean implements Serializable {

    OrderDAO orderDAO = new OrderDAOImpl();
    OrderItemDAO orderItemDAO = new OrderItemDAOImpl();
    ProductDAO productDAO = new ProductDAOImpl();
    private List<Order> orders = new ArrayList<>();
    private List<OrderItem> orderItems;
    private List<Order> allOrders = new ArrayList<>();

    @Inject
    private CartBean cartBean;

    @Inject
    private AddressBean addressBean;

    public String placeOrder() {
        FacesContext context = FacesContext.getCurrentInstance();
        User user = (User) context
                .getExternalContext()
                .getSessionMap()
                .get("user");
        try {

            if (addressBean.getAddress() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Please select a delivery address.", null));
                return null;
            }

            if (cartBean.getSelectedItems() == null || cartBean.getSelectedItems().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Your cart is empty.", null));
                return null;
            }

            Order order = new Order();

            order.setUserId(user.getUserId());

            order.setAddressId(addressBean.getAddress().getAddressId());
            order.setTotalAmount(cartBean.getSelectedTotal());
            order.setOrderStatus("Pending");
            order.setPaymentMethod("COD");
            order.setPaymentStatus("Unpaid");

            int orderId = orderDAO.createOrder(order);
            for (CartItem cartItem : cartBean.getSelectedItems()) {
                System.out.println("Product ID: " + cartItem.getProduct().getProductId());
                System.out.println("Inserting item: " + cartItem.getProduct().getName());

                OrderItem item = new OrderItem();

                item.setOrderId(orderId);
                item.setProductId(cartItem.getProductId());
                item.setQuantity(cartItem.getQuantity());
                item.setProductName(cartItem.getProduct().getName());
                item.setPrice(cartItem.getProduct().getPrice());

                orderItemDAO.insertOrderItem(item);
            }

            // clear cart
            cartBean.deleteSelected();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Order Success."));

            return "/views/customer/order-history?faces-redirect=true";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Order Failed."));
            e.printStackTrace();
        }

        return null;
    }

    public List<Order> getOrders() {
        FacesContext context = FacesContext.getCurrentInstance();
        User user = (User) context
                .getExternalContext()
                .getSessionMap()
                .get("user");

        try {
            orders = orderDAO.getOrdersByUser(user.getUserId());
            return orders;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String viewOrderItems(int orderId) {
        try {
            orderItems = orderItemDAO.getItemsByOrderId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/views/customer/order-detail.xhtml?faces-redirect=true&orderId=" + orderId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void deleteOrder(int orderId) {
        try {

            orderDAO.deleteOrder(orderId);

            orders.removeIf(o -> o.getOrderId() == orderId);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Order deleted successfully"));

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            e.getMessage(), null));
        }
    }

    public String getOrderItemImage(int pid) {

        try {
            System.out.println("======Id=====" + pid + "==================");
            Product p = productDAO.getProductById(pid);

            System.out.println(p.getProductImage());
            return p.getProductImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error Image=================";
    }
    
     public List<Order> getAllOrders() {
        try {
            allOrders = orderDAO.getAllOrders();
            return allOrders;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
     
     
     public void updatePaymentStatus(Order order){
    try{
        orderDAO.updatePaymentStatus(order.getOrderId(), order.getPaymentStatus());
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage("Payment status updated"));
    }catch(Exception e){
        e.printStackTrace();
    }
}

public void updateOrderStatus(Order order){
    try{
        orderDAO.updateOrderStatus(order.getOrderId(), order.getOrderStatus());
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage("Order status updated"));
    }catch(Exception e){
        e.printStackTrace();
    }
}
}
