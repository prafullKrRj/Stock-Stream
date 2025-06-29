<div align="center">
  <h1>📈 Stock Stream</h1>
  <p><em>A Modern Android Stock Trading Platform</em></p>
  
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose">
  <img src="https://img.shields.io/badge/Alpha%20Vantage-FF6B35?style=for-the-badge" alt="API">
</div>

---

## ✨ About

Stock Stream is a sleek Android application that brings real-time stock market data to your fingertips. Built with modern Android architecture and powered by Alpha Vantage API, it offers a seamless trading experience with beautiful UI and powerful features.

## 📱 Download

[📲 Download APK](https://drive.google.com/file/d/1OsOf8fJoAcj0EMiPear5x5ncHbSaL2Qc/view?usp=sharing)
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
MVVM
├── Presentation Layer (Jetpack Compose)
├── Domain Layer (Use Cases & Models)  
└── Data Layer (Repository Pattern)
└── DI Layer
```

### 🛠️ **Tech Stack**
- **UI**: Jetpack Compose + Material 3
- **DI**: Koin
- **Database**: Room, Datastore
- **Networking**: Retrofit + OkHttp
- **Caching**: Custom Cache Manager
- **Charts**: ehsannarmani/ComposeCharts
- **Architecture**: MVVM + Repository Pattern

## 📱 Screens

| Screen | Description |
|--------|-------------|
| **Home** | Top gainers/losers with market overview |
| **Watchlist** | Personal stock collections management |
| **Stock Detail** | Company info, charts, and watchlist actions |
| **Search** | Smart stock symbol search |
| **News** | Categorized market news and updates |
| **Settings** | Theme preferences |

## 📱 App Screenshots

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="https://raw.githubusercontent.com/prafullKrRj/Stock-Stream/master/ss/1_home.jpg" alt="Home Screen" width="280" style="border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);"/>
        <br><strong>Home Screen</strong>
        <br><sub>Market overview with top gainers & losers</sub>
      </td>
      <td align="center">
        <img src="https://raw.githubusercontent.com/prafullKrRj/Stock-Stream/master/ss/2_companyOverview.jpg" alt="Company Overview" width="280" style="border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);"/>
        <br><strong>Company Overview</strong>
        <br><sub>Detailed company information & metrics</sub>
      </td>
    </tr>
    <tr>
      <td align="center">
        <img src="https://raw.githubusercontent.com/prafullKrRj/Stock-Stream/master/ss/3_addToWatchList.jpg" alt="Add to Watchlist" width="280" style="border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);"/>
        <br><strong>Add to Watchlist</strong>
        <br><sub>Manage your stock watchlists</sub>
      </td>
      <td align="center">
        <img src="https://raw.githubusercontent.com/prafullKrRj/Stock-Stream/master/ss/4_search.jpg" alt="Search" width="280" style="border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);"/>
        <br><strong>Search</strong>
        <br><sub>Intelligent stock symbol search</sub>
      </td>
    </tr>
    <tr>
      <td align="center">
        <img src="https://raw.githubusercontent.com/prafullKrRj/Stock-Stream/master/ss/5_news.jpg" alt="News Feed" width="280" style="border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);"/>
        <br><strong>News Feed</strong>
        <br><sub>Latest market news & updates</sub>
      </td>
      <td align="center">
        <img src="https://raw.githubusercontent.com/prafullKrRj/Stock-Stream/master/ss/6_watchlists.jpg" alt="Watchlists" width="280" style="border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);"/>
        <br><strong>Watchlists</strong>
        <br><sub>Multiple watchlist management</sub>
      </td>
    </tr>
    <tr>
      <td align="center">
        <img src="https://raw.githubusercontent.com/prafullKrRj/Stock-Stream/master/ss/7_companies.jpg" alt="Companies" width="280" style="border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);"/>
        <br><strong>Companies</strong>
        <br><sub>Browse company listings</sub>
      </td>
      <td align="center">
        <img src="https://raw.githubusercontent.com/prafullKrRj/Stock-Stream/master/ss/8_settings.jpg" alt="Settings" width="280" style="border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);"/>
        <br><strong>Settings</strong>
        <br><sub>App preferences & configuration</sub>
      </td>
    </tr>
  </table>
</div>

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
│   ├── remote/         # API services, DTOs, Mappers
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

- **Card-based Layout** - Clean content organization  
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


## 👨‍💻 Developer

**Prafull Kumar**  
[GitHub](https://github.com/prafullKrRj) • [LinkedIn](https://linkedin.com/in/prafullkrRj)

---

<div align="center">
  <p>Built with ❤️ using Jetpack Compose</p>
  <p><sub>⭐ Star this repo if you found it helpful!</sub></p>
</div>
