/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import dao.OccasionDAO;
import dao.OccasionDAOImpl;
import dao.ProductDAO;
import dao.ProductDAOImpl;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import model.Category;
import model.Occasion;
import model.Product;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Admin
 */
@Named("productBean")
@ViewScoped
public class ProductBean implements Serializable {

    final private ProductDAO productDAO = new ProductDAOImpl();
    final private CategoryDAO categoryDAO = new CategoryDAOImpl();
    final private OccasionDAO occasionDAO = new OccasionDAOImpl();

    private Product product = new Product();
    private List<Product> products;
    private List<Category> categories;
    private List<Occasion> occasions;

    private String searchKeyword;
    private Integer selectedProductId;
    private Product selectedProduct;

    @PostConstruct
    public void init() {
        try {
            products = productDAO.getAllProducts();
            categories = categoryDAO.getAllCategories();
            occasions = occasionDAO.getAllOccasions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Occasion> getOccasions() {
        return occasions;
    }

    //delete later
    public List<Product> getList() {
        try {
            products = productDAO.getAllProducts();
            return products;
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

    public String update() {
        try {
            productDAO.updateProduct(product);
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage("Updated successfully"));
            return "/CRUD_View/Update/showDBUpdateTable";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save failed", null));
            return null;
        }
    }

    public Product getProduct() {
        return product;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void filterByCategory(int categoryId) {
        try {
            products = productDAO.getProductsByCategory(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filterByOccasion(int occasionId) {
        try {
            products = productDAO.getProductsByOccasion(occasionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //=================== Search ======================
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public Integer getSelectedProductId() {
        return selectedProductId;
    }

    public void setSelectedProductId(Integer selectedProductId) {
        this.selectedProductId = selectedProductId;
    }

    public List<Product> completeProduct(String query) {

        try {
            return productDAO.searchByName(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void selectProduct(SelectEvent<Product> event) {
        Product p = event.getObject();
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("product-detail.xhtml?id=" + p.getProductId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchProducts() {

        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("products.xhtml?keyword=" + searchKeyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSearchProducts() {

        try {

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {

                products = productDAO.searchByName(searchKeyword);

            } else {

                products = productDAO.getAllProducts();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
