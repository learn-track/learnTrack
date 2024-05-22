# 3. Access Policy

Date: 2024-05-21

## Status

Accepted

## Context

The application requires the creation of distinct user types, each possessing unique access privileges. The challenge we face is the development of a role-based access control system that is both secure and scalable. The roles we have identified thus far include Student, Teacher, and Admin.

## Decision

We have decided to implement a role-based access control system across our entire backend application. This approach will enable us to specify which roles have access to which resources, thereby securing and protecting specific endpoints.

We will create separate packages for each user role. This strategy will facilitate the easy implementation of new features for specific roles and will ensure a well-structured backend application.

We use the same root URL across each package. For example, all resource endpoints in the admin package start with /admin. These root endpoints can only be accessed by users with the corresponding role.

We will be working with the following roles and packages:
- Admin
- Teacher
- Student

## Consequences

Access control can be efficiently managed through the use of role-specific root URLs. This means that each user role is associated with a unique root URL, and access privileges are determined based on this association.

This decision will enable us to implement a secure and scalable access control system for our application.

The most significant challenge will be the implementation of the access control, which will require us to work closely with our security handler (SpringBoot Security).