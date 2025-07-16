# DAO Operations Checklist

<!-- Complete this checklist for each entity -->

## Entity name: {_Replace this placeholder (along with the enclosing braces) with the entity name._}

## Attributes

{_Replace this placeholder (along with the enclosing braces) with a bullet list of the attributes you current think will be needed for this entity._}

## Data-access object (DAO) operations

### Updating model data

At certain moments of your app's operation, methods declared in the DAO (and subsequently implemented by Room) will be invoked to update the persistent data represented by instances of the entity class. 

For example, the moment that the user completes entry of a new note in the `EditFragment` of the Notes app, we insert an instance of `Note` into the database. In other examples, we might need to insert several instances at once, taking them from an array or an *`Iterable`*. Similarly, in some applications, we might need to update data that's already been written to the database; this would correspond to a DAO method annotated with `@Update`.

Below these instructions, use the checkboxes below to indicate the insert, update and deletion operations that will be needed for the given entity. Mark an included item by _replacing_ the space between the square brackets with an X character.

#### Insert

* [ ] Insert a single instance into the database at a time.
* [ ] Insert multiple instances into the database at once.

#### Update

* [ ] Write changes to the database for a single instance at a time.
* [ ] Write changes to the database for multiple instances at once.
    
#### Delete 

* [ ] Delete a single instance from the database at a time.
* [ ] Delete multiple instances from the database at once.
    
### Reading/querying model data

Your DAO will also need to declare methods that read data from the database, providing that (usually via a repository) to the viewmodel. This will usually be returned as `LiveData` or as a value passed along a ReactiveX `Single` or `Maybe` reactive stream.

If you think you will have multiple queries that return a single instance, and/or multiple queries that return multiple instances, copy and pase the bullet items below as many times as necessary.

* [ ] Return a single instance, selected by {_replace this the name(s) of the attribute(s) that will be used to filter for a single instance of the entity_}.

* [ ] Return multiple instances, selected by {_replace this the name of the attribute(s) that will be used to filter for the desired instances of the entity_}, and orded by {_replace this with the name(s) of the column(s) that will be used to sort the results_}.
