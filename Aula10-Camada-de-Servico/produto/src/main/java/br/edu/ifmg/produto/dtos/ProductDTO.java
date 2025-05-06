package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.entities.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Table(name="tb_product")
public class ProductDTO extends RepresentationModel<ProductDTO> {

    @Schema(description = "Database generated id product")
    private long id;
    @Schema(description = "Product name")
    private String name;
    @Schema(description = "A detailed description of the product")
    private String description;
    @Schema(description = "Product price")
    private double price;
    @Schema(description = "Product url of the image")
    private String imageUrl;
    @Schema(description = "Product categories (one or more)")
    private Set<CategoryDTO> categories = new HashSet<>();

    public ProductDTO(){
    }

    public ProductDTO(long id, String name, String description, double price, String imageUrl,
                      Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ProductDTO(Product entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();

        entity.getCategories().forEach(c -> this.categories.add(new CategoryDTO(c)));
    }

    public ProductDTO(Product product, Set<Category> categories){
        this(product);
        categories.forEach(c -> this.categories.add(new CategoryDTO(c)));
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductDTO product)) return false;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", categories=" + categories +
                '}';
    }
}
