# VNE Techwear — Backend (Spring Boot)

Базовый REST API для интернет‑магазина VNE (techwear).  
Фронтенд (Next.js) может забирать данные по HTTP.

## Функционал

- **GET `/products`** — список товаров (`id`, `name`, `price`, `category`)  
  Параметры поиска: `?category=pants` и/или `?q=tee`
- **GET `/products/{id}`** — полный товар (`id`, `name`, `description`, `price`, `category`, `sizes`)
- **POST `/products`** — создание товара (валидация: `name`, `price`, `category` обязательны)
- **DELETE `/products/{id}`** — удаление товара

База данных: **H2** (файл `./data/vne_shop`). Предзаполнена минимальными данными через `data.sql`.

## Быстрый старт (локально)

Требуется: **JDK 21**, **Maven 3.9+**

```bash
mvn spring-boot:run
```

Откроется на `http://localhost:8080`  
**Swagger UI**: `http://localhost:8080/swagger-ui.html`  
**OpenAPI JSON**: `http://localhost:8080/v3/api-docs`  

Консоль H2: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/vne_shop`)

### Примеры запросов

```bash
# Список всех товаров
curl http://localhost:8080/products

# Поиск по категории и названию
curl "http://localhost:8080/products?category=pants&q=cargo"

# Получить один товар
curl http://localhost:8080/products/1

# Создать товар
curl -X POST http://localhost:8080/products   -H "Content-Type: application/json"   -d '{
        "name":"VNE Hoodie",
        "description":"Heavyweight fleece hoodie",
        "price":89.90,
        "category":"tops",
        "sizes":["S","M","L","XL"]
      }'

# Удалить товар
curl -X DELETE http://localhost:8080/products/3
```

## Запуск через Docker

Требуется: **Docker**

```bash
# Сборка образа
docker build -t vne-techwear-api:0.1.0 .

# Запуск контейнера
docker run --rm -p 8080:8080 -v "$(pwd)/data:/app/data" --name vne-api vne-techwear-api:0.1.0
```

Данные будут храниться в локальной папке `./data` (примонтирована в контейнер).

## Структура проекта

```
vne-techwear-api
├── Dockerfile
├── pom.xml
├── src
│   └── main
│       ├── java/com/vne/shop
│       │   ├── VneShopApplication.java
│       │   ├── common/
│       │   │   ├── GlobalExceptionHandler.java
│       │   │   └── NotFoundException.java
│       │   └── product/
│       │       ├── Product.java
│       │       ├── ProductRepository.java
│       │       ├── ProductService.java
│       │       ├── ProductController.java
│       │       └── dto/
│       │           ├── ProductCreateRequest.java
│       │           └── ProductListItemDTO.java
│       └── resources
│           ├── application.yml
│           └── data.sql
└── README.md
```

## Требования задания (чек‑лист)

- [x] REST API для каталога товаров
- [x] Валидация входных данных (обязательные поля, цена > 0)
- [x] Поиск по `category` и `q` (название)
- [x] Dockerfile для локального запуска
- [x] Инструкция по запуску (README)

## Лицензия

MIT
