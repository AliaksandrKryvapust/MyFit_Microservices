# MyFit_Microservices

Due to the growing popularity of a healthy lifestyle, the main functions of the application have been created, which allows you to keep a food diary, as well as keep statistics.

The main functions of the application
1. Registration/Authorization
2. Accounting for calories from food
3. Keeping a food and physical activity diary
4. Viewing statistics
5. Preparation of the food menu

Registration
To register, the user must enter:
* email
* Password
* Name
* Height (profile)
* Weight (profile)
* Age (profile)

Authorization
To log in, the user must enter:
* email
* Password

Counting calories from food
1. Product selection:
* Choose a product
* Specify the weight
2. Multiple products can be selected
3. Information is displayed (calories, proteins, fats, carbohydrates) for each product

Food Diary
Available after authorization.
1. Nutrition
* The user fills in the products that he ate during the day
2. User's weight
* The user indicates his weight (necessary for statistics)

Statistics
Available after authorization.
1. Viewing statistics (graph X = days, Y=...) per week/month :
* calories consumed
* user's weight

Power menu
Available after authorization.
1. Choose:
* day of the week
* products/dishes
* specify the weight
2. All data specified by the user is saved

# Presentation

For demonstration, queries from the Postman collection from the \postman folder can be run.
The my_fit_droplet collection is intended for droplet requests on the digitalocean server (the server is temporarily stopped, demonstration on request).

# Used tools/technologies

* Microservice architecture
* Spring (Security, Boot)
* Hibernate
* PostgreSQL
* MongoDB
* Tomcat
* Docker
* Quartz
* Amazon S3
* Apache POI
* Junit
* Mockito
* Maven
* GIT
* HTTP
* REST
* Postman
* DigitalOcean Developer Cloud



