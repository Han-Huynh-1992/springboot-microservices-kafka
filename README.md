# Spring Boot Kafka Microservices Order Processing System

This project demonstrates a microservices architecture built with Spring Boot and Apache Kafka, containerized using Docker Compose for easy local deployment.

The system simulates an order processing workflow using event-driven communication between services.

The project consists of the following services:

### OrderService | Producer
When a new order is created, the service publishes an 'OrderCreatedEvent' containing the order information to the Kafka broker.

### EmailService | Consumer
This service listens for the order event and sends a confirmation email to the customer.

### ShipmentService | Consumer
This service consumes the same order event and creates a shipment record, calculates the estimated delivery date, and sets the shipment status.
After the shipment record is successfully created and stored in the database, the service also publishes a 'ShipmentCreatedEvent'. This event is consumed by the EmailService to send a shipment confirmation email to the customer, informing them that the order has been shipped and providing delivery details.

### Kafka Broker | Message Broker
Kafka receives events from the producer and distributes them to all consumers subscribed to the topic.

# How to run the app locally
  1. Install Docker Desktop
  2. Clone the repository on master branch
  3. To start the application, from the root directory of the project, run: "docker compose --env-file local.env up --build" command
  4. Navigate to Postman tool, the Order API will be at "POST localhost:8080/order/create" to create an order, you can use JSON sample (orderData.txt) to add data on "Body" tab

# Application Flow
  #### 1. Order Creation
  + When a new order is created, the OrderService stores the order in the database and publishes an OrderCreatedEvent to the Kafka broker.
  #### 2. Kafka Event Distribution
  + The Kafka Broker receives the event and distributes it to all consumers subscribed to the topic.
  #### 3. Email Notification
  + The EmailService receives the event and sends a confirmation email to the customer containing the order details.
  #### 4. Shipment Processing
  + The ShipmentService receives the same event and creates a shipment record.
  + The estimated delivery date is not simply the current date. Instead, it is calculated using business logic that considers several factors.
  #### 5. Shipment Confirmation Email
  + After the shipment record is stored, the ShipmentService publishes a 'ShipmentCreatedEvent'.
  + The EmailService consumes this event and sends another email to notify the customer that the order has been shipped.
  #### Factors affecting the estimated delivery date
    1. Order Creation Date
        + The date when the order is created.
    2. Processing Time
        + Time required to prepare the order (packing, quality checks).
        + Typically around 1–2 days.
    3. Shipping Time
        + Estimated transportation time depending on shipping method, location, and distance.
        + Usually 2–5 days.
    4. Weekends and Holidays
        + Non-working days may be excluded from delivery calculations if shipments are not processed during those periods.
