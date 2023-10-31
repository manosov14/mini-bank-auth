# Проект mini-bank

## User story
1. Я как пользователь хочу стать клиентом банка (регистрация)
2. Я как клиент, хочу открыть счет
3. Я как пользователь хочу пополнить счет под %
4. Я как пользователь хочу видеть все свои счета

## ТЗ
- [x] Фичи реализуются 2 CRUD сервисами на выбранном ЯП
  - Сервис 1 имеет REST API интерфейс работы с пользователями 
    - Create – создание пользователя ФИО, username, email, пароль. 
    - Read – поиск пользователя в базе.
    - Update – обновление какой-либо информации пользователя. 
    - Delete – удаление пользователя из базы.
  - Сервис 2 имеет REST API интерфейс работы со счетами (https://github.com/manosov14/mini-bank-accounts )
    - Create - создание счета пользователя 20 знаков
    - Read - получения счета, списка счетов пользователя
    - Update - обновления информации о счете
    - Delete - удаление счета
- [x] Каждая фича закрывается feature toggle.
- [x] Информация о пользователях и счетах хранится в базе/базах PostgreSQL.
- [x] Код сервисов хранится в GIT.
- [x] Разработка ведется по GitFlow.
- [x] Код бизнес логики сервисов покрывается Unit тестами.
- [x] Код сервисов заворачивается в Docker образы.
- [x] Сборка кода и деплой осуществляется автоматизированным DevOps Pipeline на выбранный хостинг.
- [ ] Деплой новых версий необходимо реализовать через Blue Green deployment.
- [x] Обращение к сервисам осуществляется через прикладной балансировщик.


# Реализация
## Swagger
host://swagger-ui/index.htm
## CI
https://github.com/manosov14/mini-bank-auth/blob/master/.github/workflows/CI.yml
## CD
https://github.com/manosov14/mini-bank-auth/blob/master/.github/workflows/CD.yml

## Feature toggles
Реализованы через  через `spring.profiles (feature-toggles)`
Значения настраиваются в `./resources/application-feature-toggles.yml`

## API
| Method | API (/api/v1) | FT | Auth (JWT) | Description |
| --- | --- | --- | --- | --- |
| `POST` | `/user` | `features.userCanCreate` | \+ | Create user |
| `PUT` | `/user/{id}` | `features.userCanUpdate` | \+ | Update user |
| `GET` | `/user` | `features` | \+ | Get user |
| `DELETE` | `/user/{id}` | `features.userCanDelete` | \+ | Delete user |
| `POST` | `/login` | `features.userCanLogin` | \+ | Login user. jwt release |
| `POST` | `/logout` | `features.userCanDelete` | \+ | Logo user. jwt kill |