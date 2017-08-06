# ticketservice
Walmart homework ticketservice

# Environment: 
Mac OS X El Capitan 10.11.6
Java 1.7
Maven 3.5.0

# Assumption

- The venue has 10 * 30 seats in total.
- Seats are held and reserved in the order of their indices.
- A seat has to be held before to be reserved.
- A hold will exprire in 20 seconds. The interval between checking SeatHold status is 2 seconds.
- Request to reserve a hold must has the same Email.
- All data/info is stored in memory.

# Setup and Build
1. git clone https://github.com/ClarKa/ticketservice.git
2. cd ticketservice/ticketservice/
3. mvn clean install

# Test
mvn test
