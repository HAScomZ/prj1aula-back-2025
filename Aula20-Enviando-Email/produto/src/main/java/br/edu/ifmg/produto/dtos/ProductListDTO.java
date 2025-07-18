package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.projections.ProductProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProductListDTO extends RepresentationModel<ProductListDTO> {
    @Schema(description = "Database generated ID product")
    private long id;
    @Schema(description = "Product Name")
    @Size(min = 3, max = 255, message = "Deve ter entre 3 e 255 caracteres")
    private String name;
    @Schema(description = "A detailed description of the product")
    private String description;
    @Schema(description = "Product Price")
    @Positive(message = "Deve ser maior que zero")
    private double price;
    @Schema(description = "A image url represents this product")
    private String imageUrl;
    @Schema(description = "Product categories (one or more)")
    @NotEmpty(message = "Deve ter pelo menos uma categoria")
    private Set<CategoryDTO> categories = new HashSet<>();

    public ProductListDTO() {

    }
    public ProductListDTO(String name, String description, double price, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ProductListDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();

        entity.getCategories().forEach(c -> this.categories.add(new CategoryDTO(c)));
    }

    public ProductListDTO(Product product, Set<Category> categories) {
        this(product);
        categories.forEach(c-> this.categories.add(new CategoryDTO(c)));
    }

    public ProductListDTO(ProductProjection projection) {
        this.id = projection.getId();
        this.name = projection.getName();
        this.price = projection.getPrice();
        this.imageUrl = projection.getImageUrl();
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
        if (!(o instanceof ProductListDTO product)) return false;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", categories=" + categories +
                '}';
    }
}