---

# *My-shop*

Описание проекта
-
Приложение представляет собой склад товаров, с которым можно взаимодействовать добавляя товары и присваивать им различны
категории.



Инструкция по запуску:
-

1. Чтобы запустить приложение локально нужна запущенная бд Postgres. С помощью pgAdmin или других UI для 
взаимодействия с бд создайте базу данных со след параметрами:
    1) 
        * POSTGRES_USER = postgres
        * POSTGRES_PASSWORD = admin
        * POSTGRES_DB = my-shop
2. Так же возможен запуск проекта в docker.
3. Команда "docker-compose up" запускает приложение с его бд в отдельных контейнерах вв docker-compose
4. Для проверки работоспособности приложения предусмотрены постан тесты:
    - [postman-tests](https://github.com/RomanLuperkal/Test-task/blob/develop/postman-tests/my-shop.json)
5.Так же для приложения имеется сформированый файл спецификации swagger
    - [swagger OAS](https://github.com/RomanLuperkal/Test-task/blob/develop/swagger/my-shop-OAS.json)
