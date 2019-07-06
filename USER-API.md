## Introduction
This is the backend design for the wizardspace.

## Requirements
Write an application that shows time in different timezones

* User must be able to create an account and log in.
* When logged in, a user can see, edit and delete apps he created.
* Implement at least three roles with different permission levels:
> a regular user would only be able to CRUD on their owned apps, \
> a moderator would be able to CRUD any apps, \
> and an admin would be able to CRUD all records and users.

* REST API. Make it possible to perform all user actions via the API, including authentication 

## Tech stack
* We'll use Angular 8 for front-end.
* We'll use AWS Lambda for backend API implementation.
* We'll use AWS API gateway as lambda front.
* We'll use DynamoDB as our backend data store.

## APIs
### createUser
```json
Request: {
    path: /users,
    method: POST,
    body : {
        email: String,
        password: String,
        name: String
    }
}
```

Response:
```json
HTTP 200 OK
```



```json
User: {
    email: String,
    password: String, // md5 hash
    name: String,
    role: String,
    accountStatus: enum [PENDING_CONFIRMATION, ACTIVE, DELETED],
    creationTimestamp: epoch,
    lastUpdatedTimestamp: epoch
}
```

Future Restrictions: Captcha, email confirmation

### getUserById
Request:
```json
{
    path: /users/{id},
    method: GET,
    query-parameters: {
        authID: String
    }
}
```

Response:
```json
{
    user: User
}
```

Restrictions: Already logged in user

### getAllUsers
Request:
```json
{
    path: /users,
    method: GET,
    query-parameters: {
        requesterId: User,
        authID: String,
        paginationHandle: String
    },
}
```

Response:
```json
{
    users: List<User>
}
```

Restrictions:  Admin only

### updateUser
Request:
```json
{
    path: /users/{id},
    method: PATCH,
    body: {
        editorID: String,
        authID: String,
        attributes: [
            { name: String, value: String}
            ..
        ]
    }
}
```

Response:
```json
HTTP 200 OK
```

Restrictions: Already logged in user, or an admin

### deleteUser
Not required. We'll use updateUser for this.

### login
Request:
```json
{
    path: /session,
    method: POST,
    body: {
        email: String,
        password: String // md5 hash
    }
}
```

Response:
```json
{
    user: User,
    authID: String
}
```

Restrictions: Captcha

## DynamoDB schema
### Table: User
```json
{
    email: String, // PrimaryKey
    userId: String,
    password: String,
    name: String,
    accessLevel: String,
    accountStatus: enum [PENDING_CONFIRMATION, ACTIVE, DELETED],
    creationTimestamp: epoch, // sort key
    lastUpdatedTimestamp: epoch,
    lastUpdatedBy: String
}
```

## Future steps
* Captcha checks
* Temporal auth credentials
* Browser and IP based verification

## Deployment
* Build with gradle: "gradle build". This will generate a zip file, e.g. build/distributions/Backend-1.0-SNAPSHOT.zip
* Update the zip file to lambda function through the console.

In the future we'll have a pipeline for deployment.