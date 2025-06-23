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

* The purpose of our Android app will be able to let users play the daily crossword puzzle as well 
* as previous ones.


### Intended users and user stories

* Intended user 1: A person who likes to do crosswords in their free time instead of playing games
* on their phone as well as being competitive so they can see how they match up against others.
* User story intended user: As a crossword enthusiast, I want to generate and solve new puzzles 
* daily so that I can challenge myself and climb the leaderboard.


### Functionality

* The user will be able to click a button that will generate a randomized crossword.
* The user will log in with their email through a third party authentification service.
* The user will be able to play the daily crossword for that day as well as past daily puzzles
* There is an auto check that will happen with each letter implemented so after each letter the cloud
* will send back to see if there is a match
* A change of background color for correct or incorrect letters/words.
* The user can resume their recent game state if the game was not completed before leaving to menu.
* To initialize the game for the user it will load an initial game state that consists of the puzzle 
* structure(puzzle id) and the clues in a grid map to the side of the game
* The clues will be invisible to the user until they click on the corresponding grid in the puzzle
* and then only that clue will become visible to them to avoid cheating
* A user can not have more than daily puzzle of the day game open at one time
* There will be a check feature that the user can use to see if the corresping letters in their puzzle are correct



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
* dictionary word api


### Stretch goals and possible enhancements 

* Create animations for when the user gets a word in the crossword correct or finishes the game
* Leaderboard implementation
* Once the user completes a crossword, if done in the top 5 least amount of time have their
* username displayed in the leaderboard.
* There is a score that is based on the end time subtracting the initial time
* If user inputs a correct or incorrect word there will be an audio queue that activates.
* Add an animation for a correct inputted answer or/and when the user finishes the game
* Have user be able to delete their username from the leaderboard if they wanted to 
* Add multiple different themes so it gives variety to our crossword outside of just one theme
* Save an initial game state for offline play
* Add a point system to the crossword puzzle (maybe penalize or increase depending on input tries)
* Have a user choose the size of crossword generated from a few different choices for each theme

