package br.edu.ifmg.produto.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Entity
@Table(name = "tb_role")
public class Role implements GrantedAuthority{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String authority;

    public Role (){
    }

    public Role(long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Role role)) return false;
        return id == role.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
