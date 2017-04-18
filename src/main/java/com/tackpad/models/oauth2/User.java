/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tackpad.models.oauth2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tackpad.models.Company;
import com.tackpad.models.Message;
import com.tackpad.models.UserDeviceFCMToken;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.UserStatus;
import com.tackpad.requests.CreateBossinessUserForm;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import  org.hibernate.annotations.CascadeType;

@Getter
@Setter
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = {Message.UpdateMessageValidation.class,
			UserNotification.CreateUserNotificationValidation.class,
			UserDeviceFCMToken.UserDeviceFCMTokenValidation.class})
	private Long id;

	@NotEmpty
	@NotNull(groups = CreateBossinessUserForm.CreateBusinessUserValidation.class)
	private String name;

	@NotEmpty
	@JsonProperty("email")
	@Column(name = "email", unique = true, nullable = false)
	@NotNull(groups = CreateBossinessUserForm.CreateBusinessUserValidation.class)
	private String login;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@NotEmpty
	@Length(min = 6, groups = CreateBossinessUserForm.CreateBusinessUserValidation.class)
	@NotNull(groups = CreateBossinessUserForm.CreateBusinessUserValidation.class)
	@JsonIgnore
	private String password;

	@Valid
	@ManyToOne
	private Company company;

	@NotEmpty
	@NotNull(groups = CreateBossinessUserForm.CreateBusinessUserValidation.class)
	private String phoneNumber;

	@ManyToMany(fetch = FetchType.EAGER)
	@Cascade(CascadeType.DELETE)
	@JoinTable(name = "user_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private Set<UserRole> userRoles = new HashSet<UserRole>();

	public User() {
	}

	public User(User user) {
		super();
		this.id = user.id;
		this.name = user.name;
		this.login = user.login;
		this.password = user.getPassword();
		this.userRoles = user.userRoles;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	public String getEmail() {
		return login;
	}

	@JsonProperty
	public void setEmail(String email) {
		this.login = email;
	}
}
