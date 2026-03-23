# starts with this prompt:
"Design a movie ticket booking system similar to BookMyShow that allows users to browse movies, select theaters and showtimes, book tickets, and manage reservations."

# make clarifications
1. browse moveis: full-text search/ fuzzy matching/ simple title lookup?
-> just iterate through the movie list and check if the title contains the search item
2. How does seat selection work? Does the user pick specific seats from a map, or does the system auto-assign? And can they book more than one seat at a time?
-> Users pick specific seats from a seat map. And yes, they can book multiple seats in one transaction
-> our system needs to expose which seats are available so a frontend could display them.
3. Are we designing for a single theater or multiple? And do theaters have multiple screens?
-> Multiple theaters, each with multiple screens. A user can search for a movie and see where it's playing, or go to a specific theater and see what's on.
-> Two entry points into the system: search by movie title globally, or browse a specific theater's offerings.
4. seat configurations
->  Every screen has the same layout: rows A through Z, seats 0 through 20
5. manage reservations
-> cancle only
6. different seats with different prices?
-> no
7. process payment?
-> no
8. concurrency
-> handle it. only exactly one can succeed booking the same 

# final requirements
Requirements:
1. Users can search for movies by title
2. Users can browse movies playing at a given theater
3. Theaters have multiple screens; all screens share the same seat layout (rows A-Z, seats 0-20)
4. Users can view available seats for a showtime and select specific ones
5. Users can book multiple seats in a single reservation; booking returns a confirmation ID
6. Concurrent booking of the same seat: exactly one succeeds
7. Users can cancel a reservation by confirmation ID, releasing the seats

Out of Scope:
- Payment processing (assume payment always succeeds)
- Variable seat layouts or seat types (all seats identical)
- Rescheduling (cancel and rebook instead)
- UI / rendering

# Core Entities and Relationships
Entity	Responsibility	Type
BookingSystem	Orchestrator. Owns theaters. Entry point for search and cross-theater queries.	Class
Movie	Searchable entity. Title plus ID. Ties showtimes together across theaters.	Class
Theater	Named location. Owns a list of showtimes.	Class
Showtime	A specific screening. Tracks per-seat availability and handles booking concurrency.	Class
Reservation	User's booking reference. Stores confirmation ID and which seats were booked. Used for cancellation.	Class
Seat	A string identifying a specific seat, e.g. "A5". No behavior, just an identifier.	String
Screen	Label identifying which room a showtime is in, e.g. "Screen 3".	String field
## Key Relationships
BookingSystem → List<Theater>
Theater → List<Showtime>
Showtime → Theater (back-reference for navigation)
Showtime → Movie (reference)
Showtime → List<Reservation> (booking records for this showtime)
Reservation → Showtime (back-reference for cancellation routing)
Reservation → List<string> (e.g., ["A5", "A6"])