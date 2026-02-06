package org.example.ecommerce.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Name is required")
	@Column(nullable = false)
	private String username;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	// @UniqueUserEmail(message = "{user.email.unique.error}")
	@Column(nullable = false, unique = true)
	private String email;

	@NotBlank(message = "Password is required")
	@Column(nullable = false)
	private String password;

	@Column(length = 50)
	private String role;

	@Column(length = 500)
	private String profilePicture;

	private java.time.LocalDate birthDate;

	// --- HIBERNATE MAPPING EXAMPLES ---

	/**
	 * One-to-One relationship with Cart.
	 * 
	 * mappedBy = "user": This signifies that User is the inverse side of the
	 * relationship.
	 * The 'user' field in the Cart entity owns the relationship (contains the
	 * foreign key).
	 * 
	 * cascade = CascadeType.ALL: Operations (PERSIST, MERGE, REMOVE, REFRESH,
	 * DETACH)
	 * performed on the User entity will be cascaded to the associated Cart entity.
	 * 
	 * orphanRemoval = true: If a User's cart is set to null, the Cart entity will
	 * be
	 * deleted from the database.
	 */
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Cart cart;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}


}
//class UserDetailServiceImpl implements UserDetailsService{
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//}

//class CustomAuthenticationProvider implements AuthenticationProvider {
//
//	@Override
//	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//		// TODO Auto-generated method stub
//		return new UsernamePasswordAuthenticationToken(null, null);
//	}
//
//	@Override
//	public boolean supports(Class<?> authentication) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//}
