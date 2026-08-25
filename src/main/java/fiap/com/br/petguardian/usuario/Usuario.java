package fiap.com.br.petguardian.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.tarefa.Tarefa;
import fiap.com.br.petguardian.usuariopet.UsuarioPet;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuario")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Tarefa> tarefas = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<UsuarioPet> usuarioPets = new HashSet<>();

    // Preenchido pelo AutenticacaoService no momento da autenticação — não é persistido.
    @Transient
    @Builder.Default
    private Boolean responsavelPrincipalFamilia = false;

    // ---- UserDetails ----
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_TUTOR"));
        if (Boolean.TRUE.equals(responsavelPrincipalFamilia)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_DONO_FAMILIA"));
        }
        return authorities;
    }

    @Override
    public String getPassword() { return senha; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}