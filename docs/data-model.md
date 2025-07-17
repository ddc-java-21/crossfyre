---
title: Data Model
description: "UML class diagram, entity-relationship diagram, and DDL."
order: 30
---

{% include ddc-abbreviations.md %}

## Page contents
{:.no_toc:}

- ToC
{:toc}

## UML class diagram

[![CrossFyre UML Class Diagram](img/CrossFyre-UML.svg)](pdf/CrossFyre-UML.pdf)

## ERD entity diagram

[![CrossFyre ERD Entity Diagram](img/CrossFyre-ERD.svg)](pdf/CrossFyre-ERD.pdf)

## Data Definition Language code

{% include linked-file.md file="sql/ddl-server.sql" type="sql" %}


## Implementation


### Entity classes

- [`User`](https://github.com/ddc-java-21/crossfyre/blob/main/server/src/main/java/edu/cnm/deepdive/crossfyre/model/entity/User.java)
- [`Puzzle`](https://github.com/ddc-java-21/crossfyre/blob/main/server/src/main/java/edu/cnm/deepdive/crossfyre/model/entity/Puzzle.java)
- [`PuzzleWord`](https://github.com/ddc-java-21/crossfyre/blob/main/server/src/main/java/edu/cnm/deepdive/crossfyre/model/entity/PuzzleWord.java)
- [`UserPuzzle`](https://github.com/ddc-java-21/crossfyre/blob/main/server/src/main/java/edu/cnm/deepdive/crossfyre/model/entity/UserPuzzle.java)
- [`Guess`](https://github.com/ddc-java-21/crossfyre/blob/main/server/src/main/java/edu/cnm/deepdive/crossfyre/model/entity/Guess.java)


### Repository Classes

- [`UserRepository`](https://github.com/ddc-java-21/crossfyre/blob/c405eaffd00715d02b785b7b48946ccb49a3c6d7/server/src/main/java/edu/cnm/deepdive/crossfyre/service/dao/UserRepository.java)
- [`PuzzleRepository`](https://github.com/ddc-java-21/crossfyre/blob/cb016ba49b204a210a6aa7bb9293da38a1e08e2d/server/src/main/java/edu/cnm/deepdive/crossfyre/service/dao/PuzzleRepository.java)
- [`UserPuzzleRepository`](https://github.com/ddc-java-21/crossfyre/blob/cb016ba49b204a210a6aa7bb9293da38a1e08e2d/server/src/main/java/edu/cnm/deepdive/crossfyre/service/dao/UserPuzzleRepository.java)


### Controller Classes

- [`UserController`](https://github.com/ddc-java-21/crossfyre/blob/main/server/src/main/java/edu/cnm/deepdive/crossfyre/controller/UserController.java)


### Service Classes

- [`UserService`](https://github.com/ddc-java-21/crossfyre/blob/main/server/src/main/java/edu/cnm/deepdive/crossfyre/service/UserService.java)
- [`AbstractUserService`](https://github.com/ddc-java-21/crossfyre/blob/main/server/src/main/java/edu/cnm/deepdive/crossfyre/service/AbstractUserService.java)



