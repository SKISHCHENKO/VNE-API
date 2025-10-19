# VNE Techwear — Backend (Spring Boot)

Базовый REST API для интернет‑магазина VNE (techwear).  

## Функционал

1.Получение списка товаров  
 • Эндпоинт: GET /products  
 • Возвращает список всех товаров (id, название, цена, категория).  

- **GET `/products`** — список товаров (`id`, `name`, `price`, `category`)   
  Параметры поиска: `?category=pants` и/или `?q=tee`  

  2.Получение одного товара  
 • Эндпоинт: GET /products/:id  
 • Возвращает полную информацию о товаре (id, название, описание, цена, категория, размеры).  

- **GET `/products/{id}`** — полный товар (`id`, `name`, `description`, `price`, `category`, `sizes`)  

3.Добавление нового товара  
 • Эндпоинт: POST /products  
 • Принимает JSON с данными о товаре, сохраняет в базе.  
- **POST `/products`** — создание товара (валидация: `name`, `price`, `category` обязательны)  

4.Удаление товара  
 • Эндпоинт: DELETE /products/:id  
 • Удаляет товар по id.  

- **DELETE `/products/{id}`** — удаление товара  

База данных: **H2** (файл `./data/vne_shop`). Предзаполнена минимальными данными через `data.sql`.

## Быстрый старт (локально)

Требуется: **JDK 21**, **Maven 3.9+**

```bash
mvn clean package
mvn spring-boot:run
```

Откроется на `http://localhost:8080`  
**Swagger UI**: `http://localhost:8080/swagger-ui.html`  
**OpenAPI JSON**: `http://localhost:8080/v3/api-docs`  

Подключение (по умолчанию подключается автоматически):  
Консоль H2: `http://localhost:8080/h2-console` 
JDBC URL: `jdbc:h2:file:./data/vne_shop`  
логин: sa, пароль пустой  

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

## Примеры ответов

### 1) `GET /products` — список товаров

**200 OK**
```json
[
  { "id": 1, "name": "VNE Tee",          "price": 29.90,  "category": "tops" },
  { "id": 2, "name": "VNE Cargo Pants",  "price": 69.90,  "category": "pants" },
  { "id": 3, "name": "VNE Tech Jacket",  "price": 129.00, "category": "outerwear" }
]
```

#### Поиск: `GET /products?category=pants&q=cargo`
**200 OK**
```json
[
  { "id": 2, "name": "VNE Cargo Pants", "price": 69.90, "category": "pants" }
]
```

---

### 2) `GET /products/{id}` — один товар (полная карточка)

**200 OK**
```json
{
  "id": 1,
  "name": "VNE Tee",
  "description": "Classic heavyweight cotton tee",
  "price": 29.90,
  "category": "tops",
  "sizes": ["S", "M", "L", "XL"]
}
```

**404 Not Found** (если товара с таким id нет)
```json
{
    "error": "NOT_FOUND!",
    "message": "Товар 6 не найден"
}
```

---

### 3) `POST /products` — создание товара

**Пример тела запроса**
```json
{
  "name": "VNE Hoodie",
  "description": "Heavyweight fleece hoodie",
  "price": 89.90,
  "category": "tops",
  "sizes": ["S","M","L","XL"]
}
```

**201 Created**
```
Location: /products/4
Content-Type: application/json
```
```json
{
  "id": 4,
  "name": "VNE Hoodie",
  "description": "Heavyweight fleece hoodie",
  "price": 89.90,
  "category": "tops",
  "sizes": ["S","M","L","XL"]
}
```

**400 Bad Request** (ошибки валидации, например пустое имя/категория или цена ≤ 0)
```json
{
    "error": "BAD_REQUEST",
    "message": "Ошбика валидации!"
}
```

---

### 4) `DELETE /products/{id}` — удаление товара

**204 No Content** (успех, без тела ответа)

**404 Not Found** (если товара с таким id нет)
```json
{
    "error": "NOT_FOUND!",
    "message": "Товар 6 не найден"
}
```

---

## Коды ответов по эндпоинтам (кратко)

- `GET /products` — `200`
- `GET /products/{id}` — `200`, `404`
- `POST /products` — `201`, `400`
- `DELETE /products/{id}` — `204`, `404`


---

## Запуск через Docker


```bash
# Сборка образа
docker build -t vne-techwear-api:0.1.0 .

# Запуск контейнера
docker run --rm -p 8080:8080 -v "$(pwd)/data:/app/data" --name vne-api vne-techwear-api:0.1.0
```

Данные будут храниться в локальной папке `./data`

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


