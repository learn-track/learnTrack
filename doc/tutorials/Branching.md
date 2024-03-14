# Branching

This documents how we use Branches and commits in this Project.

## Best Practice

### Brach naming

The general Rule here is that for every Jira-Ticket you create a new Branch.

For clear structure and follow the following naming strategie.

First up we put Branches in directories, which are:
- **feat/** for feature Branches
- **fix/** for Bugfixes or similar
- **chores/** for dependency updates

Followed by the directory we have ticket. Jira tickets follow a easy naming strategie **"Project-Number"** in our case the project shortcut is **LERN** which leads to **"LEARN-12"** as an example. 

All of this put together will look something like this: **feat/LERN-14**

### Commit message 

When you want commit your changes on the branch you need to add a commit message, describing what you changed. 
To keep the commits and branches together the message follows a similar format to the branch. 

In the beginning you put the commit type wicht is essentially the brach directory. This is then followed again by the ticket and then your message. 
Example: **feat(LERN-14): added branching tutorial**

