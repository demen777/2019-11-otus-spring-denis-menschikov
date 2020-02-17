export class HttpResponseError extends Error {
    constructor(response) {
        super();
        this.response = response;
        this.message = "http_code=" + response.status + " statusText=" + response.statusText;
    }
}

export class ApiResponseError extends HttpResponseError {
    constructor(response, json) {
        super(response);
        console.log(response, json);
        this.json = json;
        this.message = json.error + "\n"  + json.message;
    }
}

