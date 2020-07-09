Original App Design Project - README Template
===

# APP_NAME_HERE

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This app will allow users to find restaurants, bakeries, grocery stores, etc. that have foods/options for people with food allergies. For example, people who cannot eat gluten can specify that gluten is their allergy, and the app will show restaurants and other places that offer options that are gluten free. Users can rate the places they go to, upload picutres of the foods they eat and add comments to let other people know of their experience at a certain place. 

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Lifestyle
- **Mobile:** maps, camera, and location.
- **Story:** There are a lot of people with food allergies and restrictions, and it can be very difficult for these people to find places where they will be able to have some good food while also avoiding their allergens.
- **Market:** This app is good for anyone who has or knows someone with dietary restrictions.
- **Habit:** Whenever people want to go out to eat, or buy food, they can use this app. Users can provide feedback about the restaurants/eateries they have tried and their experience with them.
- **Scope:** This app will contain many of the elements that have been learned throughout the course. I think it will be pretty challenging to implement, but not impossible. The product is pretty well defined thus far.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* user login
* set/select allergies/food restrictions
* set location
* use Google Maps to find local restaurants + filter out ones that offer foods the user can eat
* reviews page
    * Detailed view of a restaurant
    * user can read/write reviews of a restaurant
    * user can take pictures of stuff they've eaten/their experience
* Clean UI

**Optional Nice-to-have Stories**

* View a map view of restaurants and see their location
* Users can take a picture of a food item in grocery stores and be told if the food was processed in a factory that handles other foods that contain their allergen/if the food itself contains their allergens
* User can add a restaurant to their favorites tab
* User can add and change their profile picture

### 2. Screen Archetypes

* login 
   * user login
   * select food restrictions
   * set locations
* Stream
   * Google maps with a list of places and previw information
       * rating, name, picture
   * List of favorite places
* Detail
    * name + description of restaurant
    * reviews
* Creation
    * add a review
* Profile
    * Profile picture
    * posts
* Settings
    * change location
    * change profile picture
    * change allergies/restrictions
* Search
    * User can search for restaurants in a location different from their current location

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Restaurant list
* Favorites
* Searh
* Profile

**Flow Navigation** (Screen to Screen)

* Restaurants list
   * Details view
* Favorites
   * Details view
* Search
    * Restaurants list
        * Details View
* Profile
    * Details View

## Wireframes
![](https://i.imgur.com/DnGllNv.jpg)

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 

### Models


| Property     |       Type       |       Description       |
|  --------    |     --------     |       --------          |
| Restaurant   | String           | Name of Restaurant      |
| User         | Pointer to User  | Current user looking for restaurants|
| Image        | File             | Images of food/place    |
| Description  | String           | Description of place    |
| Review text  | String           | User review of place    |
| Review image | File             | Image from user of experience|
| Rating       | Number           | Rating of place out of 5|
| createdAt    | DateTime         | Time a review was made  |
| Location     | JSON Object      | Location of restaurant  |
                                  

### Networking
- [Add list of network requests by screen ]
- Home Feed Screen
    * (Read/GET) Query restaurants list
- Details Page
    * (Create/POST) Create a new review
    * (Read/GET) Query reviews
- Favorites Page
    * (Read/GET) Query restaurant favorites
- Profile
    * (Read/GET) Query user object
- Search
    * (Read/GET) Query restaurants list matching search location
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
