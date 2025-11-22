package com.example.eternal_games;
import java.io.Serializable;

public class Producto implements Serializable {
    public String id;
    public String title;
    public String description;
    public int price;
    //public String code;
    public boolean status;
    public String platform;
    public boolean topSell;
    public String genre;
    public String category;
    public int img;
}