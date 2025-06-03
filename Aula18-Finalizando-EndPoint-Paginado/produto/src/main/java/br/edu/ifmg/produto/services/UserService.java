package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.dtos.RoleDTO;
import br.edu.ifmg.produto.dtos.UserDTO;
import br.edu.ifmg.produto.dtos.UserInsertDTO;
import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.entities.Role;
import br.edu.ifmg.produto.entities.User;
import br.edu.ifmg.produto.projections.UserDetailsProjection;
import br.edu.ifmg.produto.repository.RoleRepository;
import br.edu.ifmg.produto.repository.UserRepository;
import br.edu.ifmg.produto.resources.ProductResource;
import br.edu.ifmg.produto.services.exceptions.DatabaseException;
import br.edu.ifmg.produto.services.exceptions.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> list = repository.findAll(pageable);
        // 'list' tem uma lista de 'User'.
        // O map() vai transformar em uma lista de 'UserDTO', que é o tipo que deve ser retornado
        return list.map(u -> new UserDTO(u));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> opt = repository.findById(id);
        User user = opt.orElseThrow(() -> new ResourceNotFound("User not found"));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {

        User entity = new User();
        copyDtoToEntity(dto, entity);
        // "Criptografa" a senha:
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        User novo = repository.save(entity);
        return new UserDTO(novo);
    }





    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        try {
            User entity = repository.getReferenceById(id); // Verifica se existe
            copyDtoToEntity(dto, entity);                  // Traz os novos dados
            entity = repository.save(entity);              // Salva com os novo dados

            return new UserDTO(entity);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFound("User not found with id " + id);
        }
    }

    @Transactional
    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFound("User not found with id " + id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for (RoleDTO role : dto.getRoles()) {
            Role r = roleRepository.getReferenceById(role.getId());
            entity.getRoles().add(r);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result
                = repository.searchUserAndRoleByEmail(username);

        if (result.isEmpty()){
            throw new UsernameNotFoundException("Email not found");
        }

        User user = new User();
        user.setEmail(result.get(0).getUsername());
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection p : result) {
            user.addRole(new Role(p.getRoleId(), p.getAuthority()));
        }
        return user;
    }
}
