ALTER TABLE user_roles
    ADD CONSTRAINT FK_UserRoles_Role
        FOREIGN KEY (role_id)
            REFERENCES Role(id);

ALTER TABLE user_roles
    ADD CONSTRAINT FK_UserRoles_User
        FOREIGN KEY (user_id)
            REFERENCES User(id);