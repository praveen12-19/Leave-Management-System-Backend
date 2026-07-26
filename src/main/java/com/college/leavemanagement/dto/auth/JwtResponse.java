package com.college.leavemanagement.dto.auth;

import com.college.leavemanagement.enums.Role;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Long linkedProfileId;

    public JwtResponse() {}

    public JwtResponse(String token, String type, Long id, String username, String email, Role role, Long linkedProfileId) {
        this.token = token;
        if (type != null) this.type = type;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.linkedProfileId = linkedProfileId;
    }

    public static JwtResponseBuilder builder() {
        return new JwtResponseBuilder();
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Long getLinkedProfileId() { return linkedProfileId; }
    public void setLinkedProfileId(Long linkedProfileId) { this.linkedProfileId = linkedProfileId; }

    public static class JwtResponseBuilder {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String username;
        private String email;
        private Role role;
        private Long linkedProfileId;

        JwtResponseBuilder() {}

        public JwtResponseBuilder token(String token) { this.token = token; return this; }
        public JwtResponseBuilder type(String type) { this.type = type; return this; }
        public JwtResponseBuilder id(Long id) { this.id = id; return this; }
        public JwtResponseBuilder username(String username) { this.username = username; return this; }
        public JwtResponseBuilder email(String email) { this.email = email; return this; }
        public JwtResponseBuilder role(Role role) { this.role = role; return this; }
        public JwtResponseBuilder linkedProfileId(Long linkedProfileId) { this.linkedProfileId = linkedProfileId; return this; }

        public JwtResponse build() {
            return new JwtResponse(token, type, id, username, email, role, linkedProfileId);
        }
    }
}
