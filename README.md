# PokeAPI – Pokémon Watchlist 2.0  
**Assignment 5 – Android Development**  
**Deadline:** November 21, 2025 @ 9:59 PM

## 📱 Overview
PokeAPI – **Pokémon Watchlist 2.0** is an Android application (API 33+) that allows users to build a dynamic Pokémon watchlist using real-time data pulled from the **PokeAPI**.  
This upgraded version expands the original watchlist app by supporting unlimited Pokémon entries and automatically retrieving full Pokémon profiles through a RESTful Web API.

---

## 🎯 Objectives
- Integrate external RESTful Web API data into Android apps  
- Build scrollable, dynamic UI views  
- Implement input validation  
- Load and display remote images  
- Use ListView + Intents for navigation  

---

## 🧩 Features

### ✅ 1. Add Pokémon to Watchlist (20 pts)
- User enters a Pokémon **name or ID** in an input field  
- Input validation includes:
  - Invalid if containing: `%`, `&`, `*`, `(`, `@`, `)`, `!`, `;`, `:`, `<`, `>`
  - If numeric: must be **0 < id ≤ 1010**
- If valid:
  - Pokémon is added to the watchlist  
  - Full profile view loads automatically
- If invalid:
  - A Toast message notifies the user  

---

### 📋 2. Watchlist (30 pts)
- Pokémon stored and displayed in a **ListView**
- Each list entry shows:
  - **Pokédex ID**
  - **Name**
- Selecting an entry opens the Pokémon’s full profile

---

### 🧠 3. Pokémon Profile View (40 pts)
All data retrieved from the PokeAPI:  
- Name  
- Sprite image (displayed as an image, not a URL)  
- Pokédex ID  
- Height  
- Weight  
- Base XP  
- One move  
- One ability  
- View supports **scrolling**  
- API used:  
