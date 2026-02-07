# Problem
Consider there are differnt types of alexa devices available. 
One with audio, one with screen, one with audio and screen. 
These devices may have a battery or may not. 
Battery devices will have battery percentage. 
Both battery and non battery devices can be put charging. 
The task is to show the battery percentage. 
Include a show methond and that method should show the current battery percentage if it has a battery. 
If not just say, battery not available. You should also say whether its currently charging or not. 
There will four statements to print show method like Charging and battery percentage, charging and no battery, just battery percent and no battery.
Expectation is to write interface-driven code using appropriate design patterns

# S0lution
🧠 45-Minute LLD Interview — Full Walkthrough (English)
## 0–3 min: Restate the problem

We need to model different types of Alexa devices: audio-only, screen-only, and audio-plus-screen.
Each device may or may not have a battery. If it has a battery, it has a battery percentage.
All devices can be charging regardless of whether they have a battery.
The system should expose a show method that prints:

whether the device is charging

battery percentage if a battery exists

otherwise, “battery not available”.

The expectation is interface-driven design and appropriate design patterns.

## 3–7 min: Clarify requirements (what interviewers expect)

Before designing, I want to clarify a few assumptions:

Battery percentage is valid only when a battery exists, range 0–100

Charging is an independent state (can be true even without battery)

Output format is flexible, correctness matters more than exact wording

Device type (audio/screen) is orthogonal to battery capability

This suggests composition over inheritance.

## 7–12 min: High-level design decision

I see two independent dimensions:

Device capability (audio, screen, audio+screen)

Battery behavior (battery vs no battery)

To avoid subclass explosion, I will:

model battery behavior using the Strategy pattern

create devices using a Factory

keep the design simple and readable under interview constraints

I intentionally avoid over-engineering (e.g. Optional, heavy state machines), but the design remains extensible.

## 12–15 min: Identify patterns explicitly (important!)

Strategy Pattern: to encapsulate battery behavior

Factory Pattern: to centralize object creation

Composition: devices have a battery, instead of are a battery device

## 35–40 min: Explain extensibility (very important)

This design scales well:

Adding a new device type → new subclass, factory update

Adding new battery behavior → new Battery implementation

If charging becomes more complex → introduce a ChargingState strategy

No existing code needs to be modified significantly.

## 40–45 min: Handle common follow-up questions
❓ Why Strategy for battery?

Because battery behavior changes independently of device type, and Strategy avoids tight coupling and subclass explosion.

❓ Why Factory?

It centralizes creation logic, enforces valid combinations, and keeps client code clean.

❓ Why not Optional / Null Object?

For this problem, I prioritized readability and speed.
If battery behavior becomes more complex or null handling becomes risky, this can be evolved into a stricter Null Object or Optional-based API.