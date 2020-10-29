import React, { useState, useEffect, SyntheticEvent } from "react";
import "./App.css";

import Joke from "./components/Joke";
import Hello from "./components/Hello";
import User from "./components/User";
import Secret from "./components/Secret";
import { OperationsInterface } from "./OperationsInterface";
import { getCookie } from "./utils/getCookieByName";

function App() {
  const [fetchedData, setFetchedData] = useState("Fetch something...");
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [
    availableOperations,
    setAvailableOperations,
  ] = useState<OperationsInterface | null>(null);
  const [isDataLoaded, setIsDataLoaded] = useState(false);

  const loginUrl = "/login";
  const logoutUrl = "/logout";

  useEffect(() => {
    if (!isDataLoaded) {
      setIsDataLoaded(true);
      fetchOperations();
      fetchStatus();
    }
  }, [isDataLoaded]);

  async function fetchOperations() {
    const response = await fetch("/api/account/operations/");
    const operations = await response.json();
    setAvailableOperations(operations);
  }

  async function fetchStatus() {
    const response = await fetch("/api/account/status/");
    const responseJson = await response.json();
    const isAuthenticated = responseJson.isAuthenticated;
    setIsLoggedIn(isAuthenticated);
  }

  function fetchedDataHandler(data: string) {
    setFetchedData(data);
  }

  async function loginHandler(e: SyntheticEvent) {
    e.preventDefault();
    const username = document.querySelector(
      "#login input[name=username]"
    ) as HTMLInputElement;
    const password = document.querySelector(
      "#login input[name=password]"
    ) as HTMLInputElement;
    const cookie = getCookie("XSRF-TOKEN") ?? "";
    const data = {
      username: username.value,
      password: password.value,
    };
    const res = await fetch(loginUrl, {
      method: "POST",
      headers: {
        "X-XSRF-TOKEN": cookie,
      },
      body: JSON.stringify(data),
    });

    if (res.status === 200) {
      setIsDataLoaded(false);
    } else {
      alert("Wrong credentials");
    }
  }

  async function logoutHandler(e: SyntheticEvent) {
    e.preventDefault();
    const cookie = getCookie("XSRF-TOKEN") ?? "";
    await fetch(logoutUrl, {
      method: "POST",
      headers: {
        "X-XSRF-TOKEN": cookie,
      },
    });
    setIsDataLoaded(false);
  }

  return (
    <div className="App">
      <header className="header">Spring + React example</header>
      <div className="body">
        <div>{fetchedData}</div>
        {availableOperations && availableOperations["joke"] && (
          <Joke dataHandler={fetchedDataHandler} />
        )}
        {availableOperations && availableOperations["hello"] && (
          <Hello dataHandler={fetchedDataHandler} />
        )}
        {availableOperations && availableOperations["secret"] && (
          <Secret dataHandler={fetchedDataHandler} />
        )}
        {isLoggedIn ? (
          <form action={logoutUrl}>
            <button type="submit" value="logout" onClick={logoutHandler}>
              Logout
            </button>
          </form>
        ) : (
          <form action={loginUrl} id="login">
            <input type="text" name="username" placeholder="username" />
            <input type="password" name="password" placeholder="password" />
            <button type="submit" value="login" onClick={loginHandler}>
              Login
            </button>
          </form>
        )}
        <hr />
        <User />
      </div>
    </div>
  );
}

export default App;
