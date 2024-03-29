
# ***REST API проксирование запросов***

## Описание проекта

*Проксирование запросов от клиента на:* https://jsonplaceholder.typicode.com/

Реализовано обработчики(GET,POST,PUT,DELETE), которые проксируют запросы
+ /api/posts/**
+ /api/users/**
+ /api/albums/**

Реализована базовая авторизация, ролевая модель доступа к данным (ROLE_ADMIN,ROLE_POSTS,ROLE_USER,ROLE_ALBUMS)
+ ROLE_ADMIN - имеет доступ ко всем обработчикам
+ ROLE_POSTS - имет доступ к /api/posts/**
+ ROLE_USER - имеет доступ к /api/users/**
+ ROLE_ALBUMS - имеет доступ к + /api/albums/**

Реализована аторизация для нового пользователя по адресу **/api/registration** c ролью по умолчанию ROLE_USER

Авторизация пользователь **/api/auth**


Логирование запросов пользователь в файле logs-debug.log

Данные о пользователе хранятся в базе данных PostgreSQL, пароль в зашифрованном в виде. 
Для авторизации и проверки прав доступа используется JWT/JWS токен.


## Запуск
```
docker compose up
```
Поднимутся контейнеры и занесутся дефолтные значения в таблицы

Username |Password | Role |
--- | --- | --- |
user | 100 | ROLE_USER |
 admin | 100 | ROLE_ADMIN | 
 posts | 100 | ROLE_POSTS |
 albums | 100 | ROLE_ALBUMS |

## Стек

+ Java 17
+ Spring Boot
+ Spring Data
+ Spring Security
+ Spring AOP
+ PostgeSQL

  


