Feature: Receive Inventory
  Scenario: Receive new product into warehouse
    Given warehouse "WH-001" exists
    When I receive 100 units of product "SKU-123"
    Then the inventory should show 100 units of "SKU-123"