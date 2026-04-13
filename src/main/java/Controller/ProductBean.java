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
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import model.Category;
import model.Occasion;
import model.Product;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Admin
 */
@Named("productBean")
@SessionScoped
public class ProductBean implements Serializable {

    final private ProductDAO productDAO = new ProductDAOImpl();
    final private CategoryDAO categoryDAO = new CategoryDAOImpl();
    final private OccasionDAO occasionDAO = new OccasionDAOImpl();

    private Product product = new Product();
    private List<Product> products;
    private List<Category> categories;
    private List<Occasion> occasions;
    private List<Product> selectedProducts;

    private String searchKeyword;

    @PostConstruct
    public void init() {
        try {
            loadSearchProducts();
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
            products = productDAO.getAllProducts();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Deleted"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Delete Failed", null));
        }
    }

    public void insert() {
        try {
            productDAO.insertProduct(product);
            products = productDAO.getAllProducts();
            product = new Product(); 
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save failed", null));
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

    public void update() {
        try {
            productDAO.updateProduct(product);
            products = productDAO.getAllProducts();
            product = new Product(); 
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage("Updated successfully"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save failed", null));
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public void deleteSelectedProducts() {

        if (selectedProducts == null || selectedProducts.isEmpty()) {
            return;
        }

        for (Product p : selectedProducts) {
            delete(p.getProductId());
        }

        selectedProducts = null;
    }

    public void openNew() {
        product = new Product();
    }

    public void save() {

        try {

            if (product.getProductId() == 0) {
                productDAO.insertProduct(product);
            } else {
                productDAO.updateProduct(product);
            }

            products = productDAO.getAllProducts();
            product = new Product(); 

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Product saved"));

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save failed", null));

        }
    }

    //=================== Search ======================
    

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
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
    
    
    public void handleFileUpload(FileUploadEvent event) {
    UploadedFile file = event.getFile();
    if (file != null) {
        // Set the byte array directly into your product object
        this.product.setImage(file.getContent());
        
        FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        System.out.println("Uploaded: " + file.getFileName() + " Size: " + file.getSize());
    }
}
}
