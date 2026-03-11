# Shopizer API Reference

Base URL: `http://localhost:8080`

> **Access Levels:**
> - `/public/` — No authentication required
> - `/customer/`, `/cart/`, `/search/`, `/products/` — Public storefront
> - `/auth/` — Authenticated customer (JWT)
> - `/private/` — Admin/merchant (JWT)

---

## Authentication

### User (Admin)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/login` | Admin login |
| GET | `/api/v1/auth/refresh` | Refresh admin token |
| POST | `/api/v1/user/password/reset/request` | Request password reset |
| GET | `/api/v1/user/{store}/reset/{token}` | Validate reset token |
| POST | `/api/v1/user/{store}/password/{token}` | Reset password |

### Customer
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/customer/register` | Register customer |
| POST | `/api/v1/customer/login` | Customer login |
| GET | `/api/v1/auth/customer/refresh` | Refresh customer token |
| POST | `/api/v1/auth/customer/password` | Change customer password |
| POST | `/api/v1/customer/password/reset/request` | Request password reset |
| GET | `/api/v1/customer/{store}/reset/{token}` | Validate reset token |
| POST | `/api/v1/customer/{store}/password/{token}` | Reset password |

---

## Store / Merchant

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/store/{code}` | Get public store info |
| GET | `/api/v1/private/store/{code}` | Get store (admin) |
| GET | `/api/v1/private/stores` | List all stores |
| GET | `/api/v1/private/stores/names` | List store names |
| GET | `/api/v1/private/merchant/{code}/stores` | Get merchant stores |
| GET | `/api/v1/private/merchant/{code}/children` | Get child stores |
| POST | `/api/v1/private/store` | Create store |
| PUT | `/api/v1/private/store/{code}` | Update store |
| DELETE | `/api/v1/private/store/{code}` | Delete store |
| GET | `/api/v1/store/unique` | Check store code uniqueness |
| GET | `/api/v1/store/languages` | Get store languages |
| GET | `/api/v1/private/store/{code}/marketing` | Get store marketing info |
| POST | `/api/v1/private/store/{code}/marketing` | Update store marketing |
| POST | `/api/v1/private/store/{code}/marketing/logo` | Upload store logo |
| DELETE | `/api/v1/private/store/{code}/marketing/logo` | Delete store logo |

---

## Products (v1)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/products` | List products |
| GET | `/api/v1/product/{id}` | Get product by ID |
| GET | `/api/v1/product/{friendlyUrl}` | Get product by URL |
| GET | `/api/v1/product/{id}/price` | Get product price |
| GET | `/api/v1/private/product/unique` | Check product uniqueness |
| PUT | `/api/v1/private/product/{id}` | Update product |
| PATCH | `/api/v1/private/product/{id}` | Partial update product |
| DELETE | `/api/v1/private/product/{id}` | Delete product |
| POST | `/api/v1/private/product/{productId}/category/{categoryId}` | Add product to category |
| DELETE | `/api/v1/private/product/{productId}/category/{categoryId}` | Remove product from category |
| GET | `/api/v1/product/{id}/related` | Get related products |

## Products (v2)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v2/private/product` | Create product |
| PUT | `/api/v2/private/product/{id}` | Update product |
| GET | `/api/v2/private/product/{id}` | Get product (admin) |
| DELETE | `/api/v2/private/product/{id}` | Delete product |
| GET | `/api/v2/product/{sku}` | Get product by SKU |
| GET | `/api/v2/product/name/{friendlyUrl}` | Get product by name |
| GET | `/api/v2/products` | List products |
| GET | `/api/v2/products/category/{friendlyUrl}` | Products by category |
| PATCH | `/api/v2/private/product/{sku}` | Patch product by SKU |
| GET | `/api/v2/private/product/inventory` | Get product inventory |

---

## Product Images

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/product/{id}/image` | Upload product image |
| GET | `/api/v1/product/{productId}/images` | Get product images |
| PUT | `/api/v1/private/product/image/{id}` | Update product image |
| DELETE | `/api/v1/private/product/{id}/image/{imageId}` | Delete product image |

---

## Product Attributes & Options

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/product/option` | Create option |
| GET | `/api/v1/private/product/option/{id}` | Get option |
| PUT | `/api/v1/private/product/option/{optionId}` | Update option |
| DELETE | `/api/v1/private/product/option/{optionId}` | Delete option |
| GET | `/api/v1/private/product/options` | List options |
| GET | `/api/v1/private/product/option/unique` | Check option uniqueness |
| POST | `/api/v1/private/product/option/value` | Create option value |
| GET | `/api/v1/private/product/option/value/{id}` | Get option value |
| PUT | `/api/v1/private/product/option/value/{id}` | Update option value |
| DELETE | `/api/v1/private/product/option/value/{id}` | Delete option value |
| GET | `/api/v1/private/product/options/values` | List option values |
| POST | `/api/v1/private/product/option/value/{id}/image` | Upload option value image |
| DELETE | `/api/v1/private/product/option/value/{id}/image` | Delete option value image |
| GET | `/api/v1/private/product/{id}/attributes` | Get product attributes |
| GET | `/api/v1/private/product/{id}/attribute/{attributeId}` | Get attribute |
| POST | `/api/v1/private/product/{id}/attribute` | Add attribute |
| POST | `/api/v1/private/product/{id}/attributes` | Add multiple attributes |
| PUT | `/api/v1/private/product/{id}/attribute/{attributeId}` | Update attribute |
| DELETE | `/api/v1/private/product/{id}/attribute/{attributeId}` | Delete attribute |

---

## Product Variants (v2)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v2/private/product/{productId}/variant` | Create variant |
| PUT | `/api/v2/private/product/{id}/variant/{variantId}` | Update variant |
| GET | `/api/v2/private/product/{id}/variant/{variantId}` | Get variant |
| GET | `/api/v2/private/product/{id}/variants` | List variants |
| DELETE | `/api/v2/private/product/{id}/variant/{variantId}` | Delete variant |
| GET | `/api/v2/private/product/{id}/variant/{sku}/unique` | Check variant uniqueness |
| POST | `/api/v2/private/product/{id}/{variantId}/image` | Upload variant image |

## Product Variant Groups (v2)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v2/private/product/productVariantGroup` | Create variant group |
| PUT | `/api/v2/private/product/productVariantGroup/{id}` | Update variant group |
| GET | `/api/v2/private/product/productVariantGroup/{id}` | Get variant group |
| GET | `/api/v2/private/product/{id}/productVariantGroup` | Get product variant groups |
| DELETE | `/api/v2/private/product/productVariantGroup/{id}` | Delete variant group |
| POST | `/api/v2/private/product/productVariantGroup/{id}/image` | Upload variant group image |

## Product Variations (v2)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v2/private/product/variation` | Create variation |
| GET | `/api/v2/private/product/variation/{variationId}` | Get variation |
| PUT | `/api/v2/private/product/variation/{variationId}` | Update variation |
| DELETE | `/api/v2/private/product/variation/{variationId}` | Delete variation |
| GET | `/api/v2/private/product/variations` | List variations |
| GET | `/api/v2/private/product/variation/unique` | Check variation uniqueness |
| POST | `/api/v2/product/{id}/variation` | Get product variation |
| GET | `/api/v2/category/{id}/variations` | Get category variations |

---

## Product Inventory

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/product/{productId}/inventory` | Create inventory |
| PUT | `/api/v1/private/product/{productId}/inventory/{id}` | Update inventory |
| DELETE | `/api/v1/private/product/{productId}/inventory/{id}` | Delete inventory |
| GET | `/api/v1/private/product/{sku}/inventory` | Get inventory by SKU |
| GET | `/api/v1/private/product/inventory` | List inventory |

---

## Product Pricing

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/product/{sku}/inventory/{inventoryId}/price` | Create price |
| GET | `/api/v1/private/product/{sku}/inventory/{inventoryId}/price/{priceId}` | Get price |
| GET | `/api/v1/private/product/{sku}/price` | Get product price |
| PUT | `/api/v1/private/product/{sku}/price/{priceId}` | Update price |
| GET | `/api/v1/private/product/{sku}/inventory/{inventoryId}/price` | Get inventory price |
| GET | `/api/v1/private/product/{sku}/prices` | List prices |

---

## Product Types

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/private/product/types` | List product types |
| GET | `/api/v1/private/product/type/{id}` | Get product type |
| GET | `/api/v1/private/product/type/unique` | Check type uniqueness |
| POST | `/api/v1/private/product/type` | Create product type |
| PUT | `/api/v1/private/product/type/{id}` | Update product type |
| DELETE | `/api/v1/private/product/type/{id}` | Delete product type |

---

## Product Groups

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/products/group` | Create product group |
| PATCH | `/api/v1/private/products/group/{code}` | Update product group |
| GET | `/api/v1/private/product/groups` | List product groups |
| GET | `/api/v1/products/group/{code}` | Get products in group |
| POST | `/api/v1/private/products/{productId}/group/{code}` | Add product to group |
| DELETE | `/api/v1/products/group/{code}` | Delete product group |

---

## Product Reviews

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/product/{id}/reviews` | Get product reviews |
| POST | `/api/v1/private/product/{id}/review` | Create review (admin) |
| PUT | `/api/v1/private/product/{id}/review/{reviewId}` | Update review |
| DELETE | `/api/v1/private/product/{id}/review/{reviewId}` | Delete review |

---

## Product Property Sets

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/product/property/set` | Create property set |
| GET | `/api/v1/private/product/property/set/{id}` | Get property set |
| PUT | `/api/v1/private/product/property/set/{id}` | Update property set |
| DELETE | `/api/v1/private/product/property/set/{id}` | Delete property set |
| GET | `/api/v1/private/product/property/set` | List property sets |
| GET | `/api/v1/private/product/property/set/unique` | Check uniqueness |

---

## Manufacturers

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/manufacturer` | Create manufacturer |
| GET | `/api/v1/manufacturer/{id}` | Get manufacturer |
| GET | `/api/v1/manufacturers` | List manufacturers (public) |
| GET | `/api/v1/private/manufacturers` | List manufacturers (admin) |
| GET | `/api/v1/private/manufacturer/unique` | Check uniqueness |
| PUT | `/api/v1/private/manufacturer/{id}` | Update manufacturer |
| DELETE | `/api/v1/private/manufacturer/{id}` | Delete manufacturer |
| GET | `/api/v1/category/{id}/manufacturer` | Get manufacturers by category |

---

## Categories

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/category/{id}` | Get category by ID |
| GET | `/api/v1/category/{friendlyUrl}` | Get category by URL |
| GET | `/api/v1/category` | List categories |
| GET | `/api/v1/category/product/{ProductId}` | Get categories for product |
| GET | `/api/v1/private/category/unique` | Check category uniqueness |
| POST | `/api/v1/private/category` | Create category |
| PUT | `/api/v1/private/category/{id}` | Update category |
| PATCH | `/api/v1/private/category/{id}/visible` | Toggle category visibility |
| PUT | `/api/v1/private/category/{id}/move/{parent}` | Move category |
| DELETE | `/api/v1/private/category/{id}` | Delete category |

---

## Catalog

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/private/catalogs` | List catalogs |
| GET | `/api/v1/private/catalog/unique` | Check catalog uniqueness |
| POST | `/api/v1/private/catalog` | Create catalog |
| PATCH | `/api/v1/private/catalog/{id}` | Update catalog |
| GET | `/api/v1/private/catalog/{id}` | Get catalog |
| DELETE | `/api/v1/private/catalog/{id}` | Delete catalog |
| POST | `/api/v1/private/catalog/{id}` | Add entry to catalog |
| DELETE | `/api/v1/private/catalog/{id}/entry/{entryId}` | Remove catalog entry |
| GET | `/api/v1/private/catalog/{id}/entry` | List catalog entries |

---

## Shopping Cart

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/cart` | Create cart |
| PUT | `/api/v1/cart/{code}` | Update cart |
| GET | `/api/v1/cart/{code}` | Get cart |
| POST | `/api/v1/cart/{code}/multi` | Add multiple items |
| POST | `/api/v1/cart/{code}/promo/{promo}` | Apply promo code |
| DELETE | `/api/v1/cart/{code}/product/{sku}` | Remove item from cart |
| POST | `/api/v1/customers/{id}/cart` | Create cart for customer |
| GET | `/api/v1/auth/customer/{id}/cart` | Get customer cart (auth) |
| GET | `/api/v1/auth/customer/cart` | Get current customer cart |

---

## Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/cart/{code}/checkout` | Checkout (anonymous) |
| POST | `/api/v1/auth/cart/{code}/checkout` | Checkout (authenticated) |
| GET | `/api/v1/private/orders` | List all orders (admin) |
| GET | `/api/v1/private/orders/{id}` | Get order (admin) |
| GET | `/api/v1/private/orders/customers/{id}` | Get orders by customer |
| GET | `/api/v1/auth/orders` | Get authenticated customer orders |
| GET | `/api/v1/auth/orders/{id}` | Get specific order (auth) |
| PATCH | `/api/v1/private/orders/{id}/customer` | Update order customer |
| PUT | `/api/v1/private/orders/{id}/status` | Update order status |
| GET | `/api/v1/private/orders/{id}/history` | Get order history |
| POST | `/api/v1/private/orders/{id}/history` | Add order history entry |

## Order Totals

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/cart/{code}/total` | Calculate cart total |
| POST | `/api/v1/auth/cart/{code}/total` | Calculate cart total (auth) |

## Order Shipping

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/cart/{code}/shipping` | Get shipping quotes |
| POST | `/api/v1/auth/cart/{code}/shipping` | Get shipping quotes (auth) |

## Order Payment

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/cart/{code}/payment/init` | Init payment (anonymous) |
| POST | `/api/v1/auth/cart/{code}/payment/init` | Init payment (auth) |
| GET | `/api/v1/private/orders/{id}/payment/nextTransaction` | Get next transaction |
| GET | `/api/v1/private/orders/{id}/payment/transactions` | Get transactions |
| GET | `/api/v1/private/orders/payment/capturable` | Get capturable orders |
| POST | `/api/v1/private/orders/{id}/capture` | Capture payment |
| POST | `/api/v1/private/orders/{id}/refund` | Refund payment |
| POST | `/api/v1/private/orders/{id}/authorize` | Authorize payment |

---

## Customers

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/customer` | Create customer (admin) |
| PUT | `/api/v1/private/customer/{id}` | Update customer (admin) |
| PATCH | `/api/v1/private/customer/{id}/address` | Update customer address (admin) |
| DELETE | `/api/v1/private/customer/{id}` | Delete customer (admin) |
| GET | `/api/v1/private/customers` | List customers |
| GET | `/api/v1/private/customer/{id}` | Get customer by ID |
| GET | `/api/v1/private/customer/profile` | Get customer profile |
| GET | `/api/v1/auth/customer/profile` | Get own profile (auth) |
| PATCH | `/api/v1/auth/customer/address` | Update own address (auth) |
| PATCH | `/api/v1/auth/customer/` | Update own profile (auth) |
| DELETE | `/api/v1/auth/customer/` | Delete own account (auth) |

## Customer Reviews

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/customers/{id}/reviews` | Create customer review |
| GET | `/api/v1/customers/{id}/reviews` | Get customer reviews |
| PUT | `/api/v1/private/customers/{id}/reviews/{reviewid}` | Update review |
| DELETE | `/api/v1/private/customers/{id}/reviews/{reviewId}` | Delete review |

## Newsletter

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/newsletter` | Subscribe to newsletter |
| PUT | `/api/v1/newsletter/{email}` | Update subscription |
| DELETE | `/api/v1/newsletter/{email}` | Unsubscribe |

---

## Users (Admin)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/private/users/{id}` | Get user by ID |
| POST | `/api/v1/private/user/` | Create user |
| PUT | `/api/v1/private/user/{id}` | Update user |
| PATCH | `/api/v1/private/user/{id}/password` | Change user password |
| PATCH | `/api/v1/private/user/{id}/enabled` | Enable/disable user |
| DELETE | `/api/v1/private/user/{id}` | Delete user |
| GET | `/api/v1/private/users` | List users |
| POST | `/api/v1/private/user/unique` | Check user uniqueness |
| GET | `/api/v1/private/user/profile` | Get own profile |

---

## Tax

### Tax Classes
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/tax/class` | Create tax class |
| GET | `/api/v1/private/tax/class` | List tax classes |
| GET | `/api/v1/private/tax/class/{code}` | Get tax class |
| GET | `/api/v1/private/tax/class/unique` | Check uniqueness |
| PUT | `/api/v1/private/tax/class/{id}` | Update tax class |
| DELETE | `/api/v1/private/tax/class/{id}` | Delete tax class |

### Tax Rates
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/tax/rate` | Create tax rate |
| GET | `/api/v1/private/tax/rates` | List tax rates |
| GET | `/api/v1/private/tax/rate/{id}` | Get tax rate |
| GET | `/api/v1/private/tax/rate/unique` | Check uniqueness |
| PUT | `/api/v1/private/tax/rate/{id}` | Update tax rate |
| DELETE | `/api/v1/private/tax/rate/{id}` | Delete tax rate |

---

## Shipping

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/private/shipping/origin` | Get shipping origin |
| POST | `/api/v1/private/shipping/origin` | Set shipping origin |
| GET | `/api/v1/private/shipping/packages` | List shipping packages |
| GET | `/api/v1/private/shipping/package/{code}` | Get shipping package |
| POST | `/api/v1/private/shipping/package` | Create shipping package |
| PUT | `/api/v1/private/shipping/package/{code}` | Update shipping package |
| DELETE | `/api/v1/private/shipping/package/{code}` | Delete shipping package |
| GET | `/api/v1/private/modules/shipping` | List shipping modules |
| GET | `/api/v1/private/modules/shipping/{code}` | Get shipping module |
| POST | `/api/v1/private/modules/shipping` | Configure shipping module |
| GET | `/api/v1/private/shipping/expedition` | Get expedition config |
| POST | `/api/v1/private/shipping/expedition` | Set expedition config |
| GET | `/api/v1/shipping/country` | Get shippable countries |

---

## Payment

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/private/modules/payment` | List payment modules |
| GET | `/api/v1/private/modules/payment/{code}` | Get payment module |
| POST | `/api/v1/private/modules/payment` | Configure payment module |

---

## Content

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/content/pages` | List content pages (public) |
| GET | `/api/v1/content/summary` | Get content summary |
| GET | `/api/v1/content/boxes` | Get content boxes |
| GET | `/api/v1/content/pages/{code}` | Get page by code |
| GET | `/api/v1/content/pages/name/{name}` | Get page by name |
| GET | `/api/v1/content/boxes/{code}` | Get box by code |
| GET | `/api/v1/content/images` | List content images |
| POST | `/api/v1/private/content/box` | Create content box |
| POST | `/api/v1/private/content/page` | Create content page |
| PUT | `/api/v1/private/content/page/{id}` | Update content page |
| PUT | `/api/v1/private/content/box/{id}` | Update content box |
| DELETE | `/api/v1/private/content/page/{id}` | Delete content page |
| DELETE | `/api/v1/private/content/box/{id}` | Delete content box |
| GET | `/api/v1/private/content/pages` | List pages (admin) |
| GET | `/api/v1/private/content/boxes` | List boxes (admin) |
| GET | `/api/v1/private/content/list` | List all content |
| GET | `/api/v1/private/content/any/{code}` | Get any content by code |
| GET | `/api/v1/private/contents/any` | List all content |
| GET | `/api/v1/private/content/boxes/{code}` | Get box (admin) |
| GET | `/api/v1/private/content/box/{code}/exists` | Check box exists |
| GET | `/api/v1/private/content/page/{code}/exists` | Check page exists |
| POST | `/api/v1/private/file` | Upload file |
| POST | `/api/v1/private/files` | Upload multiple files |
| PUT | `/api/v1/private/content/{id}` | Update content |
| DELETE | `/api/v1/private/content/{id}` | Delete content |
| DELETE | `/api/v1/private/content/` | Delete all content |
| GET | `/api/v1/private/content/folder` | List content folders |
| POST | `/api/v1/private/content/images/add` | Upload content image |
| GET | `/api/v1/content/images/download` | Download image |
| POST | `/api/v1/private/content/images/rename` | Rename image |
| DELETE | `/api/v1/private/content/images/remove` | Remove image |

---

## Search

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/search` | Search products |
| POST | `/api/v1/search/autocomplete` | Autocomplete search |

---

## Security

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/sec/private/{group}/permissions` | Get group permissions |
| GET | `/api/v1/sec/private/permissions` | List all permissions |
| GET | `/api/v1/sec/private/groups` | List security groups |

---

## References

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/languages` | List languages |
| GET | `/api/v1/country` | List countries |
| GET | `/api/v1/zones` | List zones/states |
| GET | `/api/v1/currency` | List currencies |
| GET | `/api/v1/measures` | List measurement units |

---

## Configurations

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/private/configurations/payment` | Save payment config |
| GET | `/api/v1/private/configurations/payment` | Get payment config |
| GET | `/api/v1/private/configurations/shipping` | Get shipping config |
| DELETE | `/api/v1/auth/cache/store/{storeId}/clear` | Clear store cache |

---

## System

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/config` | Get public config |
| POST | `/api/v1/contact` | Submit contact form |
| POST | `/api/v1/private/optin` | Create optin |
| POST | `/api/v1/private/system/search/index` | Rebuild search index |

---

## Marketplace

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/private/marketplace/{store}` | Get marketplace info |
| POST | `/api/v1/store/signup` | Store signup |
| GET | `/api/v1/store/{store}/signup/{token}` | Validate signup token |

---

## Legacy (v0)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/services/public/{store}` | Get store info |
| POST | `/services/public/{store}/contact` | Store contact form |
| POST | `/services/private/system/module` | Configure module |
| POST | `/services/private/system/optin` | Create optin |
| DELETE | `/services/private/system/optin/{code}` | Delete optin |
| POST | `/services/private/system/optin/{code}/customer` | Add customer to optin |
