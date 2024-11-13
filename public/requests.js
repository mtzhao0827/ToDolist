
function getHeaders() {
    const headers = {
        'Content-Type': 'application/json',
    }
    const token = window.localStorage.getItem('token');
    if (token) {
        headers["Authorization"] = `Bearer ${token}`
    }
    return headers
}

async function errorHandler(res, showAlert) {
    const bodyText = await res.text()
    let body = bodyText

    if (bodyText && bodyText.trim().startsWith("{")) {
        body = JSON.parse(bodyText)
    }

    if (!res.ok) {
        showAlert && alert(`${res.statusText}: ${body.message}`)
        throw new Error(body.message)
    }

    return body
}

async function get(url, param = null, showAlert = false) {
    if (param) {
        const params = new URLSearchParams(param)
        url = `${url}?${params.toString()}`
    }

    const res = await fetch(url, {
        method: "GET",
        headers: getHeaders(),
    });

    return errorHandler(res, showAlert)
}

async function post(url, body = null, showAlert= false) {
    const opt = {
        method: 'POST',
        headers: getHeaders(),
    }

    if (body) {
        opt.body = JSON.stringify(body)
    }

    const res = await fetch(url,opt);

    return errorHandler(res, showAlert)
}

async function del(url, body = null, showAlert = false){
    const res = await fetch(url, {
        method: 'DELETE',
        headers: getHeaders(),
    });

    return errorHandler(res, showAlert)
}

async function patch(url, body = null, showAlert = false){
    const opt = {
        method: 'PATCH',
        headers: getHeaders(),
    }

    if (body) {
        opt.body = JSON.stringify(body)
    }

    const res = await fetch(url,opt);

    return errorHandler(res, showAlert)
}

async function upload(url, element = null, showAlert = false){
    const token = window.localStorage.getItem('token');

    const headers = {
        'Authorization': `Bearer ${token}`,
    }

    const formData = new FormData();

    for (const file of element.files) {
        formData.append("file", file);
    }

    const opt = {
        method: 'POST',
        headers: headers,
        body: formData,
    }

    const res = await fetch(url,opt);

    return errorHandler(res, showAlert)

}

async function getUrl(url, element = null, showAlert = false) {
    const opt = {
        method: 'PATCH',
        headers: getHeaders(),
        body: element,
    }

    const res = await fetch(url,opt);

    return errorHandler(res, showAlert)

}