import React from "react";

export default function AttributeRow(props) {
    return (
        <tr>
            <th scope="row" width="10%">{props.name}</th>
            <td>{props.value}</td>
        </tr>
    );
}