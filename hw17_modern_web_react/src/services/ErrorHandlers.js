export async function handleResponseError(response) {
    if (response.status >= 400 && response.status < 500) {
        const {error_type, message} = await response.json();
        console.log("error_type=" + error_type);
        console.log("message=" + message);
        alert(error_type + "\n" + message);
    } else {
        console.log(response);
    }
    throw new Error("HTTP error, status = " + response.status);
}

export function handleError(error) {
    console.log(error);
}