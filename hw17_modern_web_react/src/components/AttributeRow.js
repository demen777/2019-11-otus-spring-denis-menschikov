import React from "react";

export default function AttributeRow(props) {
    return (
        <tr>
            <td className="header">{props.name}</td>
            <td>{props.value}</td>
        </tr>
    );
}