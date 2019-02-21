package stockist.tekmeda.com.tekmedastockist.bean;

import java.io.Serializable;

public class Stockists implements Serializable{

    private String firstName, lastName, id, emailid, area, enterpriseName,phone,city,pin,street,state,address;

    public Stockists() {
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Stockists(String firstName, String lastName, String id, String emailid, String area, String enterpriseName, String phone, String city, String pin, String street, String state, String address) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.emailid = emailid;
        this.area = area;
        this.enterpriseName = enterpriseName;
        this.phone = phone;
        this.city = city;
        this.pin = pin;
        this.street = street;
        this.state = state;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getArea() {
        return area;
    }

    @Override
    public String toString() {
        return "Stockists{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id='" + id + '\'' +
                ", emailid='" + emailid + '\'' +
                ", area='" + area + '\'' +
                ", enterpriseName='" + enterpriseName + '\'' +
                '}';
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
}
