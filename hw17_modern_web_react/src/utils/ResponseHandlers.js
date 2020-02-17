import {ApiResponseError, HttpResponseError} from "./ResponseError";

export function getResponseAndJson(response) {
    console.log(response.ok);
    return response.json().then(json => {
            return {"response": response, "json": json}
        },
        error => {
            if (response.ok) {
                throw new Error(error)
            } else {
                throw new HttpResponseError(response);
            }
        })
}

export function checkResponseAndJson(obj) {
    return customCheckResponseAndJson(obj, response => response.ok);
}

export function customCheckResponseAndJson(obj, checkPredicate) {
    console.log(obj);
    const {response, json} = obj;
    if (checkPredicate(response)) {
        return json;
    } else {
        throw new ApiResponseError(response, json);
    }
}