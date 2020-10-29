import React from "react";
import { UserInterface } from "./UserInterface";

interface UserRowInterface extends UserInterface {
  deleteHandler(id: string): void;
}

function TableRow(props: UserRowInterface) {
  return (
    <tr>
      <td>{props.id}</td>
      <td>{props.firstName}</td>
      <td>{props.lastName}</td>
      <td>
        <button
          onClick={() => {
            props.deleteHandler(props.id);
          }}
          disabled={props.disabled}
        >
          Remove
        </button>
      </td>
    </tr>
  );
}

export default TableRow;
