# Hibernate Mapping Notes & Database Snapshots

This document provides a detailed breakdown of the Hibernate mappings used in the Ecommerce Demo project, including their default behaviors, configurations, and corresponding database representations.

---

## 1. One-to-One Bidirectional Mapping
**Example: [User](file:///d:/workspace/ecommerce-demo/src/main/java/org/example/ecommerce/model/User.java) $\leftrightarrow$ [Cart](file:///d:/workspace/ecommerce-demo/src/main/java/org/example/ecommerce/model/Cart.java)**

### Java Configuration
In `User.java`:
```java
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
private Cart cart;
```
In `Cart.java`:
```java
@OneToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", unique = true)
private User user;
```

### Database Snapshot
Hibernate creates a foreign key in the `carts` table pointing to `users`.

**Table: `users`**
| id (PK) | email | password | name |
| :--- | :--- | :--- | :--- |
| 1 | john@example.com | secret | John Doe |

**Table: `carts`**
| id (PK) | user_id (FK, Unique) |
| :--- | :--- |
| 11 | 1 |

> [!NOTE]
> **Default Behavior**: If `@JoinColumn` is omitted, Hibernate defaults to `[property_name]_[id_column_name]` (e.g., `user_id`). The `mappedBy` attribute indicates that the `User` entity is the inverse side, and the `Cart` entity owns the relationship.

---

## 2. One-to-Many / Many-to-One Bidirectional
**Example: [Cart](file:///d:/workspace/ecommerce-demo/src/main/java/org/example/ecommerce/model/Cart.java) $\leftrightarrow$ [CartItem](file:///d:/workspace/ecommerce-demo/src/main/java/org/example/ecommerce/model/CartItem.java)**

### Java Configuration
In `Cart.java`:
```java
@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<CartItem> items = new ArrayList<>();
```
In `CartItem.java`:
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "cart_id")
private Cart cart;
```

### Database Snapshot
The "Many" side (`cart_items`) holds the foreign key.

**Table: `cart_items`**
| id (PK) | cart_id (FK) | product_id (FK) | quantity |
| :--- | :--- | :--- | :--- |
| 101 | 11 | 501 | 2 |
| 102 | 11 | 502 | 1 |

> [!IMPORTANT]
> **CascadeType.ALL + orphanRemoval**: This configuration ensures that if a `CartItem` is removed from the `items` list in Java, Hibernate will automatically delete the corresponding row from the `cart_items` table.

---

## 3. Many-to-One Unidirectional
**Example: [CartItem](file:///d:/workspace/ecommerce-demo/src/main/java/org/example/ecommerce/model/CartItem.java) $\rightarrow$ [Product](file:///d:/workspace/ecommerce-demo/src/main/java/org/example/ecommerce/model/Product.java)**

### Java Configuration
In `CartItem.java`:
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "product_id")
private Product product;
```

### Database Snapshot
**Table: `products`**
| id (PK) | name | price | description |
| :--- | :--- | :--- | :--- |
| 501 | Laptop | 1200.00 | High-end gaming laptop |
| 502 | Mouse | 25.00 | Wireless optical mouse |

---

## Summary of Fetch Types & Defaults
| Annotation | Default Fetch Type | Typical Usage |
| :--- | :--- | :--- |
| `@OneToOne` | `EAGER` | When the related entity is almost always needed. |
| `@ManyToOne` | `EAGER` (Often changed to `LAZY`) | When many entities point to one (e.g., items to a product). |
| `@OneToMany` | `LAZY` | When an entity has a collection (to avoid loading thousands of items). |
| `@ManyToMany` | `LAZY` | Requires a Join Table. |

> [!TIP]
> Always prefer `FetchType.LAZY` for collections to prevent the **N+1 Query Problem**, and use `JOIN FETCH` in your JPQL/HQL when you specifically need to load the related data.
