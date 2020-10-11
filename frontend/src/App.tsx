import React, { useState } from "react";
import "./App.css";

import Joke from "./components/Joke";
import Hello from "./components/Hello";
import User from './components/User';

function App() {
  const [fetchedData, setFetchedData] = useState("Fetch something...");

  function fetchedDataHandler(data: string) {
    setFetchedData(data);
  }

  return (
    <div className="App">
      <header className="App-header">
        <div>{fetchedData}</div>
        <Joke dataHandler={fetchedDataHandler} />
        <Hello dataHandler={fetchedDataHandler} />
        <User />
      </header>
    </div>
  );
}

export default App;
