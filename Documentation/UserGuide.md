# User Guide: Running the Program Locally and Reproducing Results

This user guide provides step-by-step instructions on how to set up, run the program locally, and reproduce the results.

## Prerequisites

Before you begin, ensure you have the following installed:
- [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) - Ensure you download the correct version for your operating system.
- [Git](https://git-scm.com/downloads) - Required for version control and source code management.
- [Node.js and NPM](https://nodejs.org/en/download/) - Necessary for running and managing JavaScript forntend runtime environments.
- [MySQL Workbench and MySQL Community Server](https://dev.mysql.com/downloads/) - For database management and development.
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) - Recommended IDE for editing and running the source code.
- [Visual Studio Code](https://code.visualstudio.com/download) - A lightweight but powerful source code editor for running the front end.


## Run the Backend
Follow these steps to set up and run the backend of the application:

1. **Clone the Repository**:
   Start by cloning the repository to your local machine using Git.

2. **Set Up the Database**:
   - Open MySQL Workbench and create a new schema named `fridge_app`.
   - Run the SQL script located at `Database\fridge_app.sql` to set up your database tables and initial data.

3. **Configure the Project**:
   - Open the `\Backend` directory as a project in IntelliJ IDEA.
   - Build the project to resolve dependencies and prepare for execution.

4. **Update Configuration Files**:
   - Navigate to `Backend/src/main/resources/application.properties`.
   - Fill in your SQL server password and AWS keys to ensure all external services are correctly configured.

5. **Run the Application**:
   - Execute the file `Backend/src/main/java/com/end/fridge/FridgeEndApplication.java` to start the backend server.
   - The server should now be running, typically accessible via `http://localhost:8080` or another port defined in your `application.properties`.



## Run the frontend 
Follow these steps to set up and run the frontend of the application:
1. Open the `\frontend` directory in Visual Studio Code or your preferred code editor.
2. Open a terminal within the code editor or use your system's command line.
3. Execute the following commands to install dependencies and start the frontend server:
   ```bash
   npm install       # Install project dependencies
   npm run dev       # Start the development server


## Getting Started

### Creating an Account
First, sign up for an account. Having an account will help you have a virtual refrigerator of your own and enjoy personalized recipe service.

<img width="1511" alt="Screenshot 2024-04-30 at 9 37 52 AM" src="https://github.com/LouisLu00/CS370/assets/84982166/c883da5d-5033-48cb-839f-39184d6ab005">

### Logging in
Once you have an account, just log in!

<img width="1512" alt="Screenshot 2024-04-30 at 9 36 06 AM" src="https://github.com/LouisLu00/CS370/assets/84982166/3cf2e8c8-2669-4c66-ba8f-629a7bb551d3">


## Features

### Fridge
This is your virtual refrigerator! Here you can store your ingredients, keep track of their quantities and shelf life. Our refrigerator will alert you when an ingredient is about to expire to avoid waste. Also, our fridge has different filters to make it easy for you to manage your ingredients. You can choose to add ingredients to your virtual fridge by manually entering ingredients, uploading pictures of ingredients, or snapshotting them.

#### Manually upload
![99bc8cd8cf9eeb2e75a7de0cb5fcf9ec](https://github.com/LouisLu00/CS370/assets/84982166/967aafca-6e4a-4b0a-a709-e9f98e3aabd5)
Here you can add ingredients by inputting their names, also can edit quantity and expiration date.

We will introduce the image-uploading/snapshot methods in the later section.

#### Your Fridge
Right now, you have your virtual fridge that records all of your ingredients!

<img width="1512" alt="Screenshot 2024-04-30 at 10 23 40 AM" src="https://github.com/LouisLu00/CS370/assets/84982166/8143b198-59db-42e3-ae16-3d04c8e463a7">


### Automatic Recipe Generator
Based on the ingredients in your fridge, we will generate recipes for you. Our recipes include a picture of the dish, the ingredients used, the time to make it, and the steps to make it. You can 'like' or 'dislike' the current recipe, which helps the system to personalize recommendations based on your preferences.
<img width="1512" alt="Screenshot 2024-04-30 at 9 44 04 PM" src="https://github.com/LouisLu00/CS370/assets/84982166/0e198f7a-9681-4ff6-baa6-2a9a4c2a90d6">

<img width="1512" alt="Screenshot 2024-04-30 at 10 20 02 AM" src="https://github.com/LouisLu00/CS370/assets/84982166/8605cfda-976e-4c9e-a468-6cea22271574">


### Personalized Recipe Recommendation
We will recommend personalized recipes for you based on your history of choices as well as preferences.

<img width="1512" alt="Screenshot 2024-04-30 at 10 19 45 AM" src="https://github.com/LouisLu00/CS370/assets/84982166/70063f19-0672-4354-ab47-f773ad84881c">

![438a157497d9bb92301967f58f202cfa](https://github.com/LouisLu00/CS370/assets/84982166/519da38d-56cf-4cbd-9926-661ed57b6bf9)



### Image Recognition

#### Image Uploading
The first way: you can choose to upload a picture of your ingredients and we will recognize it automatically.

<img width="1512" alt="746a58030892153cbc10bd987c1431ab" src="https://github.com/LouisLu00/CS370/assets/84982166/31012728-a015-46a3-972e-f103090b455a">

<img width="1510" alt="fa829488eaf238d245d5e919c21a87d0" src="https://github.com/LouisLu00/CS370/assets/84982166/6098487b-32cd-41fa-9f60-3b5639326c2c">

#### Snapshot
You can also choose to use the snapshot option to allow us to recognize your ingredients.

<img width="1511" alt="Screenshot 2024-04-30 at 10 27 58 AM" src="https://github.com/LouisLu00/CS370/assets/84982166/3c82cd43-7e2f-477e-af3d-eda0ca7218b9">

<img width="1512" alt="Screenshot 2024-04-30 at 10 28 13 AM" src="https://github.com/LouisLu00/CS370/assets/84982166/9f72c460-3713-41d9-8fe4-1d8c9b51cdf6">







