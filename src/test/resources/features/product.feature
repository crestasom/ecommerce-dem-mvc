Feature: Product Management
  As an Administrator
  I want to manage products in the e-commerce system
  So that customers can view and purchase them

  # Success Case for Adding a Product
  Scenario: Successfully adding a new product
    Given I am logged in as an administrator
    And I navigate to the add product page
    When I enter product details with name "MacBook Pro", description "M3 Chip Laptop", and price 2500.00
    And I click save
    Then I should see a success message "Product saved successfully!"
    And the product "MacBook Pro" should be present in the product list

  # Failure Case for Adding a Product
  Scenario: Failing to add a product with empty details
    Given I am logged in as an administrator
    And I navigate to the add product page
    When I enter product details with name "", description "", and price 0.00
    And I click save
    Then I should see an error message or the form should remain
