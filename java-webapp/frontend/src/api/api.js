import {serverURL} from "../index";

export async function getBooks() {
    let urlBooks = new URL(`${serverURL}/books?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    let booksResponse = await fetch(urlBooks.toString())
    return await booksResponse.json();
}

export async function getStats() {
    let urlStats = new URL(`${serverURL}/stats?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    let statsResponse = await fetch(urlStats.toString())
    return await statsResponse.json()
}

export async function getRequests() {
    let urlRequest = new URL(`${serverURL}/request?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    let requestsResponse = await fetch(urlRequest.toString())
    return await requestsResponse.json();
}

export async function getReturnRequests() {
    let urlReturnRequests = new URL(`${serverURL}/return_request?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    let returnRequestsResponse = await fetch(urlReturnRequests.toString())
    return await returnRequestsResponse.json()
}

export async function getDeliveryTypes() {
    let urlDeliveryTypes = new URL(`${serverURL}/delivery_types?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    let deliveryTypesResponse = await fetch(urlDeliveryTypes.toString())
    return await deliveryTypesResponse.json()
}

export async function getHistory() {
    let urlHistory = new URL(`${serverURL}/stats/history?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    let historyResponse = await fetch(urlHistory.toString())
    return await historyResponse.json()
}