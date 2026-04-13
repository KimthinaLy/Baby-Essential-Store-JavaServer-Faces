/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.AddressDAO;
import dao.AddressDAOImpl;
import dao.OrderDAO;
import dao.OrderDAOImpl;
import dao.OrderItemDAO;
import dao.OrderItemDAOImpl;
import dao.ProductDAO;
import dao.ProductDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import model.Address;
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
    UserDAO userDAO = new UserDAOImpl();
    AddressDAO addressDAO = new AddressDAOImpl();

    private List<Order> orders = new ArrayList<>(); //by user id
    private List<OrderItem> orderItems;
    private List<Order> allOrders = new ArrayList<>(); //all orders from all user
    private Order selectedOrder = new Order(); //one specific order get by OrderId
    private Order singleOrder = new Order(); //use when create

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

            singleOrder.setUserId(user.getUserId());
            singleOrder.setAddressId(addressBean.getAddress().getAddressId());
            singleOrder.setTotalAmount(cartBean.getSelectedTotal());
            singleOrder.setOrderStatus("Pending");
            singleOrder.setPaymentStatus("Unpaid");

            int orderId = orderDAO.createOrder(singleOrder);
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
            singleOrder = new Order();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Order Success."));
            
            return "/views/customer/order-history?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Order Failed."));
            e.printStackTrace();
            return null;
        }
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

            allOrders = orderDAO.getAllOrders();
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
            Product p = productDAO.getProductById(pid);

            System.out.println(p.getProductImage());
            return p.getProductImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public void updatePaymentStatus() {
        try {
            orderDAO.updatePaymentStatus(selectedOrder.getOrderId(), selectedOrder.getPaymentStatus());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Payment status updated"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateOrderStatus() {
        try {
            orderDAO.updateOrderStatus(selectedOrder.getOrderId(), selectedOrder.getOrderStatus());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Order status updated"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String manageOrder(int orderId) {
        FacesContext context = FacesContext.getCurrentInstance();
        User user = (User) context
                .getExternalContext()
                .getSessionMap()
                .get("user");

        try {
            orderItems = orderItemDAO.getItemsByOrderId(orderId);
            selectedOrder = getSelectedOrderById(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ("MANAGER".equals(user.getRole())) {
            return "/views/manager/order-detail.xhtml?faces-redirect=true&orderId=" + orderId;
        } else {
            return "/views/employee/order-detail.xhtml?faces-redirect=true&orderId=" + orderId;
        }

    }

    public Order getSelectedOrderById(int orderId) {
        try {
            selectedOrder = orderDAO.getOrderById(orderId);
            orderItems = orderItemDAO.getItemsByOrderId(orderId);
            return selectedOrder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public User getOrderCustomer() {

        try {
            return userDAO.findById(selectedOrder.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCustomerAddress() {
        try {
            Address address = addressDAO.getAddressById(selectedOrder.getAddressId());
            String adr = address.getStreet() + ", " + address.getCity() + ", " + address.getProvince() + ", " + address.getPostalCode() + ".";
            return adr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order getSingleOrder() {
        return singleOrder;
    }

    public void setSingleOrder(Order singleOrder) {
        this.singleOrder = singleOrder;
    }
    
    
}
