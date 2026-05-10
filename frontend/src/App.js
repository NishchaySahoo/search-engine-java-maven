import React, { useState } from 'react';
import './App.css';

function App() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);

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
      <div className="header">
        <h1>🔍 Java Search Engine</h1>
        <p>Built from scratch with Java, Lucene & PageRank</p>
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