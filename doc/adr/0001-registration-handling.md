# 1. Registration Handling

Date: 2024-03-23

## Status 

Accepted

## Context

Different types of user have to be created for the application. 
This ADR discusses how we handle the creation of different user types.

The user types are:
- Student
- Teacher
- Admin (School leader)

### Teachers

#### Option 1:

Admins create user for teacher

**Pros:**
- No additional work for the teacher 

**Cons:**
- Teacher can only be assigned to one school

#### Option 2:

Allow teachers to create an account by them self.

**Pros:**
- Every teacher can create an account for them self
- Teachers can use the same account for different school 
- Teachers can use there teaching material for every class in every school
- Admins just have to assign a teacher to their school and a class without creating one

**Cons:**
- Every one can create an account
- We have to implement a registration only for teachers

### Students

#### Option 1:

Admins create user for student

**Pros:**
- Admin has the full control over the students

**Cons:** 
- Additional Work for admin

#### Option 2:

Students can create their own account by a code which automatically assigns them to a class.

**Pros:** 
- No additional work for admins
- Students can use their preferred mail address

**Cons:**
- Every one with a valid code can join a class

## Decision

### Admin

We will create admins as well as schools with a backoffice tool.

**Pros:**
- Close cooperation's with schools
- More control over who uses our platform 

**Cons:**
- More administrative work for us
- May stop schools from just trying it out

### Teachers 

We will go with option 2 as the benefits for the teachers, like using his class material for different school, overweight the additional work.
We can also implement functionalities to delete teacher accounts if they are not valid.

### Students

We will go with option 1 for the moment to leave the full control by the admin user.




