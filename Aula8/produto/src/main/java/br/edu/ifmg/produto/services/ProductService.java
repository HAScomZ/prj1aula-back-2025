package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.CategoryDTO;
import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.repository.ProductRepository;
import br.edu.ifmg.produto.resources.ProductResource;
import br.edu.ifmg.produto.services.exceptions.DatabaseException;
import br.edu.ifmg.produto.services.exceptions.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){

        Page<Product> list = productRepository.findAll(pageable);
        return list.map(product -> new ProductDTO(product)
                        .add(linkTo(methodOn(ProductResource.class).findAll(null)).withSelfRel())
                        .add(linkTo(methodOn(ProductResource.class).findById(product.getId())).withRel("Get a product"))
                //product.getCategories())
        );
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){

        Optional<Product> obj = productRepository.findById(id);
        Product product = obj.orElseThrow(()-> new ResourceNotFound("Product not found with id: " + id));

        return new ProductDTO(product);
                //.add(linkTo().withSelfRel())
                //.add(linkTo().withSelfRel("All products"))
                //.add(linkTo().withSelfRel("Update product"))
                //.add(linkTo().withRel("Delete product"));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){

        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {

        Product entity;
        try {
            entity = productRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = productRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFound("Product not found");
        }

        return new ProductDTO(entity);
    }

    @Transactional
    public void delete(Long id){

        if(productRepository.existsById(id)){
            throw new ResourceNotFound("Product not found");
        }

        try {
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity){
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImageUrl(dto.getImageUrl());

        dto.getCategories().forEach(c -> entity.getCategories().add(new Category(c)));
    }

}
