---
title: HTTP Endpoints
description: "Documented RESTful endpoints for the CrossFyre server application."
order: 40
---

## Page contents
{:.no_toc}

- ToC
{:toc}

## Authentication

All endpoints require authentication via **Google Sign-In** using a **Bearer token** in the `Authorization` header.

---

## GET /crossfyre/puzzles/{date}

Returns a `UserPuzzle` for the given date.

- **Path Variable:** `date: Instant`

**Response**
```json
{
  "id": 123,
  "externalKey": "uuid",
  "created": "2025-07-22T00:00:00Z",
  "solved": null,
  "user": { "id": 1, "name": "Jane Doe" },
  "puzzle": { "id": 88, "title": "Sample Puzzle" },
  "guesses": []
}
```

---

## /crossfyre/puzzles/{date}/puzzleWords

### GET

Fetches the base puzzle for a specific date.

- **Path Variable:** `date: Instant`

**Response**
```json
{
  "id": 88,
  "title": "Sample Puzzle",
  "words": ["apple", "banana", "coconut"]
}
```

### POST

Adds a new `PuzzleWord` to the puzzle.

- **Path Variable:** `date: Instant`

**Request Body**
```json
{
  "word": "strawberry",
  "row": 3,
  "column": 5,
  "direction": "ACROSS"
}
```

**Response**
```json
{
  "id": 88,
  "title": "Sample Puzzle",
  "words": ["apple", "banana", "coconut", "strawberry"]
}
```

---

## GET /crossfyre/puzzles/{date}/puzzleWords/{puzzleWordKey}

Fetch a specific `PuzzleWord` by key.

- **Path Variables:** `date: Instant`, `puzzleWordKey: UUID`

**Response**
```json
{
  "word": "banana",
  "row": 2,
  "column": 1,
  "direction": "DOWN"
}
```

---

## GET /crossfyre/userPuzzles/{date}

Fetch a `UserPuzzle` object for the given date.

- **Path Variable:** `date: Instant`

**Response**
```json
{
  "id": 123,
  "externalKey": "uuid",
  "solved": null,
  "guesses": []
}
```

---

## GET /crossfyre/users/me

Get the currently authenticated user.

**Response**
```json
{
  "id": 1,
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

---

## PUT /crossfyre/users/me

Update the authenticated user.

**Response**
```json
{
  "id": 1,
  "name": "Jane A. Doe",
  "email": "jane@example.com"
}
```

---

## GET /crossfyre/users/{key}

Retrieve a user by external key.

- **Path Variable:** `key: UUID`

**Response**
```json
{
  "id": 2,
  "name": "John Smith",
  "email": "john@example.com"
}
```

---

## /crossfyre/userpuzzles/{date}/guesses

### GET

Fetch all guesses for a user’s puzzle.

- **Path Variable:** `date: Instant`

**Response**
```json
[
  {
    "id": 1,
    "row": 0,
    "column": 1,
    "guessChar": "A"
  },
  {
    "id": 2,
    "row": 0,
    "column": 2,
    "guessChar": "P"
  }
]
```

### POST

Submit a new guess.

- **Path Variable:** `date: Instant`

**Request Body**
```json
{
  "row": 1,
  "column": 3,
  "guessChar": "C"
}
```

**Response**
```json
{
  "id": 123,
  "solved": false,
  "guesses": [
    {
      "row": 1,
      "column": 3,
      "guessChar": "C"
    }
  ]
}
```

---

## GET /crossfyre/userpuzzles/{date}/guesses/{guessKey}

Fetch a specific `Guess` by key.

- **Path Variables:** `date: Instant`, `guessKey: UUID`

**Response**
```json
{
  "id": 3,
  "row": 2,
  "column": 4,
  "guessChar": "E"
}
```
