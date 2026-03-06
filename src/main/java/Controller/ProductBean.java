/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.ProductDAO;
import dao.ProductDAOImpl;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import model.Product;

/**
 *
 * @author Admin
 */
@Named("productBean")
@ViewScoped
public class ProductBean implements Serializable {

    private ProductDAO productDAO = new ProductDAOImpl();
    private Product product = new Product();

    public List<Product> getList() {
        try {
            return productDAO.getAllProducts();
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(int id) {
        try {
            productDAO.deleteProduct(id);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Deleted"));
        } catch (Exception e) {
        }
    }

    public String insert() {
        try {
            productDAO.insertProduct(product);
            return "/CRUD_View/Read/ViewDBTable?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save failed", null));
            return null; //stay on the same page and rerender the current view
        }
    }

    public void getProductById() {
        try {
            String sId = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRequestParameterMap().get("id");
            if (sId != null) {
                int id = Integer.parseInt(sId);
                product = productDAO.getProductById(id);
            }
        } catch (Exception e) {
        }
    }
    
    public String update(){
        try{
        productDAO.updateProduct(product);
        FacesContext.getCurrentInstance()
                .addMessage(null,new FacesMessage("Updated successfully"));
        return "/CRUD_View/Update/showDBUpdateTable";
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save failed", null));
            return null;
        }
    }

    public Product getProduct() {
        return product;
    }
    
}