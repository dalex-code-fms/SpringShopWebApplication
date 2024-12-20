package fr.fms.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String brand;

    @NotNull
    @Size(min = 3, max = 50)
    private String description;

    @DecimalMin("50")
    private Double price;

    @ManyToOne
    private Category category;


    public Article(Long id, String brand, String description, Double price, Category category) {
        this.id = id;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public Article() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull @Size(min = 1, max = 20) String getBrand() {
        return brand;
    }

    public void setBrand(@NotNull @Size(min = 1, max = 20) String brand) {
        this.brand = brand;
    }

    public @NotNull @Size(min = 3, max = 50) String getDescription() {
        return description;
    }

    public void setDescription(@NotNull @Size(min = 3, max = 50) String description) {
        this.description = description;
    }

    public @DecimalMin("50") Double getPrice() {
        return price;
    }

    public void setPrice(@DecimalMin("50") Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category=" + category +
                '}';
    }
}
