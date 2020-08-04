Original App Design Project 
===

# ALLERGUIDE

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This app will allow users to find restaurants, bakeries, grocery stores, etc. that have foods/options for people with food allergies. For example, people who cannot eat gluten can specify that gluten is their allergy, and the app will show restaurants and other places that offer options that are gluten free. Users can rate the places they go to, upload pictures of the foods they eat and add comments to let other people know of their experience at a certain place. 

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Lifestyle
- **Mobile:** maps, camera, and location.
- **Story:** There are a lot of people with food allergies and restrictions, and it can be very difficult for these people to find places where they will be able to have some good food while also avoiding their allergens. The goal of this app is to help these people find places where they can have a good meal that will accomodate their restrictions.
- **Market:** This app is good for anyone who has or knows someone with dietary restrictions.
- **Habit:** Whenever people want to go out to eat, or buy food, they can use this app. Users can provide feedback about the restaurants/eateries they have tried and their experience with them.
- **Scope:** This app will contain many of the elements that have been learned throughout the first three weeks of the internship, plus additional elements that I will explore. I think it will be pretty challenging to implement, and the product is pretty well defined thus far.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- [x]  user login/sign up
- [x]  set/select allergies/food restrictions
- [x]  set location
- [x]  use Google Maps to find local restaurants + filter out ones that offer foods the user can eat
- [x]  allow users to tag restaurants as being friendly for a specific allergy, and show those tagged restaurants at the top of the list of restaurants in the home view.
- [x]  reviews page
    - [x]  Detailed view of a restaurant
    - [x]  user can read/write reviews of a restaurant
    - [x]  user can take pictures of stuff they've eaten/their experience
- [x]  Clean UI
- [x]  User can add a restaurant to their favorites tab
- [x]  user can see posts they have made on their profile page
- [x]  User can share their review of a restaurant

**Optional Nice-to-have Stories**

- [ ] View a map view of restaurants and see their location
- [ ] Users can take a picture of a food item in grocery stores and be told if the food was processed in a factory that handles other foods that contain their allergen/if the food itself contains their allergens
- [x] User can add and change their profile picture
- [x] User can upload an image from their camera roll for either a review or their profile picture
- [x] User can search for restaurants in a different location that meet their requirements


### 2. Screen Archetypes

* login 
   * user login/signup
   * select food restrictions
   * set locations
* Stream
   * List of places and preview information
       * rating, name, picture
   * List of favorite places
* Detail
    * name + description of restaurant
    * reviews
* Creation
    * add a review
    * add a tag
* Profile
    * Profile picture
    * posts
* Settings
    * change profile picture
    * change allergies/restrictions
* Search
    * User can search for restaurants in a location different from their current location

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Restaurant list
* Favorites
* Search
* Profile

**Flow Navigation** (Screen to Screen)

* Restaurants list
   * Details view
* Favorites
* Search
    * Restaurants list
        * Details View
* Profile
    * Settings View

## Wireframes
![](https://i.imgur.com/zRlhdKL.jpg)


## Schema 

### Models

Restaurant

| Property     |       Type       |       Description       |
|  --------    |     --------     |       --------          |
| Restaurant name| String         | Name of Restaurant      |
| Image        | File             | Images of food/place    |
| Description  | String           | Description of place    |
| Rating       | Number           | Rating of place out of 5|
| Location     | String           | Location of restaurant  |

Favorites

| Property     |       Type       |       Description       |
|  --------    |     --------     |       --------          |
| Restaurant name| String         | Name of Restaurant      |
| Restaurant   | Pointer to Restaurant| The restaurant that is the favorite|
| User         | Pointer to User  | The user who made the favorite  |


User

| Property     |       Type       |       Description       |
|  --------    |     --------     |       --------          |
| Username     | String           | Name of user            |
| Allergies    | List of Strings  | List of allergies of user|
| Password     | String           | User's password for app    |
| Profile Picture| File           | User's profile picture   |
 
 
Review/Post

| Property     |       Type       |       Description       |
|  --------    |     --------     |       --------          |
| Restaurant   | String           | Name of Restaurant      |
| User         | Pointer to User  | Current user making review|
| Review text  | String           | User review of place    |
| Review image | File             | Image from user of experience|
| Rating       | Number           | Rating of place out of 5|
| createdAt    | DateTime         | Time a review was made  |
| location     | String           | Location of restaurant  |

Allergies

| Property     |       Type       |       Description       |
|  --------    |     --------     |       --------          |
| Vegan        | Boolean          | Restriction is Vegan    |
| Vegetarian   | Boolean          | Restriction is Vegetarian|
| Gluten Free  | Boolean          | Restriction is Gluten Free|
| Lactose Free | Boolean          | Restriction is Lactose Free|
| User         | Pointer to User  | The user who has these restrictions|

Tags

| Property     |       Type       |       Description       |
|  --------    |     --------     |       --------          |
| Vegan        | Boolean          | Restriction is Vegan    |
| Vegetarian   | Boolean          | Restriction is Vegetarian|
| Gluten Free  | Boolean          | Restriction is Gluten Free|
| Lactose Free | Boolean          | Restriction is Lactose Free|
| restaurantName | String         | The restaurant who has these tags|


                                  

### Networking
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


## App Expectations

- [x]  Your app has multiple views
    - Main view, details view, favorites view, profile, settings
- [x] Your app interacts with a database (e.g. Parse)
    - will use Parse to hold user data
- [x] You can log in/log out of your app as a user
- [x] You can sign up with a new user profile
- [x] Somewhere in your app you can use the camera to take a picture and do something with the picture (e.g. take a photo and share it to a feed, or take a photo and set a user’s profile picture)
    - User can take photos of food/experience at restaurant and share in the reviews
- [x] Your app integrates with a SDK (e.g. Google Maps SDK, Facebook SDK)
    - Google Maps SDK
- [x] Your app contains at least one more complex algorithm (talk over this with your manager)
    - Filtering by Tags
- [x] Your app uses gesture recognizers (e.g. double tap to like, e.g. pinch to scale)
    - Double tap to add to favorites
- [x] Your app uses an animation (doesn’t have to be fancy) (e.g. fade in/out, e.g. animating a view growing and shrinking)
    - Transitions between activities and fragments
- [x] Your app incorporates an external library to add visual polish
    - Glide to show pictures
    - Material Design
