import React from "react";

interface SecretProps {
  dataHandler: (data: string) => void;
}

function Secret(props: SecretProps) {
  async function fetchHandler() {
    const response = await fetch("/api/secret/");
    const secret = await response.text();
    props.dataHandler(secret);
  }

  return (
    <div>
      <button onClick={fetchHandler}>Fetch a Secret</button>
    </div>
  );
}

export default Secret;
