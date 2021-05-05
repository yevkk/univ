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
    let pointsUp = points.filter(point => point.relativeY(leftMost, rightMost) >= 0)
    let pointDown = points.filter(point => point.relativeY(leftMost, rightMost) <= 0)
    let resUp = [leftMost, rightMost]
    let resDown = [leftMost, rightMost]

    QuickHull.step(pointsUp, resUp, 1)

    return resUp
}

QuickHull.step = function (points, res, sepRightI) {
    if (points.length === 2) {
        return
    }
    let newPoint = points.reduce((current, point) => {
        return (Math.abs(current.relativeY(points[sepRightI - 1], point[sepRightI])) < Math.abs(point.relativeY(points[sepRightI - 1], point[sepRightI]))) ? point : current
    }, points[0])

    res.splice(sepRightI, 0, newPoint)
    QuickHull.step(points.filter(point =>point.relativeY(points[sepRightI - 1], point[sepRightI]) < 0), res, sepRightI)
    QuickHull.step(points.filter(point =>point.relativeY(points[sepRightI], point[sepRightI + 1]) < 0), res, sepRightI + 1)
}