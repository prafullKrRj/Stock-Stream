<div align="center">
  <h1>📈 Stock Stream</h1>
  <p><em>A Modern Android Stock Trading Platform</em></p>
  
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose">
  <img src="https://img.shields.io/badge/Alpha%20Vantage-FF6B35?style=for-the-badge" alt="API">
</div>

---

## ✨ About

Stock Stream is a sleek Android application that brings real-time stock market data to your fingertips. Built with modern Android architecture and powered by Alpha Vantage API, it offers a seamless trading experience with beautiful Material 3 design.

## 🎯 Features

### 📊 **Market Intelligence**
- **Top Gainers & Losers** - Real-time market movers
- **Interactive Charts** - Beautiful price visualization with multiple time periods
- **Company Overview** - Comprehensive financial metrics

### 👀 **Smart Watchlists**
- **Multiple Watchlists** - Organize stocks by categories
- **Add/Remove Companies** - Simple watchlist management
- **Empty State Handling** - Elegant UI when no watchlists exist

### 🔍 **Advanced Search**
- **Symbol Search** - Find stocks with intelligent search
- **Recent Searches** - Quick access to previous queries
- **Search Results** - Sorted by relevance score

### 📰 **Market News**
- **Categorized News** - Technology, Finance, Crypto, Forex
- **Market Status** - Global market open/close information
- **Smart Caching** - Optimized content delivery

### 🎨 **Modern UI/UX**
- **Material 3 Design** - Latest Google design language
- **Dynamic Theming** - Light/Dark mode with system preference
- **Google Fonts** - Inter font family for crisp typography
- **Smooth Animations** - Polished user interactions

## 🏗️ Architecture

```
Clean Architecture + MVVM
├── Presentation Layer (Jetpack Compose)
├── Domain Layer (Use Cases & Models)  
└── Data Layer (Repository Pattern)
```

### 🛠️ **Tech Stack**
- **UI**: Jetpack Compose + Material 3
- **DI**: Koin
- **Database**: Room
- **Networking**: Retrofit + OkHttp
- **Caching**: Custom Cache Manager
- **Charts**: Custom Implementation
- **Architecture**: MVVM + Repository Pattern

## 📱 Screens

| Screen | Description |
|--------|-------------|
| **Home** | Top gainers/losers with market overview |
| **Watchlist** | Personal stock collections management |
| **Stock Detail** | Company info, charts, and watchlist actions |
| **Search** | Smart stock symbol search |
| **News** | Categorized market news and updates |
| **Settings** | Theme preferences and app configuration |

## 🚀 Getting Started

### Prerequisites
```bash
Android Studio Hedgehog | 2023.1.1+
Kotlin 1.9+
Min SDK: 24
Target SDK: 34
```

### Setup
1. **Clone the repository**
   ```bash
   git clone https://github.com/prafullKrRj/Stock-Stream.git
   ```

2. **Get Alpha Vantage API Key**
   - Visit [alphavantage.co](https://www.alphavantage.co/support/#api-key)
   - Get your free API key

3. **Add API Key**
   ```properties
   # local.properties
   API_KEY=your_api_key_here
   ```

4. **Build & Run**
   ```bash
   ./gradlew assembleDebug
   ```

## 📂 Project Structure

```
app/src/main/java/com/prafullkumar/stockstream/
├── data/
│   ├── cache/          # Caching mechanism
│   ├── local/          # Room database
│   ├── remote/         # API services
│   └── repository/     # Repository implementations
├── domain/
│   ├── models/         # Domain models
│   └── repository/     # Repository interfaces
├── presentation/
│   ├── screens/        # Compose screens & ViewModels
│   ├── theme/          # Material 3 theming
│   └── navigation/     # Navigation setup
└── di/                 # Dependency injection modules
```

## 🎨 Design Highlights

- **Gradient Backgrounds** - Subtle visual depth
- **Card-based Layout** - Clean content organization  
- **Consistent Spacing** - 16dp/24dp design system
- **Smart Typography** - Inter font with proper hierarchy
- **Loading States** - Skeleton screens and progress indicators
- **Error Handling** - User-friendly error messages with retry options

## 📊 API Integration

**Alpha Vantage Endpoints:**
```kotlin
// Market Data
GET /query?function=TOP_GAINERS_LOSERS

// Company Information  
GET /query?function=OVERVIEW&symbol={symbol}

// Stock Search
GET /query?function=SYMBOL_SEARCH&keywords={query}

// Price Data
GET /query?function=TIME_SERIES_DAILY&symbol={symbol}

// News Feed
GET /query?function=NEWS_SENTIMENT&topics={category}
```

## 💾 Data Management

- **Smart Caching** - Reduces API calls with expiration-based cache
- **Local Database** - Room for watchlist persistence
- **Offline Support** - Cached data available offline
- **Background Sync** - Efficient data synchronization

## 🔧 Performance

- **Lazy Loading** - Efficient list rendering
- **Image Optimization** - Memory-conscious image handling
- **API Rate Limiting** - Respectful API usage
- **Background Processing** - Non-blocking operations

## 📱 Download

[📲 Download APK](https://github.com/prafullKrRj/Stock-Stream/releases)

## 👨‍💻 Developer

**Prafull Kumar**  
[GitHub](https://github.com/prafullKrRj) • [LinkedIn](https://linkedin.com/in/prafullkrRj)

---

<div align="center">
  <p>Built with ❤️ using Jetpack Compose</p>
  <p><sub>⭐ Star this repo if you found it helpful!</sub></p>
</div>
