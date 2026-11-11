insert into app_role (name, created_at, created_by)
values ('View Users', current_timestamp, 'system');

insert into resource (code, name, created_at, created_by)
values ('USER', 'User entity', current_timestamp, 'system'),
       ('ROLE', 'Role entity', current_timestamp, 'system'),
       ('RESOURCE', 'Resource entity', current_timestamp, 'system'),
       ('PERMISSION', 'Permissions', current_timestamp, 'system');

insert into app_permission (role_id, resource_id, action)
values (1, 1, 'READ')