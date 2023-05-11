# National-Park-Finder-Android-Development
An Android Project that allows users to view a list of country parks in the United States, add them into their itinerary, update and itinerary details.

This application performs CRUD actions using National Park Service API (US), Retrofit and Firestore.

Tested on Pixel 3 API 33 using Android Studio.

<br>

## Functions:
- Searching National Parks by States in the United States
- Viewing National Parks Details
- Saving National Parks to Itinerary
- Viewing Itinerary List
- Updating Itinerary Details (i.e. Travel Date, Notes)
- Deleting National Parks from Itinerary

<br>

## Config
DB Config:
- Set up your own Firestore connection in the project directory
- Set up DB data for abbreviation of States Name for API calls
- Make necessary changes to fragments/FindParksFragment.kt

API Config:
- Set up your own API key to BASE_URL in networking/RetrofitInstance.kt

API Key Request Link: https://www.nps.gov/subjects/developer/get-started.htm

Refer to documentation upon National Park Service API calls.
