
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