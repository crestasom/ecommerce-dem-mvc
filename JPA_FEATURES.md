# Spring Data JPA Migration - Features Documentation

This document explains the Spring Data JPA features implemented in the ecommerce-demo project.

## Overview

The project now supports **both** in-memory and MySQL database persistence:
- **In-memory repositories**: Original implementation in `repository.impl` package
- **JPA repositories**: New implementation in `repository.jpa` package

## JPA Entities

All domain models are now JPA entities with proper annotations:

### Entity Mappings

| Entity | Table Name | Key Features |
|--------|-----------|--------------|
| `User` | `users` | @Id, @GeneratedValue, unique email constraint |
| `Product` | `products` | @Id, @GeneratedValue, column constraints |
| `Cart` | `carts` | @Id, relationships to User and CartItems |
| `CartItem` | `cart_items` | @Id, relationships to Cart and Product |
| `Employee` | `employees` | @Id, unique email constraint |

## Relationship Mappings

### One-to-One: User ↔ Cart
```java
// In User entity:
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
private Cart cart;

// In Cart entity:
@OneToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", unique = true)
private User user;
```
- **One-to-One**: Each user has exactly one cart
- **Cascade**: All operations cascade to cart
- **Orphan Removal**: Deleting a user deletes their cart
- **EAGER Fetch**: User is loaded immediately with cart (for demonstration)

### One-to-Many: Cart → CartItems
```java
@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<CartItem> items = new ArrayList<>();
```
- **LAZY Fetch**: Items loaded on-demand (for demonstration)
- **Cascade & Orphan Removal**: Cart items are managed by cart lifecycle

### Many-to-One: CartItem → Product
```java
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "product_id")
private Product product;
```
- **EAGER Fetch**: Product details loaded with cart item

## Fetch Type Demonstrations

### EAGER Fetch
- Related entities loaded **immediately** with parent entity
- Results in JOIN queries
- Example: `Cart → User` relationship

**Test endpoint**: `GET /api/fetch-demo/eager/{cartId}`

### LAZY Fetch
- Related entities loaded **on-demand** when accessed
- Separate queries executed when needed
- Example: `Cart → CartItems` relationship

**Test endpoint**: `GET /api/fetch-demo/lazy/{cartId}`

## N+1 Query Problem

### The Problem
When fetching a collection of entities with lazy-loaded relationships:
- **1 query** to fetch parent entities
- **N queries** to fetch related entities (one per parent)

**Demonstration**: `GET /api/n1-demo/problem/{userId}`

### The Solution: JOIN FETCH
Use JPQL with `JOIN FETCH` to load everything in a single query:

```java
@Query("SELECT c FROM Cart c JOIN FETCH c.items WHERE c.user.id = :userId")
List<Cart> findByUserIdWithItems(@Param("userId") Long userId);
```

**Demonstration**: `GET /api/n1-demo/solution-join-fetch/{userId}`

### Performance Comparison
**Endpoint**: `GET /api/n1-demo/compare/{userId}`

Check console logs to see the difference:
- **Without JOIN FETCH**: Multiple SQL queries (1 + N)
- **With JOIN FETCH**: Single SQL query with JOIN

## Spring Data JPA Repositories

All repositories are in `org.example.ecommerce.repository.jpa` package:

- `UserJpaRepository extends JpaRepository<User, Long>`
- `ProductJpaRepository extends JpaRepository<Product, Long>`
- `CartJpaRepository extends JpaRepository<Cart, Long>`
- `CartItemJpaRepository extends JpaRepository<CartItem, Long>`
- `EmployeeJpaRepository extends JpaRepository<Employee, Long>`

### Auto-Implemented Methods
Spring Data JPA provides these methods automatically:
- `save(entity)`, `findById(id)`, `findAll()`, `delete(entity)`, `count()`, etc.

### Custom Query Methods
- `UserJpaRepository.findByEmail(String email)`
- `ProductJpaRepository.findByNameContaining(String name)`
- `CartJpaRepository.findByUserId(Long userId)`
- `CartJpaRepository.findByUserIdWithItems(Long userId)` - with JOIN FETCH

## Database Configuration

### MySQL Setup
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

### Data Initialization
On application startup, `DataInitializer` seeds the database with:
- 2 sample users (admin, user1)
- 5 sample products (Laptop, Mouse, Keyboard, Monitor, Headphones)

## Testing the Features

1. **Start MySQL** (ensure it's running on port 3306)

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

3. **Check console logs** for:
   - Hibernate DDL statements (table creation)
   - SQL queries with formatting
   - Data initialization messages

4. **Test fetch types**:
   - Create a cart with items first
   - Access `/api/fetch-demo/eager/{cartId}`
   - Access `/api/fetch-demo/lazy/{cartId}`
   - Access `/api/fetch-demo/user-cart/{userId}`
   - Compare SQL queries in console

5. **Test N+1 problem with cart items**:
   - Access `/api/n1-demo/problem/{userId}`
   - Access `/api/n1-demo/eager-fetch/{cartId}`
   - Access `/api/n1-demo/compare/{userId}`
   - Compare query patterns in console

## Key Takeaways

✅ **Entity Relationships**: One-to-One (User-Cart), One-to-Many (Cart-CartItems), Many-to-One (CartItem-Product)  
✅ **Fetch Types**: EAGER vs LAZY demonstrated with real examples  
✅ **N+1 Problem**: Demonstrated with cart items loading  
✅ **Cascade Operations**: Automatic management of related entities  
✅ **Spring Data JPA**: Minimal boilerplate code for repositories  
✅ **SQL Logging**: Enabled for learning and debugging  

## Next Steps

- Implement service layer using JPA repositories
- Add more complex query examples
- Create integration tests
- Add pagination and sorting examples
