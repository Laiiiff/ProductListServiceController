package com.example.unicam;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Product {
    @Id
    private String id;

    private String name;

    public String getName() {
        return name;
    }

    public void setNome(String nome) {
        this.name = nome;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
