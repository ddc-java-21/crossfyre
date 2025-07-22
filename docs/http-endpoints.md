---
title: Data Model
description: "HTTP Endpoint Methods and Request Information"
order: 40
---

{% include ddc-abbreviations.md %}

## Page contents
{:.no_toc:}

- ToC
{:toc}

## HTTP Endpoints:

* /crossfyre/puzzles/{date} (test/dev)
  * Authentication: Google Sign-in (Bearer Token)
  * Methods:
    * GET:
      * Inputs:
        * Path variables: Instant date
        * Query string variables: n/a
        * Request headers: n/a
        * Request body: n/a
      * Outputs (Response Body):
        * UserPuzzle (MediaType: application/json)
* /crossfyre/puzzles/{date}/puzzleWords (test/dev)
  * Authentication: Google Sign-in (Bearer Token)
  * Methods:
    * GET:
      * Inputs:
        * Path variables: Instant date
        * Query string variables: n/a
        * Request headers: n/a
        * Request body: n/a
      * Outputs (Response Body):
        * Puzzle (MediaType: application/json)
    * POST:
      * Inputs:
        * Path variables: Instant date
        * Query string variables: n/a
        * Request headers: n/a
        * Request body: PuzzleWord (MediaType: application/json)
      * Outputs (Response Body):
        * Puzzle (MediaType: application/json)
* /crossfyre/puzzles/{date}/puzzleWords/{puzzleWordKey}
  * Authentication: Google Sign-in (Bearer Token)
  * Methods:
    * GET:
      * Inputs:
        * Path variables: Instant date, UUID puzzleWordKey
        * Query string variables: n/a
        * Request headers: n/a
        * Request body: n/a
      * Outputs (Response body):
        * PuzzleWord (MediaType: application/json)
* /crossfyre/userPuzzles/{date}
  * Authentication: Google Sign-in (Bearer Token)
  * Methods:
    * GET:
      * Inputs:
        * Path variables: Instant date
        * Query string variables: n/a
        * Request headers: n/a
        * Request body: n/a
      * Outputs (Response body):
        * UserPuzzle (MediaType: application/json)
* /crossfyre/users/me
  * Authentication: Google Sign-in (Bearer Token)
  * Methods:
    * GET:
      * Inputs:
        * Path variables: n/a
        * Query string variables: n/a
        * Request headers: n/a
        * Request body: n/a
      * Outputs (Response body):
        * User (MediaType: application/json)
    * PUT:
      * Inputs:
        * Path variables: n/a
        * Query string variables: n/a
        * Request headers: n/a
        * Request body: n/a
      * Outputs (Response body):
        * User (MediaType: application/json)
* /crossfyre/users/{key}
  * Authentication: Google Sign-in (Bearer Token)
  * Methods:
    * GET:
      * Inputs:
        * Path variables: UUID externalKey
        * Query string variables: n/a
        * Request headers: n/a
        * Request body: n/a
      * Outputs (Response body):
        * User (MediaType: application/json)
* /crossfyre/userpuzzles/{date}/guesses
  * Authentication: Google Sign-in (Bearer Token)
  * Methods:
    * GET:
      * Inputs:
        * Path variables: Instant date
        * Query string variables: n/a
        * Request headers: n/a
        * Request body: n/a
      * Outputs (Response body):
        * List<Guess> (MediaType: application/json)
    * POST:
      * Inputs:
        * Path variables: Instant date
        * Query string variables: n/a
        * Request headers: n/a
        * Request body: Guess (MediaType: application/json)
      * Outputs:
        * UserPuzzle (MediaType: application/json)
* /crossfyre/userpuzzles/{date}/guesses/{guessKey}
  * Authentication: Google Sign-in (Bearer Token)
  * Methods:
    * GET:
      * Inputs:
        * Path variables: Instant date, UUID guessKey
        * Query string variables: n/a
        * Request headers: n/a
        * Request body: n/a
      * Outputs (Response body):
        * Guess (MediaType: application/json)


### Example Output:

 > GET http://localhost:38983/crossfyre/userpuzzles/2025-07-20T00:00:00Z
 > Authorization: Bearer ...
 
Response:

[Headers]

...

{% include linked-file.md file="misc/example-userpuzzle.json" type="sql" %}
