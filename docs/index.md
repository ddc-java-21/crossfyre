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

* The purpose of our Android app will be able to generate a general crossword puzzle
* from scratch. There will be a dual leaderboard system that consists of a daily leaderboard and an
* all time leaderboard. The leaderboards will be loaded with the top 10 times of 
* people who have beat the crossword puzzle in the least amount of time.


### Intended users and user stories

* Intended user 1: A person who likes to do crosswords in their free time instead of playing games
* on their phone as well as being competitive so they can see how they match up against others.
* User story intended user: As a crossword enthusiast, I want to generate and solve new puzzles 
* daily so that I can challenge myself and climb the leaderboard.


### Functionality

* The user will be able to click a button that will generate a randomized crossword.
* The user will log in with their email through a third party authentification service.
* Once the user completes a crossword, if done in the top 5 least amount of time have their 
* username displayed in the leaderboard.
* The user will be able to click to generate a couple different subject themes for the crossword.
* If user inputs a correct or incorrect word there will be an audio queue that activates.
* There is an auto check option that will update on real time input if the letter is correct with
* a change of background color for correct or incorrect.
* The user can resume their recent game state if the game was not completed before leaving to menu.
* To initialize the game it will load an initial game state so it is accessible offline for only
* that current game.
* Generates a puzzle of the day and has features to look back at previous puzzles of the day in a 
* widget format(calendar)


### Persistent data

* The information regarding the clues and key words
* The generator for the crossword puzzle
* The display names of the people who have made it onto the leaderboard


### Device/external services

* google authentication provider
* crossword puzzle generator
* crossword library api/database


### Stretch goals and possible enhancements 

* Create animations for when the user gets a word in the crossword correct or finishes the game
* Add an animation for a correct inputted answer or/and when the user finishes the game
* Have user be able to delete their username from the leaderboard if they wanted to 
* Add multiple different themes so it gives variety to our crossword outside of just one theme
* Save an initial game state for offline play
* Add a point system to the crossword puzzle (maybe penalize or increase depending on input tries)
* Have a user choose the size of crossword generated from a few different choices for each theme

