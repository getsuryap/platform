package org.ospic.security.authentication.users.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ospic.organization.staffs.domains.Staff;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.security.authentication.roles.domain.Role;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "users")
@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@NotNull
	@Size(max = 20)
	private String username;

	@NotBlank(message = "Email may not be blank")
	@NotNull
	@Size(max = 50)
	@Email
	private String email;

	@Column(name = "isStaff")
	private Boolean isStaff = false;

	@NotBlank
	@NotNull
	@Size(max = 120)
	@JsonIgnore
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@JoinColumn(name = "user")
	private Staff staff;



	public User(String username, String email, String password, Boolean isStaff) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.isStaff = isStaff;
	}


}
