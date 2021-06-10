export let serverURL = 'http://192.168.1.26:8010'

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

export async function getHistoryByBookID(bookID) {
    let urlHistory = new URL(`${serverURL}/stats/history?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    urlHistory.searchParams.set('book_id', bookID)
    let historyResponse = await fetch(urlHistory.toString())
    return await historyResponse.json()
}

export async function getHistoryInPeriod(start, end) {
    let urlHistory = new URL(`${serverURL}/stats/history?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    urlHistory.searchParams.set('period_start', start)
    urlHistory.searchParams.set('period_end', end)
    let historyResponse = await fetch(urlHistory.toString())
    return await historyResponse.json()
}

export async function getBalanceChangelog() {
    let urlLog = new URL(`${serverURL}/changelog/balance?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    let historyResponse = await fetch(urlLog.toString())
    return await historyResponse.json()
}

export async function getBalanceChangelogByBookID(bookID) {
    let urlLog = new URL(`${serverURL}/changelog/balance?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    urlLog.searchParams.set('book_id', bookID)
    let logResponse = await fetch(urlLog.toString())
    return await logResponse.json()
}

export async function getBalanceChangelogInPeriod(start, end) {
    let urlLog = new URL(`${serverURL}/changelog/balance?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    urlLog.searchParams.set('period_start', start)
    urlLog.searchParams.set('period_end', end)
    let logResponse = await fetch(urlLog.toString())
    return await logResponse.json()
}

export async function getRateChangelog() {
    let urlLog = new URL(`${serverURL}/changelog/rate?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    let historyResponse = await fetch(urlLog.toString())
    return await historyResponse.json()
}

export async function getRateChangelogByBookID(bookID) {
    let urlLog = new URL(`${serverURL}/changelog/rate?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    urlLog.searchParams.set('book_id', bookID)
    let logResponse = await fetch(urlLog.toString())
    return await logResponse.json()
}

export async function getRateChangelogInPeriod(start, end) {
    let urlLog = new URL(`${serverURL}/changelog/rate?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)
    urlLog.searchParams.set('period_start', start)
    urlLog.searchParams.set('period_end', end)
    let logResponse = await fetch(urlLog.toString())
    return await logResponse.json()
}