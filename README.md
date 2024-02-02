# Android app currency exchange rate using openexchange APIs
Using openexchange APIs to create an app that shows latest exchange rate to other currencies. The app uses MVVM design pattern with clean architecture best practices and the following libraries
- Retrofit to make API calls.
- Room database to store likes.
- Jetpack compose for UI
- Dagger/Hilt for dependency injection.
  
### Features
- Shows a list of latest exchange rate in all currecies given a currency as input and the amount you want to exchange.
  
### APIs
- Fetch the latest exchange rate - https://openexchangerates.org/api/latest.json
- Fetch the list of supported currencies - https://openexchangerates.org/api/currencies.json
  
## Demo
Entering an amount and selecting a currency. Shows the exchange rate of all the other currencies
![](https://github.com/pankaj6apr/currency/blob/main/currency.gif)
