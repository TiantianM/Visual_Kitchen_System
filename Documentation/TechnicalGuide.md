## Project Architecture
<img width="935" alt="Screenshot 2024-04-30 at 12 20 03 PM" src="https://github.com/LouisLu00/CS370/assets/84982166/963acb05-8006-4616-b841-ba52246c2ecc">


## Front-end 
Our project's frontend is built with Vue 3.2.0.It interacts with the backend services to manage user sessions, ingredient inventories, and recipe suggestions. We manages user sessions using browser storage and supports user interactions like logging out, ingredient sorting, and navigation to different parts of the application such as recipe creation and ingredient editing.

### Fridge Module
- Allows users to store ingredients in the fridge.
- Supports three upload methods: manual upload, image analysis upload, and photo upload.
- Manages ingredient expiration dates and other related information.
### Recommendation Module
- Generates recipe recommendations based on the ingredients stored in the fridge.

### Project Structure

- **dist**: Contains compiled assets and the main HTML file for deployment.
  - **assets**: Compiled CSS and JavaScript files.
- **public**: Static assets like icons.
- **src**: Source code directory.
  - **components**: Contains Vue components.
  - **pages**: Contains Vue components for different pages.
  - **router**: Vue router configuration.
  - **customFetch.js**: Custom fetch function for API requests.
  - **main.js**: Entry point of the application.


Key functionalities are implemented through Vue components and services:

- `front/src`: Frontend Java Code
  - `front/src/components/`: Reusable Vue components used across different parts of the application: 
    - `front/src/components/grid_ingredient.vue`: Displays ingredients in a grid layout on various pages
    - `front/src/components/top_button.vue`: Provides a consistently styled button used for primary actions across the site
  - `front/src/pages`: Vue components that serve as entire pages, each corresponding to a route in the application:
    - `front/src/pages/camera_result.vue`: Shows the results from the image recognition process.
    - `front/src/pages/creator_recipe.vue`: Allows users to create recipes by entering details and uploading images.
    - `front/src/pages/edit_ingredient.vue`: Provides functionality to edit details of an existing ingredient.
    - `front/src/pages/index.vue`: Serves as the main landing page or dashboard of the application.
    - `front/src/pages/login.vue`: Manages user authentication and login interface.
    - `front/src/pages/recipe_info.vue`: Displays detailed information about a recipe.
    - `front/src/pages/recommend.vue`: Recommends recipes based on the ingredients available in the user's fridge.
  - `front/src/router`: Manages the routing for the application, defining paths to the different pages. 

## Back-end
Our project uses the Java SpringBoot framework for the backend. All the backend codes are stored in: `./Backend` following the Spring Boot structure. The backend basically covers the core application functions using different objects. 

The 'User' object stores users' information and work togerther with JWT authentication to allow users logging in and out. The 'recipe' and 'ingredient' objects store various features of specific ingredients and recipes. The fridge objects keeps track of the different ngreidents in a user's fridge. And the recommendation result object calculates the recommended recipe based on the available ingredients in the fridge. Lastly, the preference history object collects user feedback (like or dislike for a specific recommended recipe).

- `Backend/src/main`: Backend Java Code
  -  `Backend/src/main/resources/application.properties`: Defines the necessary properties to run the program locally, such as AWS API key and local SQL server passwords
  -  `Backend/src/main/java/com/end/fridge/FridgeEndApplication.java`: Main function to run the whole application backend 
  -  `Backend/src/main/java/com/end/fridge/Controller`: Defines how the backend handles requests from the client sude
  -  `Backend/src/main/java/com/end/fridge/domain`: Defines the structure of four entities: Users, Fridges, Ingredients, Recipes, Recommendation Results and Preference History
  -  `Backend/src/main/java/com/end/fridge/repository`: Abstracts the data access layer, providing direct interaction with the database
  -  `Backend/src/main/java/com/end/fridge/service`: Builds the backend operation logic for user registration, recipe recommendation, and recording preference history
  -  `Backend/src/main/java/com/end/fridge/util`: Uses JWT authentication to verify the identity of a user by using a signed token, supporting the user logging and logging out function
  
## Database
- `fridge_app.sql`: Keep track of the current database structure and data inside
- `Scrapping Results`: Save pickle file of the scarp results

## Scraping
The scraping code is stored in: `/Scraping`
Currently, our project used  `https://www.food.com/` to get the recipe information. The soruce code are stored at  `Scraping/Recipe_Scrap`
- `config.ini`: Stores the configuration parameters for the scraping process, including the URL, page range, and output file name.
- `food_scraper.py`: Contains the scraping methods.
- `recipe.py`: Defines the data structure of the `Recipe` object.
- `file_storage.py`: Serializes the scraped data into a pickle object, which will later be imported into a PostgreSQL database.

Currently, our project uses `https://foodb.ca/` to get ingredient information. The soruce code are stored at  `Scraping/Ingredients_Scrap`
- `config.ini`: Configuration for scraping, including base URL, page range, and output file.
- `ingredient_scraper.py`: Web scraping logic to fetch and parse ingredient details from web pages, utilizing `requests` and `BeautifulSoup`.
- `ingredient.py`: Data structure for `Ingredient` objects, detailing name, scientific name, food group, food subgroup, and image URL.
- `file_storage.py`: File management for serializing and deserializing `Ingredient` objects to/from a pickle file for persistent data storage.

