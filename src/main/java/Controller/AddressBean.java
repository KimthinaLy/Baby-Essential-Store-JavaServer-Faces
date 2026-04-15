/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.AddressDAO;
import dao.AddressDAOImpl;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import model.Address;
import model.User;

/**
 *
 * @author Admin
 */

@Named
@SessionScoped
public class AddressBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private Address address = new Address();
    private AddressDAO addressDAO = new AddressDAOImpl();

    public Address getAddress() {
        FacesContext context = FacesContext.getCurrentInstance();
        User user = (User) context
                .getExternalContext()
                .getSessionMap()
                .get("user");
       try{
             address = addressDAO.getAddressByUser(user.getUserId());
       }catch (Exception e){
           e.printStackTrace();
       } 
        return address;   
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    public Address getAddressByUserId(int userId){
        try{
            address = addressDAO.getAddressByUser(userId);
        } catch(Exception e){
            e.printStackTrace();
        }
        return address;
    }
    
     public String getAddressLineByUserId(int userId){
         String adr="";
        try{
            address = addressDAO.getAddressByUser(userId);
             adr = address.getStreet() + ", " + address.getCity() + ", " + address.getProvince() + ", " + address.getPostalCode() + ".";
        } catch(Exception e){
            e.printStackTrace();
        }
        
        if ("".equals(adr))
            return "---";
        else
            return adr;
    }
}
