
# \* Wizard Space *

# Introduction
To create a web app store.

# Requirements
* Anyone should be a able to create apps

# P0
* The homepage lists the apps by category. The ranking is static.
* Users can login and sign-up.
* Users have 12 levels. 4 - Regular user, 11 - admin, 12 - super admin. We'll start with levels 4 and 12 only.
* A template can be in draft mode, and will be versioned. Templates have a live editor.
* Template executor will be a separate component.

# P1
* Templates can be forked.
* Templates can have multiple views.
* The ranking of apps on homepage is dynamic.

# Storage
* App templates to be stored in S3. Template metadata to be stored in DynamoDB
* User data to be stored in DynamoDB.
* Session data to be stored in DynamoDB

## Template
* States: DRAFT, PENDING_REVIEW, PUBLISHED, DELETED
* privacy: PUBLIC\_OPEN_SOURCE, PUBLIC, PRIVATE
* TemplateId will be UUID.
* VersionId will be epoch date (time of publish). Draft will be version 9999999999.

### APIs
* getAllTemplates(paginationHandle)
* getTemplatesByUser(paginationHandle)
* getTemplateData(templateId, authorId, versionId)

### Table: templates (dynamoDB)
```javascript
{
    authorId: string,
    templateId: string,
    status: string,
    privacy: string,
    templatePath: string,
    name: string,
    description: string,
    liveVersion: number,
    created: epoch,
    lastUpdated: epoch
}
```

### Table: template-versions (dynamoDB)
```javascript
{
    templateId: string,
    created: epoch,
    versionId: number
    authorId: string,
}
```

### Bucket: templates (S3)
```javascript
{
    templateId1: {
        version1: {
            template.html
            icon-200x200.png
        },
    }
}
```

## User
### Table: users (dynamoDB)
``` javascript
{
}
```
