---
title: Overview
description: "Summary of in-progress or completed project."
order: 0
---

{% include ddc-abbreviations.md %}

## Page contents

{:.no_toc}

- ToC
{:toc}

## Summary

  CrossFyre is a crossword-puzzle generator app for Android that allows users to work on a daily
generated cross word puzzle of a specific day. Its hallmark feature is its randomly generated puzzle
that creates a unique puzzle every day. It uses a couple public API's to retrieve words and
definitions to serve as the answer banks for the puzzles. By having a login system, it allows users
to leave and comeback to play a puzzle whenever they feel like it. So whether they're looking for a
brain-teaser with their morning coffee or feeling like expanding their vocabulary, CrossFyre is
anyone's go-to source for some puzzle-solving entertainment.

## Intended users and user stories

- A kid who wants a fun but challenging way to learn new words through quick, brain-teaser like
  games
  > As a kid out of school, I play my crossword game to teach myself new words while
  I am on summer break.

- An elder who wants to keep away their brain fog and play word games that they're used to can
  play our crosswords
  > As an elder, I know that it can be hard to remember things, so I sharpen my brain by playing
  CrossFyre as it lets me play a quick crossword game that I am familiar with the style of.

## Functionality

* If the user hasn't signed in or is currently signed out, they will be asked to sign in using their Google account.
* The app will load, and the user will be met with a pop-up to play that daily puzzle or sign out.
* If the user clicks sign out, it will go back to asking for their login, else it will load the daily puzzle.
* When the user inputs a letter, their character will be sent to the server, and it will be logged up until the user guess every letter correctly in the puzzle.
* The user can resume their puzzle of the day if their game was not completed, and it is still within the same day.
* The generation of the puzzle will happen a day before the puzzle will be used and when the calendar date changes then the in queue puzzle will be used and another puzzle will be generated and waiting to be used for the next day.
* The puzzle layout will depend on the day of the week as there will be seven different puzzle layouts of a 5x5 grid.
* A set of 10 words and 10 clues will be associated with each generation of a puzzle (for a 5x5 grid).
* The puzzle will auto-select the default starting position (one across).
* The hint associated with the selection will be displayed and labeled underneath the puzzle.
* The hints of any unselected words will not be visible until that word is selected to counteract cheating.

[//]: # (Finish persistent data)
## Persistent data

* PuzzleWord
  * Display name
  * OAuth2.0 identifier
  * Timestamp of first login to the app

* Puzzle
  * Task title
  * Task description
  * Timestamp of task creation
  * Assigned task date
  * Completion of a task

* UserPuzzle
  * Puzzle identifier
  * User identifier for puzzle of User
  * Timestamp of task creation
  * Timestamp of completion

* Guess
  * Note title
  * Note description
  * Timestamp of task creation
  * Optionally assigned note date

* User
  * Display name
  * OAuth2.0 identifier
  * Timestamp of first login to the app

## Device/external services

* Google cloud console
  * [`Service`](https://console.cloud.google.com)
  * Will hande user authentification for sign-in
  * This console will be used on the initial sign-in, and refreshing of the app if they don't sign out

* Datamuse API
  * [`Service`](https://www.datamuse.com/api)
  * Word finding query engine
  * Finds words that match a given set of constraints that are likely in a given context
  * The constraints can be on meaning, spelling, sound, and vocabulary in any combination

* Merriam-Webster Collegiate Dictionary
  * [`Service`](https://dictionaryapi.com/products/api-collegiate-dictionary)
  * Word and definition bank
  * Gives us over 225,000 words and precise definitions

## Stretch goals and possible enhancements

* Change the background color of the grid depending on whether the letter is correct or not
* The user can play previous puzzles of the day by selecting a date
* Create a LocalDate for each puzzle so that it is flexible depending on your timezone
* Create animations for when the user gets a word in the crossword correct and/or finishes the game.
* If user inputs a correct word there will be an audio queue when the animation would play.
* Leaderboard implementation.
  * Have user be able to delete their username from the leaderboard if they wanted to.
* Add multiple different themes.
* Add a point system to the crossword puzzle (maybe penalize or increase depending on input tries).
* Have a user choose a crossword size generated for a brand-new puzzle for personal use unrelated to puzzle of the day.

