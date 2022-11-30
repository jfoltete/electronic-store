# Electronic store

## About
A REST service with a simple shopping cart, using Spring Boot and an in-memory database H2.

## Features
- Admin operations
    - add/remove a product
    - add discount deals for products

- Customer operations
    - add/remove items from a shopping cart
    - generate an invoice for the cart (items, deals applied and price)


## API

### Products 

- `GET /products` get all products
- `POST /products` create product
```
body example: {"name":"myProduct","price":100.00,"availability":2}
```
- `DELETE /products/{productId}` delete product

### Deals

- `GET /products/{productId}/coupons` get all coupons for product
- `POST /products/{productId}/coupons` create coupon for product
```
body example 1: {"id":1,"targetQuantity":2,"discountPercent":100}
Buy one, get one free

body example 2: {"id":1,"targetQuantity":3,"discountPercent":50}
Buy two, get one at -50%

body example 3: {"id":1,"targetQuantity":1,"discountPercent":20}
-20%
```

### Cart

- `POST /carts` create cart
```
empty body
```
- `GET /carts/{cartId}` get cart
- `POST /carts/{cartId}/items` add item to cart
```
body example: {"productId":1,"quantity":1}
```
- `DELETE /carts/{cartId}/items/{itemId}` delete item from cart

### Invoice

- `GET /carts/{cartId}/invoice` generate invoice for cart




## Build and Test
```bash
$ chmod +x scripts/mvnw
$ ./mvnw clean install
```

## Run
Using the jar
```bash
$ java -jar target/electronic-store-0.0.1-SNAPSHOT.jar
```

Using maven
```bash
$ chmod +x scripts/mvnw
$ ./mvnw spring-boot:run
```

## Monitor DB
You can monitor the db with http://localhost:8801/h2-console
