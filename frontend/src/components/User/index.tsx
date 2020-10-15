import React, { Fragment, useState, SyntheticEvent } from "react";

function User() {
  const [users, setUsers] = useState([""]);

  async function getUserHandler() {
    const response = await fetch("/api/v1/get");
    const data = await response.json();
    const usersList = await data.join(", ");

    setUsers(usersList);
  }
  async function addUserHandler(e: SyntheticEvent) {
    e.preventDefault();
    const data = new FormData(document.forms[0] as HTMLFormElement);

    const response = await fetch("/api/v1/create", {
      method: "POST",
      body: data,
    });
    getUserHandler();
  }
  return (
    <Fragment>
      <div style={styles.usersBlock}>{users}</div>
      <div>
        <button onClick={getUserHandler}>Fetch users</button>
      </div>
      <div>
        <form action="/api/v1/create" id="form">
          <input type="text" name="name" />
          <button type="submit" onClick={addUserHandler}>
            Add user
          </button>
        </form>
      </div>
    </Fragment>
  );
}
const styles = {
  usersBlock: {
    border: "1px solid grey",
    minWidth: 300,
    minHeight: 100,
  } as React.CSSProperties,
};
export default User;
