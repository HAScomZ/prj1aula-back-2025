package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.RoleDTO;
import br.edu.ifmg.produto.dtos.UserDTO;
import br.edu.ifmg.produto.dtos.UserInsertDTO;


import br.edu.ifmg.produto.entities.Role;
import br.edu.ifmg.produto.entities.User;
import br.edu.ifmg.produto.projections.UserDetailsProjection;
import br.edu.ifmg.produto.repository.RoleRepository;
import br.edu.ifmg.produto.repository.UserRepository;
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

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable){

        Page<User> list = repository.findAll(pageable);
        return users.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id){

        Optional<User> opt = repository.findById(id);
        User user = opt.orElseThrow(() -> new ResourceNotFound("User not found"));

        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto){

        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = repository.save(entity);
        return new UserDTO(user);

    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {

        try {
            User entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);

            return new UserDTO(entity);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFound("User not found" + id);
        }

    }

    @Transactional
    public void delete(Long id){
        if (!repository.existsById(id)) {
            throw new ResourceNotFound("User not found" + id);
        }

        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for (RoleDTO role : dto.getRoles()) {
            Role roleEntity = roleRepository.getReferenceById(role.getId());
            entity.getRoles().add(roleEntity);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        User user = new User();
        user.setEmail(result.getFirst().getUsername());
        user.setPassword(result.getFirst().getPassword());
        for(UserDetailsProjection projection: result){
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }

}
