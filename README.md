## Project Launch

* Clone project https://github.com/DayDream-21/SwaggerRestApp
* Open console in project folder and execute command **docker-compose up** (of course you will need docker)
* Go to http://localhost:8080/swagger-ui/#/ to interact with the application using the UI swagger

## Scope of automation.

* Automation of the seafaring process in a small state
* 3 ports, each with its own capacity
* Initially 10 ships
* New ships can be built and old ships can be scrapped

## Users and functions

* **Maritime Registry**: decommissioning and entering new ships, registering and discharging sailors, viewing up-to-date data on 
  ports and ships
* **Port Services**: port load analysis
* **Captains**: entering ship's location, assigning and removing a seafarer
* **Coast Guard**: up-to-date information about ships at sea

## Restrictions

### Maritime Register
* The Maritime Register maintains a list of professional seafarers. Each has the role of captain (CAPITAN) or sailor
  (MATE)
* The ship's registry can only fire a seafarer if the seafarer is on land (ON_LAND)
* The ship's registry cannot decommission a ship with seafarers on board
* The ship's registry assigns a captain to the ship (**As long as it is possible to assign sailors of any role, but until there is 
  the ship has one captain**)

### Captains and seamen
* There can only be one captain on a ship
* The captain assigns sailors to the ship, and he can also send a sailor to rest when in port (PORT) (**For now
  it is possible to assign sailors of any role, but as long as there is not one captain on the ship**)
* Assigning (ON_SHIP) and removing (ON_LAND) sailors is only done in port. For ships at sea (SEA) changing
  command is not possible
* Captain gives commands to ship, hence ship cannot go to sea (SEA) without captain
* There is a maximum and minimum number of crew members at which the ship can leave the port
* Any sailor can only be assigned to 1 ship or not be assigned to any ship
* Coast Guard wants to know not only about the ships at sea, but also how many people are on them and what their roles are

## Assumptions 

* No separation of user access rights to information
* All ships are built according to the same design 
* Ships differ only in the size of minimum and maximum crew
* Ships are built one at a time (no 2+ ships can be entered simultaneously) 
* All mooring places in ports are of the same size for a typical ship design (any ship occupies only 1 place)
