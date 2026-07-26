package com.college.leavemanagement.entity;

import com.college.leavemanagement.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Long linkedProfileId;

    public User() {}

    public User(Long id, String username, String email, String password, Role role, Long linkedProfileId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.linkedProfileId = linkedProfileId;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Long getLinkedProfileId() { return linkedProfileId; }
    public void setLinkedProfileId(Long linkedProfileId) { this.linkedProfileId = linkedProfileId; }

    public static class UserBuilder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private Role role;
        private Long linkedProfileId;

        UserBuilder() {}

        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder username(String username) { this.username = username; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder role(Role role) { this.role = role; return this; }
        public UserBuilder linkedProfileId(Long linkedProfileId) { this.linkedProfileId = linkedProfileId; return this; }

        public User build() {
            return new User(id, username, email, password, role, linkedProfileId);
        }
    }
}

