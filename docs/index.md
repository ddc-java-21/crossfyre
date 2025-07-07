---
title: Overview
description: "Summary of in-progress or completed project."
order: 0
---

{% include ddc-abbreviations.md %}

### Page contents
{:.no_toc}

- ToC
  {:toc}

### Summary

* CrossFyre is a crossword-puzzle generator app for Android that allows users to work on a daily
* generated cross userWord puzzle of the day. Its hallmark feature is a generated puzzled of the day 
* that creates a unique puzzle for a selected theme. It uses a public API (or APIs) to retrieve 
* content to serve as the content for the puzzles, allowing for unique puzzles to be created every 
* day. It tracks user puzzle history using an account login system. So whether looking for a 
* relaxing companion to a morning coffee, or feeling like expanding your vocabulary that day, this 
* app is anyone's go-to source for fun puzzle-solving madness.


### Intended users and user stories

* Intended user 1: As a person who likes to do small brain teasers in their free time at work,
* I play crosswords to help my brain ease off the stress caused at my job.
* Intended user 2: As someone who likes to learn new words when I don't have school in the summer,
* I play crosswords to help expand my vocabulary to help my articulation.


### Functionality

* The user will be able to click a button that will load the daily crossword puzzle.
* The user will log in with their email through a third party authentification service.
* The user will be able to play past daily puzzles that they choose up to an extent.
* There is an auto check that will happen with each letter implemented so after each letter the
* puzzle will be sent back to the cloud and it will send back a check to see if there is a match.
* A change of background color for correct or incorrect letters/words.
* The user can resume their recent game state if the game was not completed before leaving to menu.
* To initialize the game for the user it will load an initial game state that consists of the puzzle
* structure(puzzle id) and the clues in a grid map to the side of the game in a column format.
* The clues will be invisible to the user until they click on the corresponding grid in the puzzle.
* and then only that clue will become visible to the user to counteract cheating.
* A user can not have more than one daily puzzle of the day game open at one time.
* There will be a check feature that the user can use to see if the corresponding letters in their
* puzzle are matched anywhere.



### Persistent data

# Puzzle
* Puzzle id
* Grid layout
* Clue list(across, down)
* Word List
* DateTime updated and created

# User
* User ID
* profile settings
* progress data (ID's and timestamps of completion)

# Dictionary
* Active game words
* clues for active game words


### Device/external services

* google authentication provider
* crossword puzzle generator
* pattern matcher
* dictionary userWord api


### Stretch goals and possible enhancements

* Create animations for when the user gets a userWord in the crossword correct or finishes the game.
* Leaderboard implementation.
* Once the user completes a crossword, if done in the top 5 least amount of time have their
* username displayed in the leaderboard.
* There is a score that is based on the end time subtracted by the initial time.
* If user inputs a correct or incorrect userWord there will be an audio queue that activates when they
* do a check.
* Add an animation for a correct inputted answer and/or when the user finishes the game.
* Have user be able to delete their username from the leaderboard if they wanted to.
* Add multiple different themes so it gives variety to our crossword outside of just one theme.
* Add a point system to the crossword puzzle (maybe penalize or increase depending on input tries).
* Have a user choose the size of crossword generated from a few different choices for each theme.

