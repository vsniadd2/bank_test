# Система управления банковскими картами

REST API приложение для управления банковскими картами, разработанное на Spring Boot с использованием JWT аутентификации, шифрования данных и ролевого доступа.

## Содержание

- [Описание проекта](#описание-проекта)
- [Технологический стек](#технологический-стек)
- [Функциональность](#функциональность)
- [Архитектура](#архитектура)
- [API Endpoints](#api-endpoints)
- [Установка и запуск](#установка-и-запуск)
- [Конфигурация](#конфигурация)
- [Безопасность](#безопасность)
- [Известные проблемы](#известные-проблемы)

## Описание проекта

Система управления банковскими картами предоставляет REST API для:
- Регистрации и аутентификации пользователей
- Создания и управления банковскими картами
- Переводов между картами
- Административного управления пользователями и картами
- Просмотра баланса и истории операций

### Основные возможности

**Для пользователей (USER):**
- Регистрация и вход в систему
- Создание новых банковских карт
- Просмотр своих карт с пагинацией
- Переводы между своими картами
- Пополнение карт
- Просмотр баланса
- Запрос на блокировку карты

**Для администраторов (ADMIN):**
- Просмотр всех карт в системе
- Управление пользователями (блокировка/разблокировка)
- Создание карт для пользователей
- Одобрение запросов на блокировку карт
- Обновление CVV кодов карт

## Технологический стек

- **Java 21** - язык программирования
- **Spring Boot 3.5.6** - фреймворк
- **Spring Security** - безопасность и аутентификация
- **Spring Data JPA** - работа с базой данных
- **PostgreSQL 15** - база данных
- **Flyway 11.0.0** - миграции базы данных
- **JWT (jjwt 0.12.6)** - токены аутентификации
- **Lombok** - уменьшение boilerplate кода
- **MapStruct 1.6.0** - маппинг объектов
- **SpringDoc OpenAPI 2.6.0** - документация API
- **Docker Compose** - контейнеризация

## Архитектура

Проект следует принципам многослойной архитектуры:

```
src/main/java/com/example/bankcards/
├── config/          # Конфигурационные классы
│   ├── SecurityConfiguration.java
│   ├── OpenApiConfig.java
│   └── AppConfig.java
├── controller/      # REST контроллеры
│   ├── AuthenticationController.java
│   ├── CardController.java
│   └── AdminController.java
├── service/         # Бизнес-логика
│   ├── AuthenticationService.java
│   ├── CardService.java
│   └── AdminService.java
├── repository/      # Доступ к данным
│   ├── UserRepository.java
│   ├── CardRepository.java
│   └── TokenRepository.java
├── entity/          # JPA сущности
│   ├── User.java
│   ├── Card.java
│   └── Token.java
├── dto/             # Data Transfer Objects
│   ├── auth/
│   ├── card/
│   ├── transaction/
│   └── user/
├── security/        # Компоненты безопасности
│   ├── JwtService.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
├── exception/       # Обработка исключений
│   └── GlobalExceptionHandler.java
└── util/            # Утилиты
    └── CardEncryptionUtil.java
```

## API Endpoints

### Аутентификация (`/api/v1/auth`)

| Метод | Endpoint | Описание | Доступ |
|-------|----------|----------|--------|
| POST | `/registration` | Регистрация нового пользователя | Публичный |
| POST | `/authenticate` | Вход в систему | Публичный |
| POST | `/refresh-token` | Обновление токена | Публичный |

### Управление картами (`/api/v1/card`)

| Метод | Endpoint | Описание | Доступ |
|-------|----------|----------|--------|
| POST | `/` | Создание новой карты | USER |
| GET | `/` | Получение списка карт (с пагинацией) | USER |
| POST | `/transfer` | Перевод между картами | USER |
| POST | `/deposit` | Пополнение карты | USER |
| GET | `/deposit` | Получение баланса карты | USER |
| POST | `/block-card` | Запрос на блокировку карты | USER |

### Административные операции (`/api/v1/admin`)

| Метод | Endpoint | Описание | Доступ |
|-------|----------|----------|--------|
| GET | `/all-cards` | Получение всех карт | ADMIN |
| POST | `/cards/{cardId}/approve-block` | Одобрение блокировки карты | ADMIN |
| GET | `/users` | Получение списка пользователей | ADMIN |
| GET | `/users/{userId}` | Получение пользователя по ID | ADMIN |
| PATCH | `/users/{userId}/block` | Блокировка пользователя | ADMIN |
| PATCH | `/users/{userId}/unblock` | Разблокировка пользователя | ADMIN |
| POST | `/users/{userId}/cards` | Создание карты для пользователя | ADMIN |
| PATCH | `/users/{userId}/cards/{cardId}/cvv` | Обновление CVV карты | ADMIN |

## Установка и запуск

### Требования

- Java 21 или выше
- Maven 3.6+
- Docker и Docker Compose (для PostgreSQL)

### Шаги установки

1. **Клонирование репозитория**
```bash
git clone <repository-url>
cd bank_rest
```

2. **Запуск PostgreSQL через Docker Compose**
```bash
docker-compose up -d
```

3. **Применение миграций базы данных**

Миграции Flyway применяются автоматически при запуске приложения.

4. **Сборка проекта**
```bash
mvn clean install
```

5. **Запуск приложения**
```bash
mvn spring-boot:run
```

Приложение будет доступно по адресу: `http://localhost:8080`

### Конфигурация базы данных

По умолчанию используется PostgreSQL на порту `15432`:
- **Host:** `localhost`
- **Port:** `15432`
- **Database:** `effective_db`
- **Username:** `postgres`
- **Password:** `4242`

Настройки можно изменить в `src/main/resources/application.yml`.

## Конфигурация

Основные настройки находятся в `src/main/resources/application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:15432/effective_db
    username: postgres
    password: 4242
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
```

### JWT настройки

```yaml
application:
  security:
    jwt:
      secret-key: <your-secret-key>
      expiration: 1800000  # 30 минут
      refresh-token:
        expiration: 604800000  # 7 дней
```

### Шифрование карт

```yaml
application:
  encryption:
    card-secret: <your-encryption-key>
```

## Безопасность

### Аутентификация

- Используется JWT (JSON Web Tokens) для аутентификации
- Access токены действительны 30 минут
- Refresh токены действительны 7 дней
- Токены хранятся в базе данных для возможности отзыва

### Шифрование данных

- Номера карт шифруются перед сохранением в БД
- CVV коды шифруются
- В ответах API номера карт маскируются: `**** **** **** 1234`

### Ролевой доступ

- **ROLE_USER** - доступ к своим картам и операциям
- **ROLE_ADMIN** - полный доступ ко всем операциям

### CORS

Настроен CORS для работы с фронтенд приложениями:
- Разрешены все origin patterns
- Поддерживаются все основные HTTP методы
- Разрешены все заголовки

## Известные проблемы

### Проблема с Swagger UI

При попытке открыть Swagger UI (`http://localhost:8080/swagger-ui.html`) возникает ошибка:

```
GET http://localhost:8080/v3/api-docs 500 (Internal Server Error)
```

![Ошибка Swagger UI](img.png)

**Описание проблемы:**

При запросе к `/v3/api-docs` сервер возвращает ошибку 500 (Internal Server Error). Это происходит из-за того, что SpringDoc OpenAPI не может корректно сгенерировать документацию API.

**Возможные причины:**

1. **Проблемы с обработкой сложных типов возврата:**
   - Контроллеры используют `CompletableFuture<ResponseEntity<?>>` для асинхронных операций
   - Использование wildcard типов (`ResponseEntity<?>`) может вызывать проблемы при генерации схемы

2. **Проблемы с инициализацией бинов:**
   - SpringDoc может пытаться инициализировать сервисы при сканировании контроллеров
   - Зависимости от репозиториев и базы данных могут вызывать ошибки

3. **Конфигурация SpringDoc:**
   - Настройки SpringDoc находятся в Java конфигурации (`OpenApiConfig.java`)
   - Возможно, требуется дополнительная настройка для обработки асинхронных операций

**Текущее состояние:**

- Конфигурация OpenAPI создана в `OpenApiConfig.java`
- Swagger UI доступен по адресу `/swagger-ui.html`
- Генерация документации API (`/v3/api-docs`) возвращает ошибку 500

**Временное решение:**

Для тестирования API можно использовать:
- Postman
- cURL
- HTTP клиенты (IntelliJ IDEA HTTP Client, VS Code REST Client)
- Прямые HTTP запросы через браузер (для GET методов)

**Планы по исправлению:**

1. Исследовать логи приложения для точного определения причины ошибки
2. Добавить аннотации OpenAPI к контроллерам для явного описания API
3. Рассмотреть возможность использования статической OpenAPI спецификации вместо автоматической генерации
4. Проверить совместимость версий SpringDoc OpenAPI с Spring Boot 3.5.6

## Структура базы данных

### Таблицы

- **users** - пользователи системы
- **user_roles** - роли пользователей
- **cards** - банковские карты
- **token** - JWT токены

Миграции находятся в `src/main/resources/db/migration/`:
- `V1__Init_schema.sql` - создание основных таблиц
- `V2__Add_primary_role_to_users.sql` - добавление ролей
- `V3__Increase_token_length.sql` - увеличение длины токенов
- `V4__Add_cvv_column_to_cards.sql` - добавление CVV колонки

## Тестирование

### Примеры запросов

**Регистрация пользователя:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/registration \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Аутентификация:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Создание карты (требует JWT токен):**
```bash
curl -X POST http://localhost:8080/api/v1/card \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json"
```

## Дополнительная документация

- [Документация контроллеров](src/main/java/com/example/bankcards/controller/README_Controller.md)
- [Документация сервисов](src/main/java/com/example/bankcards/service/README_Service.md)
- [Документация безопасности](src/main/java/com/example/bankcards/security/README_Security.md)
- [Документация миграций](src/main/resources/db/migration/README_Migration.md)

## Авторы

Разработано в рамках учебного проекта по созданию REST API для управления банковскими картами.

## Лицензия

Этот проект создан в образовательных целях.
