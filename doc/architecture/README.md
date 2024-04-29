# learnTrack Architecture Documentation
Architecture documentation based on [arc42](https://www.innoq.com/en/blog/brief-introduction-to-arc42/)

## Introduction / Goals

The learnTrack is a LMS (Learn Management System) for swiss schools.

### Quality Goals

The main quality goals of this project include:

- Secure Login
- Easy to use for teachers, students and principals

## Constraints

Coding: There is only one Developer with experience with the tech stack we use

## Context and Scope

### System Context


## Solution Strategy

The learnTrack dev team develops this application and uses an open source technology stack, which will consist of a conventional three tier architecture 
with frontend (a single-page application), backend and relational database.

### ER Diagram

This maybe extended in the future with new features

``` mermaid
erDiagram
    user {
        UUID id
        string firstname
        string middlename
        string lastname
        string e-mail
        string password
        string user_role
        date birthdate
        datetime created
        datetime updated
    }
    
    user_grade {
        UUID user_id
        UUID grade_id
    }
    
    user_school {
        UUID user_id
        UUID school_id
    }
    
    grade {
        UUID id
        string name
        UUID school_id
        datetime created
        datetime updated
    }
    
    school {
        UUID id
        string name
        string address 
        string city
        number postcode
        datetime created
        datetime updated
    }
   
    subject {
        UUID id
        string name 
        UUID grade_id
        datetime created
        datetime updated
    }
   
    
    user ||--|{ user_grade: contains
    grade ||--|{ user_grade: contains
    user ||--|{ user_school: contains
    school ||--|{ user_school: contains
    school ||--|{ grade: contains
    subject }|--|| grade: contains
```

## Building Block View

## Runtime View

## Deployment View

## Crosscutting Concepts

## Architectural Decisions

See [ADR Folder](../adr)

## Quality Requirements

See [Quality Goals](#quality-goals) for some major goals
driving the architecture. This section may describe quality scenarios
in the future.

## Risk & Technical Debt


## Glossary

| Term | Description             |
|------|-------------------------|
| LMS  | Learn Management System |

