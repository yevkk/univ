export function convertDatetime(datetime) {
    let year = datetime.date.year
    let month = datetime.date.month < 10 ? '0' + datetime.date.month : datetime.date.month
    let day = datetime.date.day < 10 ? '0' + datetime.date.day : datetime.date.day
    let hour = datetime.time.hour < 10 ? '0' + datetime.time.hour : datetime.time.hour
    let minute = datetime.time.minute < 10 ? '0' + datetime.time.minute : datetime.time.minute

    return [year, month, day].join('-') + ' ' + [hour, minute].join(':')
}