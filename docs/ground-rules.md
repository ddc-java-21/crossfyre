---
title: Ground Rules
description: "Conventions for branch creation, naming, merging, and deletion"
order: 10
---

{% include ddc-abbreviations.md %}

## Page contents
{:.no_toc:}

- ToC
{:toc}

## Branch Creation

* A new remote (non-local) branch will be created from the last commit of main when we are working on a new feature for CrossFyre
* A new local branch will be created from a remote branch when a team member is working on that remote branch's feature 

## Branch Naming

* A remote branch will be named in the lower-spinal-case convention
* A remote branch will be named after the feature of the app that has been agreed to be separated from main to have fluid workflow on that feature separate from the main branch
* A local branch that is a branch-off of a remote branch will be named in lower-spinal-case as well as their initials added to the end(i.e. docs-jd for John Doe)

## Branch Merging

* A team member will merge their local branch into the features-based branch when they have completed or finished a section of the feature with working code
* To merge into main, team member1 will let their other team members know they are making a pull request into main so that there can be an overview on the pull request to main before it is merged
* A feature branch (service) will be the only branch type merged into the main branch, as no branch with the personal naming convention (service-jd) will be allowed to be pulled into the main branch via a pull request

## Branch Deletion

* Main and Docs branches will never be deleted
* The team member's personal feature branch can be deleted once the team member has merged their updated, personal feature branch into the remote feature branch(i.e. service-jd into service)
* The feature branch can be deleted once the team has approved the merge of the said branch into the main branch and code is working as intended(i.e. service into main)
