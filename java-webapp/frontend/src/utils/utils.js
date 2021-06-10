export function convertDatetime(datetime) {
    let year = datetime.date.year
    let month = datetime.date.month < 10 ? '0' + datetime.date.month : datetime.date.month
    let day = datetime.date.day < 10 ? '0' + datetime.date.day : datetime.date.day
    let hour = datetime.time.hour < 10 ? '0' + datetime.time.hour : datetime.time.hour
    let minute = datetime.time.minute < 10 ? '0' + datetime.time.minute : datetime.time.minute

    return [year, month, day].join('-') + ' ' + [hour, minute].join(':')
}

export function convertDate(date) {
    let year = date.year
    let month = date.month < 10 ? '0' + date.month : date.month
    let day = date.day < 10 ? '0' + date.day : date.day

    return [year, month, day].join('-')
}