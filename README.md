# FE Car Rental Android App (Java)

Skeleton Android project generated from the Android Developer Guide (Light version) for PRM Car Rental. The project focuses on renter and station staff flows with minimal dependencies and Java-based implementation.

## Structure Overview

```
FE_CarRental/
 ├─ app/
 │   ├─ src/main/java/com/prm/carrental
 │   │   ├─ core/             # Application, DI, session, base UI classes
 │   │   ├─ ui/auth           # Login & Register screens
 │   │   ├─ ui/home           # Renter navigation, stations, rentals, profile
 │   │   ├─ ui/payment        # Payment & success screens
 │   │   └─ ui/staff          # Staff verification & vehicle management flows
 │   └─ src/main/res         # Layouts, strings, themes, menus
 ├─ build.gradle             # Project level gradle config
 ├─ settings.gradle
 └─ gradle.properties
```

## Requirements
- Android Studio Ladybug (or newer) with JDK 17 support
- Android SDK Platform 34

## Getting Started
1. Open Android Studio → **Open an existing project** → select `D:\FE_CarRental`.
2. When prompted, let Android Studio install the missing Gradle wrapper (or run `gradle wrapper` manually if you have Gradle installed).
3. Sync the project. The IDE will download dependencies such as Retrofit, OkHttp, Glide, and Material Components.
4. Create a new Android emulator (API 30+) or connect a device; build & run the `app` module.

## API Integration
- Retrofit services (`AuthService`, `StationsService`) are wired to the .NET backend responses (enveloped `ApiResponse<T>`).
- `ApiClient` points to `http://10.0.2.2:5000/api/` for emulator access; adjust `BASE_URL` if your backend runs on a different host/port.
- `SessionManager` persists access tokens, role, and display name. Authorization headers are injected automatically for subsequent calls.
- Login/Register screens now invoke real APIs; successful login redirects to renter or staff flows based on `role`.
- Station list and station detail screens call `/api/stations` and `/api/stations/{id}/vehicles` to show live data.

## Next Steps
- Hook rentals, payments, and staff management screens to their corresponding endpoints.
- Introduce ViewModels + LiveData/Coroutines (or another state holder) for better life-cycle handling.
- Replace demo reservation/payment flows with real POST/PATCH commands once backend operations are finalized.

> The current codebase already authenticates against the backend and surfaces live station/vehicle data. Extend the remaining screens following the same Retrofit pattern.
