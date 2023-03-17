Feature: Report

  @report
  Scenario: There is two reports with the same paymentID that the manager can see
    Given one completed transaction
    And the manager request the reportList
    Then the reportList should contain two reports with the same tokenID

  @report
  Scenario: A customer and merchant request their transactions report
    Given one completed transaction
    When the customer requests their report
    And the merchant requests their report
    Then the customer receives 1 report
    And the merchant receives 1 report


