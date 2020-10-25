import React from "react";
import { UserInterface } from "./UserInterface";

function TableRow(props: UserInterface) {
  return (
    <tr>
      <td>{props.id}</td>
      <td>{props.firstName}</td>
      <td>{props.lastName}</td>
      <td>
        <button>Remove={props.id}</button>
      </td>
    </tr>
  );
}

export default TableRow;
