import React, { Fragment, useState, useEffect, SyntheticEvent } from "react";

import { userUrl } from "./constants";
import TableRow from "./TableRow";
import { UserInterface } from "./UserInterface";

function User() {
  const [users, setUsers] = useState<UserInterface[] | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [hasPrevious, setHasPrevious] = useState(false);
  const [hasNext, setHasNext] = useState(false);
  const [isDataLoaded, setIsDataLoaded] = useState(false);

  useEffect(() => {
    if (!isDataLoaded) {
      setIsDataLoaded(true);
      getUserData();
    }
  });

  async function getUserData() {
    const fetchUrl = userUrl + (currentPage ? `?pageNo=${currentPage}` : "");

    const response = await fetch(fetchUrl);
    if (response.status !== 200) {
      alert("Error fetching: " + response.statusText);
      return;
    }
    const responseData = await response.json();
    setUsers(responseData.data.length && responseData.data);
    setCurrentPage(responseData.pageNo ?? 0);
    setTotalPages(responseData.totalPages ?? 0);
    setHasPrevious(responseData.hasPrevious);
    setHasNext(responseData.hasNext);
  }

  async function addUserHandler(e: SyntheticEvent) {
    e.preventDefault();
    const data = new FormData(document.forms[0] as HTMLFormElement);

    await fetch(userUrl, {
      method: "POST",
      body: data,
    });
    setIsDataLoaded(false);
  }

  function getPreviousPage() {
    if (hasPrevious) {
      setIsDataLoaded(false);
      setCurrentPage(currentPage - 1);
    }
  }
  function getNextPage() {
    if (hasNext) {
      setIsDataLoaded(false);
      setCurrentPage(currentPage + 1);
    }
  }

  async function deleteHandler(id: string) {
    await fetch(`${userUrl}/${id}`, {
      method: "DELETE",
    });
    setIsDataLoaded(false);
  }

  return (
    <Fragment>
      <Fragment>
        <button onClick={getUserData}>Fetch users</button>
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
                deleteHandler={deleteHandler}
              />
            ))}
        </tbody>
      </table>
      <button onClick={getPreviousPage} disabled={!hasPrevious}>
        &lt;
      </button>
      Page {currentPage + 1} of {totalPages}
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
