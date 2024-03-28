---

# *Java-explore-with-me*

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
        * POSTGRES_PASSWORD = admim
        * POSTGRES_DB = my-shop
2. Так же возможен запуск проекта в docker.
3. Команда "docker-compose up" запускает приложение с его бд в отдельных контейнерах вв docker-compose
4. Для проверки работоспособности приложения предусмотрены постан тесты:
    - [postman-tests](https://github.com/RomanLuperkal/java-explore-with-me/blob/main/postman/Explore%20With%20Me%20-%20Main%20service.json)
    - [ewm-statistic](https://github.com/RomanLuperkal/java-explore-with-me/blob/main/postman/Explore%20with%20me-%20API%20%D1%81%D1%82%D0%B0%D1%82%D0%B8%D1%81%D1%82%D0%B8%D0%BA%D0%B0.json)
    - [ewm-service-comments](https://github.com/RomanLuperkal/java-explore-with-me/blob/main/postman/feature.json)
