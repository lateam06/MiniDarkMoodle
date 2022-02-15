package fr.uca.springbootstrap.models.users;

import fr.uca.springbootstrap.models.modules.Module;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users", 
		uniqueConstraints = {
			@UniqueConstraint(columnNames = "username")
		})
public class UserApi {

	@Id
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_modules",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "module_id"))
	private Set<Module> modules = new HashSet<>();

	public List<Module> getModules(){
		return	new ArrayList<>(modules);


	}

	public UserApi() {
	}

	public UserApi(String username) {
		this.username = username;

	}

	public UserApi(long id , String username){
		this.id = id ;
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserApi userApi = (UserApi) o;
		return Objects.equals(id, userApi.id) && Objects.equals(username, userApi.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username);
	}
}
