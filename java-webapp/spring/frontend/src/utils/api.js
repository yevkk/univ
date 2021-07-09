export let serverURL = 'http://localhost:8015'

export async function getBooks() {
    let urlBooks = new URL(`${serverURL}/books`)
    let booksResponse = await fetch(urlBooks.toString())
    return await booksResponse.json();
}

export async function getStats() {
    let urlStats = new URL(`${serverURL}/stats`)
    let statsResponse = await fetch(urlStats.toString())
    return await statsResponse.json()
}

export async function getRequests() {
    let urlRequest = new URL(`${serverURL}/requests`)
    let requestsResponse = await fetch(urlRequest.toString())
    return await requestsResponse.json();
}

export async function getRequestsByUser(id) {
    let urlRequest = new URL(`${serverURL}/requests?user_id=${id}`)
    let requestsResponse = await fetch(urlRequest.toString())
    return await requestsResponse.json();
}

export async function getReturnRequests() {
    let urlReturnRequests = new URL(`${serverURL}/return_requests`)
    let returnRequestsResponse = await fetch(urlReturnRequests.toString())
    return await returnRequestsResponse.json()
}

export async function getReturnRequestsByUser(id) {
    let urlReturnRequests = new URL(`${serverURL}/return_requests?user_id=${id}`)
    let returnRequestsResponse = await fetch(urlReturnRequests.toString())
    return await returnRequestsResponse.json()
}

export async function getDeliveryTypes() {
    let urlDeliveryTypes = new URL(`${serverURL}/delivery_types`)
    let deliveryTypesResponse = await fetch(urlDeliveryTypes.toString())
    return await deliveryTypesResponse.json()
}

export async function getBalanceChangelog() {
    let urlLog = new URL(`${serverURL}/balance_log`)
    let historyResponse = await fetch(urlLog.toString())
    return await historyResponse.json()
}

export async function getBalanceChangelogByBookID(bookID) {
    let urlLog = new URL(`${serverURL}/balance_log`)
    urlLog.searchParams.set('book_id', bookID)
    let logResponse = await fetch(urlLog.toString())
    return await logResponse.json()
}

export async function getBalanceChangelogInPeriod(start, end) {
    let urlLog = new URL(`${serverURL}/balance_log`)
    urlLog.searchParams.set('from', start)
    urlLog.searchParams.set('to', end)
    let logResponse = await fetch(urlLog.toString())
    return await logResponse.json()
}

export async function getRateChangelog() {
    let urlLog = new URL(`${serverURL}/rate_log`)
    let historyResponse = await fetch(urlLog.toString())
    return await historyResponse.json()
}

export async function getRateChangelogByBookID(bookID) {
    let urlLog = new URL(`${serverURL}/rate_log`)
    urlLog.searchParams.set('book_id', bookID)
    let logResponse = await fetch(urlLog.toString())
    return await logResponse.json()
}

export async function getRateChangelogInPeriod(start, end) {
    let urlLog = new URL(`${serverURL}/rate_log`)
    urlLog.searchParams.set('from', start)
    urlLog.searchParams.set('to', end)
    let logResponse = await fetch(urlLog.toString())
    return await logResponse.json()
}