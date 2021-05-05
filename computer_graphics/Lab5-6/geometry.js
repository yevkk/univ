'use strict'

let points = []


function QuickHull(points) {
    if (points.length === 0) {
        return []
    }
    let leftMost = points.reduce((current, point) => {
        return (current.x > point.x) ? point : current
    }, points[0])
    let rightMost = points.reduce((current, point) => {
        return (current.x < point.x) ? point : current
    }, points[0])
    let pointsDown = points.filter(point => point.relativeY(leftMost, rightMost) >= 0)
    let pointsUp = points.filter(point => point.relativeY(leftMost, rightMost) <= 0)
    let resUp = []
    let resDown = []

    QuickHull.step(pointsDown, resUp, leftMost, rightMost, 'down')
    QuickHull.step(pointsUp, resDown, leftMost, rightMost, 'up')

    return [leftMost].concat(resUp.sort((point1, point2) => (point1.x - point2.x)), rightMost, resDown.sort((point1, point2) => (point2.x - point1.x)))
}

QuickHull.step = function (points, res, leftSep, rightSep, mode) {
    if (points.length === 0) {
        return
    }
    let newPoint = points.reduce((current, point) => {
        return (Math.abs(current.relativeY(leftSep, rightSep)) < Math.abs(point.relativeY(leftSep, rightSep))) ? point : current
    }, points[0])
    res.push(newPoint)

    switch (mode) {
        case 'up':
            QuickHull.step(points.filter(point => point.relativeY(leftSep, newPoint) < 0), res, leftSep, newPoint, mode)
            QuickHull.step(points.filter(point => point.relativeY(newPoint, rightSep) < 0), res, newPoint, rightSep, mode)
            break
        case 'down':
            QuickHull.step(points.filter(point => point.relativeY(leftSep, newPoint) > 0), res, leftSep, newPoint, mode)
            QuickHull.step(points.filter(point => point.relativeY(newPoint, rightSep) > 0), res, newPoint, rightSep, mode)
            break
    }
}

function JarvisConvexHull(points) {
    if (points.length === 0) {
        return []
    } else if (points.length === 1) {
        return [...points]
    }

    let rightMost = points.reduce((current, point) => {
        return (current.x < point.x) ? point : current
    }, points[0])
    let res = [rightMost]

    for (let i = 0; ; i++) {
        let next = points.reduce((current, point, index) => {
            if (point === res[i]) {
                return current
            } else {
                return ((current.x - res[i].x) * (point.y - res[i].y) - (point.x - res[i].x) * (current.y - res[i].y)) > 0 ? point : current
            }
        }, points[0])
        if (next === rightMost) {
            break
        } else {
            res.push(next)
        }
    }

    return res
}