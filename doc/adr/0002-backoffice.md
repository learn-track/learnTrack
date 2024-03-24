# 2. Backoffice

Date: 2024-03-24

## Staus

Accepted

## Context

We need a backoffice tool for administrative work like add admins or schools.

### Option 1: Self made react app

**Pros:**
- More control
- Fully customizable 

**Cons:**
- Higher Maintenance
- More work 

### Option 2: Budibase

**Pros:**
- SSO 
- Nice design
- Looks easy to get started

**Cons:** 
- Currently no version control

### Option 3: Appsmith

**Pros:**
- Already familiar
- Git version control
- Easy rest API integration 

**Cons:** 
- Sometimes confusing
- SSO only in paid plans

## Decision

We go with option 3 Appsmith as we already know how it works and easy get started. 
Version Control with git makes it a lot easier to develop locally and then pull it onto the server. 

## Consequences 

We acknowledge that budibase would be a nice tool to maybe use in the future. 
But because of time restrictions we will go with Appmsith from which we know how it works and is the favorite of the community.