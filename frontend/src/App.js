import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);
  const [weather, setWeather] = useState(null);
  const [time, setTime] = useState(new Date());
  const [location, setLocation] = useState('');
  const [quote, setQuote] = useState(null);

  useEffect(() => {
      // Update time every second
      const timer = setInterval(() => setTime(new Date()), 1000);

      // Get weather
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(async (position) => {
          const { latitude, longitude } = position.coords;
          try {
            const res = await fetch(
              `https://api.open-meteo.com/v1/forecast?latitude=${latitude}&longitude=${longitude}&current_weather=true`
            );
            const data = await res.json();
            const weatherCode = data.current_weather.weathercode;
            const temp = data.current_weather.temperature;

            // Get city name
            const geoRes = await fetch(
              `https://nominatim.openstreetmap.org/reverse?lat=${latitude}&lon=${longitude}&format=json`
            );
            const geoData = await geoRes.json();
            const city = geoData.address.city || geoData.address.town || geoData.address.village || 'Your City';

            setLocation(city);
            setWeather({
              temp,
              condition: getWeatherCondition(weatherCode)
            });
          } catch (err) {
            console.error('Weather fetch failed:', err);
          }
        });
      }

      return () => clearInterval(timer);
    }, []);

    useEffect(() => {
      const handleMouseMove = (e) => {
        const blob1 = document.getElementById('blob1');
        const blob2 = document.getElementById('blob2');
        const blob3 = document.getElementById('blob3');

        if (blob1) {
          blob1.style.left = e.clientX - 200 + 'px';
          blob1.style.top = e.clientY - 200 + 'px';
        }
        if (blob2) {
          blob2.style.left = e.clientX - 150 + 'px';
          blob2.style.top = e.clientY - 300 + 'px';
        }
        if (blob3) {
          blob3.style.left = e.clientX - 300 + 'px';
          blob3.style.top = e.clientY - 100 + 'px';
        }
      };

      window.addEventListener('mousemove', handleMouseMove);
      return () => window.removeEventListener('mousemove', handleMouseMove);
    }, []);

    useEffect(() => {
      fetch('https://uselessfacts.jsph.pl/api/v2/facts/random?language=en')
        .then(res => res.json())
        .then(data => {
          setQuote({
            text: data.text,
            author: 'Fun Fact of the Day'
          });
        })
        .catch(err => console.error('Quote fetch failed:', err));
    }, []);

    const getWeatherCondition = (code) => {
      if (code === 0) return '☀️ Clear';
      if (code <= 3) return '⛅ Cloudy';
      if (code <= 67) return '🌧️ Rainy';
      if (code <= 77) return '❄️ Snowy';
      if (code <= 99) return '⛈️ Stormy';
      return '🌤️ Mixed';
    };

  const handleSearch = async () => {
    if (!query.trim()) return;
    setLoading(true);
    setSearched(true);

    try {
      const response = await fetch(
        `http://localhost:8080/api/search?query=${encodeURIComponent(query)}`
      );
      const data = await response.json();
      setResults(data);
    } catch (error) {
      console.error('Search failed:', error);
    }

    setLoading(false);
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') handleSearch();
  };

  return (
    <div className="app">

    <div className="blob" id="blob1"></div>
    <div className="blob" id="blob2"></div>
    <div className="blob" id="blob3"></div>

      <div className="header">
        <h1>NexaSearch</h1>
        <p>Intelligent search powered by Java, Lucene & PageRank</p>
      </div>

      <div className="search-box">
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Search Wikipedia..."
        />
        <button onClick={handleSearch}>Search</button>
      </div>

      <div className="info-bar">
        <div className="info-item">
          <span className="info-label">Time</span>
          <span className="info-value">{time.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}</span>
        </div>
        {location && (
          <div className="info-item">
            <span className="info-label">Location</span>
            <span className="info-value">📍 {location}</span>
          </div>
        )}
        {weather && (
          <div className="info-item">
            <span className="info-label">Weather</span>
            <span className="info-value">{weather.condition}</span>
            <span className="info-label">{weather.temp}°C</span>
          </div>
        )}
      </div>

      {quote && (
        <div className="quote-box">
          <p className="quote-text">"{quote.text}"</p>
          <p className="quote-author">— {quote.author}</p>
        </div>
      )}

      {loading && <p className="loading">Searching...</p>}

      {!loading && searched && results.length === 0 && (
        <p className="no-results">No results found.</p>
      )}

      <div className="results">
        {results.map((result, index) => (
          <div className="result-card" key={index}>
            <span className="rank">#{index + 1}</span>
            <div className="result-info">
              <a href={result.url} target="_blank" rel="noreferrer">
                {result.title}
              </a>
              <p className="url">{result.url}</p>
              <div className="scores">
                <span>Relevance: {result.luceneScore.toFixed(3)}</span>
                <span>PageRank: {result.pageRankScore.toFixed(4)}</span>
                <span>Combined: {result.combinedScore.toFixed(3)}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default App;