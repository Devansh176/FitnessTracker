# 🏃‍♂️ Fitness Tracker App – Kotlin + CPS Integration

A modern Android fitness companion app built using **Kotlin**. This app plays a vital role in a **Cyber-Physical System (CPS)** where real-time **heart rate data** flows from a smartwatch → Google Fit → this app → Spring Boot backend for storage and analysis.

---

## 🔗 System Flow

📲 **Smartwatch** → 📱 **Google Fit** → 📱 **Fitness Tracker App (Kotlin)** → 🌐 **Spring Boot Backend**

---

## 🧠 Core Highlights

- 🔄 **Real-time heart rate tracking** from Google Fit
- 🕐 **Data updates every 5 minutes** due to system lag from Google Fit
- ☁️ **Google Cloud OAuth** integration to securely access Google Fit scopes
- 🔐 Uses **OAuth 2.0** credentials with `credentials.json` imported to access user health data
- ⚙️ **Only Kotlin apps** are allowed real-time data access via Google Fit
- 🌐 Sends structured health data to Spring Boot backend over REST
- 🧱 Powered by **Retrofit**, **Jetpack Compose**, and **modular Kotlin architecture**

---

## ⚙️ Tech Stack

| Component         | Tech Used                            |
|------------------|--------------------------------------|
| Language          | Kotlin                               |
| UI Toolkit        | Jetpack Compose                      |
| Data Access       | Google Fit API + OAuth 2.0           |
| Network Layer     | Retrofit + REST                      |
| Backend           | Spring Boot                          |
| Build System      | Gradle (Kotlin DSL)                  |

---

## 📁 Project Structure

```
.
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/fitnesstracker/
│           │   ├── MainActivity.kt
│           │   └── network/
│           │       ├── ApiManager.kt
│           │       ├── ApiService.kt
│           │       └── RetrofitInstance.kt
│           └── ui/theme/
│               ├── Color.kt
│               ├── Theme.kt
│               └── Type.kt
│       └── res/
│           └── AndroidManifest.xml
├── .idea
├── build.gradle.kts
├── settings.gradle.kts
├── proguard-rules.pro
├── gradlew / gradlew.bat
├── credentials.json   ← OAuth 2.0 credentials from Google Cloud
```

---

## 🔐 Google Cloud Setup

To access **Google Fit** data:

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or use an existing one
3. Enable **Google Fitness API**
4. Go to **APIs & Services > OAuth Consent Screen**
   - Set up the consent screen with required scopes
   - Add required OAuth scopes:
     - `https://www.googleapis.com/auth/fitness.heart_rate.read`
     - `https://www.googleapis.com/auth/fitness.activity.read`
5. Go to **Credentials > Create Credentials > OAuth 2.0 Client ID**
   - Choose **Android**
   - Download `credentials.json`
6. Place `credentials.json` in your project root or secure resource folder
7. Use Google Sign-In with the scopes to authorize access

---

## 🚀 Getting Started

### ✅ Prerequisites

- Android Studio
- A physical Android device with Google Fit installed
- Smartwatch synced with the same account
- Backend server URL from Spring Boot setup

### 🛠️ Setup Steps

```bash
# 1. Clone the repo
git clone https://github.com/your-username/fitness-tracker-kotlin.git
cd fitness-tracker-kotlin

# 2. Open in Android Studio and let Gradle sync

# 3. Add your credentials.json for Google Fit OAuth

# 4. Build and run on a physical Android device
```

---

## 📡 API Layer

**Retrofit-based** API layer for sending health data to the backend.

```kotlin
@POST("api/heartRate")
suspend fun sendHeartRate(@Body data: HeartRatePayload): Response<Unit>
```

Sample payload:
```json
{
  "userId": "devansh_01",
  "timestamp": "2025-06-10T22:15:00Z",
  "heartRate": 82
}
```

---

## ⏱️ Real-Time Limitations

Due to Google Fit's internal system design, **real-time data can only be fetched in ~5-minute intervals**. This is an inherent limitation of their API, especially when accessing through non-wearable Kotlin-based apps.

---

## 📊 Output

Once authenticated and connected:
- App listens to heart rate data from Google Fit (updated every 5 min)
- Displays latest readings
- Sends data to backend for further analytics and monitoring

---

## 🧪 Testing

```bash
./gradlew test
```

Unit test file:
```
src/test/java/com/example/fitnesstracker/ExampleUnitTest.kt
```

---

## 👨‍💻 Author

**Devansh Dhopte**  
Final Year B.Tech – IT  
Experienced in Kotlin, Android, Spring Boot, and CPS architectures.

---

> _"This project bridges wearable tech and enterprise analytics using real-time Kotlin Android development and cloud-backed APIs."_
