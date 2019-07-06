## Introduction
This is the backend design for the wizardspace.

## Requirements
Write an application that shows time in different timezones

* User must be able to create an account and log in.
* When logged in, a user can see, edit and delete apps he created.
* Implement at least three roles with different permission levels: a regular user would only be able to CRUD on their owned records, a user manager would be able to CRUD users, and an admin would be able to CRUD all records and users.
* When a timezone is entered, each entry has a Name, Name of the city in timezone, the difference to GMT time.
* When displayed, each entry shows the current time in that timezone and the difference between the browser’s time.
* Filter by names.
* REST API. Make it possible to perform all user actions via the API, including authentication 
* In any case, you should be able to explain how a REST API works and demonstrate that by creating functional tests that use the REST Layer directly. Please be prepared to use REST clients like Postman, cURL, etc. for this purpose.
* If it’s a web application, it must be a single-page application. All actions need to be done client side using AJAX, refreshing the page is not acceptable.
* Functional UI/UX design is needed. You are not required to create a unique design, however, do follow best practices to make the project as functional as possible.
* Bonus: unit and e2e tests.

## Tech stack
* We'll use Angular7 for front-end.
* We'll use AWS Lambda for backend API implementation.
* We'll use AWS API gateway as lambda front.
* We'll use DynamoDB as our backend data store.
## APIs
### createUser
Request:
```json
{
    user: User
}
```

Response:
```json
{
}
```

```json
User: {
    email: String,
    password: String,
    name: String,
    role: String,
    accountStatus: enum [PENDING_CONFIRMATION, ACTIVE, DELETED],
    creationTimestamp: epoch,
    lastUpdatedTimestamp: epoch
}
```

Restrictions: Captcha, email confirmation

### getUserById
Request:
```json
{
    userId: User,
    authID: String
}
```

Response:
```json
{
    user: User,
    paginationHanle: String
}
```

Restrictions: Already logged in user

### getAllUsers
Request:
```json
{
    userId: User,
    authID: String,
    paginationHandle: String
}
```

Response:
```json
{
    users: List<User>
}
```

Restrictions:  User manager/Admin only

### updateUser
Request:
```json
{
    user: User,
    editorID: String,
    authID: String
}
```

Response:
```json
{
    user: User
}
```

Restrictions: Already logged in user, or a user manager/admin

### deleteUser
Not required. We'll use updateUser for this.

### login
Request:
```json
{
    email: String,
    password: String
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

### createTimezone
Request:
```json
{
    timezoneEntity: TimezoneEntity,
    authId: String
}
```

Response:
```json
{
    timezoneEntity: TimezoneEntity
}
```
TimezoneEntity:
```json
{
    name: String,
    city: String,
    timezone: String,
    authorID: String,
    creationTimestamp: epoch,
    lastUpdatedTimestamp: epoch
}
```

Restrictions: Already logged in user, or an admin
### updateTimezone
Request:
```json
{
    timezoneEntity: TimezoneEntity,
    editorId: String,
    originalTimezoneName: String,
    authID: String
}
```

Response:
```json
{
}

Restrictions: Already logged in user, or an admin

```
### deleteTimezone
Not explicitly implemented. Instead use UpdateTimezone API and mark status as DELETED.

Restrictions: Already logged in user, or an admin

### getTimezonesByUser
Request:
```json
{
    userID: String,
    authId: String,
    paginationHandle: String
}
```
Response:
```json
{
    timezoneEntities: List<TimezoneEntity>,
    paginationHandle: String
}
```

### getAllTimezones
Request:
```json
{
    userID: String,
    authId: String,
    paginationHandle: String
}
```
Response:
```json
{
    timezoneEntities: List<TimezoneEntity>,
    paginationHandle: String
}
```

Restrictions: Already logged in user, or an admin

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

### Table: TimezoneEntity
```json
{
    name: String, // Sort key
    city: String,
    timezone: String,
    authorId: String, // PrimaryKey
    status: enum [ACTIVE, DELETED],
    creationTimestamp: epoch,
    lastUpdatedTimestamp: epoch,
    lastUpdatedBy: String
}
```
## Requirement mapping

#### => User must be able to create an account and log in.
* Create Account: createUser API
* Log in: login API, set browser cookie
* Log out: remove browser cookie

#### => When logged in, a user can see, edit and delete timezones he entered.
* See (list) all timezones: getTimezonesByUser API
* Edit / update timezone: updateTimezone API
* Delete timezone: deleteTimezone API

#### => Implement at least three roles with different permission levels: a regular user would only be able to CRUD on their owned records, a user manager would be able to CRUD users, and an admin would be able to CRUD all records and users.
* [Regular user] CRUD timzones: createTimezone, getTimezonesByUser, updateTimezone, deleteTimezone APIs
* [User manager] All capabilities of a regular user + CRUD users: createUser, getUserById, updateUser, deleteUser APIs
* [Admin] All capabilities of a regular user + All capabilities of a user manager + CRUD on any timezoneEntity for any user + Upgrade user to manager with upateUser API
#### => When a timezone is entered, each entry has a Name, Name of the city in timezone, the difference to GMT time.
* In backend it'll be stored as exact GMT time, in frontend it will be referred as the difference.

#### => When displayed, each entry shows the current time in that timezone and the difference between the browser’s time.
* Front-end implementation

#### => Filter by names.
* LisFilter by name of timezon

#### => REST API. Make it possible to perform all user actions via the API, including authentication
* We'll implement above as REST APIs.

#### => In any case, you should be able to explain how a REST API works and demonstrate that by creating functional tests that use the REST Layer directly. Please be prepared to use REST clients like Postman, cURL, etc. for this purpose.
* FTs to be added as part of implementation. Postman can be used for testing the APIs.

#### => If it’s a web application, it must be a single-page application. All actions need to be done client side using AJAX, refreshing the page is not acceptable.
* We'll build Angular SPA.

#### => Functional UI/UX design is needed. You are not required to create a unique design, however, do follow best practices to make the project as functional as possible.
* UI will be built, and the best practices will be followed.

#### => Bonus: unit and e2e tests.
* Part of implementation

## Out of scope
* Captcha checks
* DynamoDB column name shortening for efficient storage

## Issues and TODOs
* A user manager can also update an admin user account. This behavior should be fixed.
* User already exists check.
* What if the name of a timezone is changed?
* Don't overwrite existing timezone during creation.
* Transform DynamoDBUserClient to UserDAL, so that activity is agnostic of which storage it is dealing with.
* DynamoDB client should be injected through guice.
* Don't expose internal stack trace to presentation layer.
* Introduce a preprocess step and move some constructor logic there
* Security: An auth token created should be expire-able. Currently if someone aquire an auth token of a user, they get unlimited access to their account.
* Using java String.hashCode() method for generating hash may be insecure.

## REST API Manual testing
* Create User: https://ibmx0vv8m9.execute-api.us-east-1.amazonaws.com/prod/timezone-app?api=CreateUser&name=sanjayv&email=hello&password=asdf

## Deployment
* Build with gradle: "gradle build". This will generate a zip file, e.g. build/distributions/Backend-1.0-SNAPSHOT.zip
* Update the zip file to lambda function through the console.

In the future we'll have a pipeline for deployment.