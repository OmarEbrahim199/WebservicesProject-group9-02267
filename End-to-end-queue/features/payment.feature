Feature: Payment
  @payment
  Scenario: Successful Payment
    Given customer with a bank account with balance 120.00
    And that the customer is registered with DTU Pay
    And the customer requests new tokens
    Given a merchant with a bank account with balance 2320.00
    And that the merchant is registered with DTU Pay
    When the merchant initiates a payment for 50 kr by the customer
    Then the payment is "successful"
    And the balance of the customer at the bank is 70.00 kr
    And the balance of the merchant at the bank is 2370.00 kr