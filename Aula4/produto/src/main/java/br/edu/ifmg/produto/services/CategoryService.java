package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.CategoryDTO;
import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.repository.CategoryRepository;
//import jakarta.transaction.Transactional;,
import br.edu.ifmg.produto.services.exceptions.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(categoria-> new CategoryDTO(categoria)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = categoryRepository.findById(id);

        Category category = obj.orElseThrow( () -> new ResourceNotFound("Category not found: " + id));
        return new CategoryDTO(category);

    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto){
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto){
        try{
            Category entity = categoryRepository.getReferenceById(id);
            entity.setName(dto.getName());
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFound("Category not found: "+id);
        }
    }

}
