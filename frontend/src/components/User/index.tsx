import React, { Fragment, useState, SyntheticEvent } from "react";

import { userUrl } from "./constants";
import TableRow from "./TableRow";
import { UserInterface } from "./UserInterface";

function User() {
  const [users, setUsers] = useState<UserInterface[] | null>(null);

  async function getUserHandler() {
    const response = await fetch(userUrl);
    if (response.status !== 200) {
      alert("Error fetching: " + response.statusText);
      return;
    }
    const responseData = await response.json();
    setUsers(responseData.data);
    setCurrentPage(responseData.pageNo ?? 0);
    setTotalPages(responseData.totalPages ?? 0);
    responseData.hasPrevious && setHasPrevious(true);
    responseData.hasNext && setHasNext(true);
  }

  async function addUserHandler(e: SyntheticEvent) {
    e.preventDefault();
    const data = new FormData(document.forms[0] as HTMLFormElement);

    const response = await fetch(userUrl, {
      method: "POST",
      body: data,
    });
    getUserHandler();
  }

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [hasPrevious, setHasPrevious] = useState(false);
  const [hasNext, setHasNext] = useState(false);
  function getPreviousPage() {}
  function getNextPage() {}

  return (
    <Fragment>
      <Fragment>
        <button onClick={getUserHandler}>Fetch users</button>
      </Fragment>
      <Fragment>
        <form action={userUrl} id="form">
          <input type="text" name="firstName" placeholder="first name" />
          <input type="text" name="lastName" placeholder="last name" />
          <button type="submit" onClick={addUserHandler}>
            Add user
          </button>
        </form>
      </Fragment>
      <table style={styles.usersBlock}>
        <thead>
          <tr>
            <th>id</th>
            <th>First name</th>
            <th>Last name</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {Array.isArray(users) &&
            users.length &&
            users.map((user) => (
              <TableRow
                key={user.id}
                firstName={user.firstName}
                lastName={user.lastName}
                id={user.id}
              />
            ))}
        </tbody>
      </table>
      <button onClick={getPreviousPage} disabled={!hasPrevious}>
        &lt;
      </button>
      Page {currentPage} of {totalPages}
      <button onClick={getNextPage} disabled={!hasNext}>
        &gt;
      </button>
    </Fragment>
  );
}

const styles = {
  usersBlock: {
    border: "1px solid grey",
    minWidth: 300,
    minHeight: 100,
    marginLeft: "auto",
    marginRight: "auto",
  } as React.CSSProperties,
};

export default User;
