
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
    const body = await res.json()

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

async function post(url, body = null,showAlert=false) {
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