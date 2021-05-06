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
    if (points.length < 3) {
        return [...points]
    }

    let orientation = (p, q, r) => ((q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y))

    let hull = []
    let leftMostI = points.reduce((current, point, index) => {
        return (points[current].x > point.x) ? index : current
    }, 0)

    let next = leftMostI
    while (true) {
        hull.push(next)

        next = points.reduce((current, point, index) => {
            return (orientation(points[next], points[index], points[current]) > 0) ? index : current
        }, (next + 1) % points.length)

        if (next === leftMostI) {
            break
        }
    }

    return hull.map(i => points[i])
}

function divideAndConquerConvexHull(points) {
    if (points.length <= divideAndConquerConvexHull.minPoints) {
        return JarvisConvexHull(points)
    }

    let P1 = [], P2 = [];
    points.forEach((item, index) => {
        if (index < points.length / 2) {
            P1.push(item)
        } else {
            P2.push(item)
        }
    })

    let hull1 = divideAndConquerConvexHull(P1)
    let hull2 = divideAndConquerConvexHull(P2)

    return divideAndConquerConvexHull.merge(hull1, hull2)
}

divideAndConquerConvexHull.minPoints = 5

divideAndConquerConvexHull.merge = function (hull1, hull2) {
    //calculating p
    let p1 = hull1[0]
    let p2 = hull1[Math.floor(hull1.length / 3)]
    let p3 = hull1[Math.floor(2 * hull1.length / 3)]
    let p = new Point((p1.x + p2.x + p3.x) / 3, (p1.y + p2.y + p3.y) / 3)

    //checking if p is inside hull2
    let pInsideHull2 = false
    for (let i = 0; i < hull2.length + 1; i++) {
        let point1 = hull2[i % hull2.length]
        let point2 = hull2[(i + 1) % hull2.length]
        if ((point1.y - p.y) * (point2 - p.y) < 0 && p.relativeX(point1, point2) > 0) {
            pInsideHull2 = !pInsideHull2
        }
    }


    let orientation = (p, q, r) => ((q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y))
    let points = [...hull1]
    if (pInsideHull2) {
        points.concat(hull2)
    } else {
        let tangentPoints = []
        let n2 = hull2.length
        hull2.forEach((point, index) => {
            if (hull2[(index + 1) % n2].relativeY(point, p) * hull2[(n2 + index - 1) % n2].relativeY(point, p) > 0) {
                tangentPoints.push(point)
            }
        })
        if (tangentPoints.length === 2) {
            let [upper, lower] = (tangentPoints[0].y > tangentPoints[1].y) ? tangentPoints : tangentPoints.reverse()

            if (p.relativeX(upper, lower) > 0) {
                for (let i = hull2.indexOf(lower); ; i = (i + 1) % n2) {
                    points.push(hull2[i])
                    if (hull2[i] === upper) {
                        break
                    }
                }
            } else {
                for (let i = hull2.indexOf(upper); ; i = (i + 1) % n2) {
                    points.push(hull2[i])
                    if (hull2[i] === lower) {
                        break
                    }
                }
            }
        } else {
            points.concat(hull2)
        }

    }

    let init = points.reduce((current, point, index) => {
        return (points[current].y < point.y) ? index : current
    }, 0)
    points.sort((point1, point2) => orientation(point1, p, point2))
    let n = points.length
    let i = (init + 1) % n
    let counter = 0;
    while (counter <= n) {
        if (orientation(points[(n + i - 1) % n], points[i], points[(i + 1) % n]) > 0) {
            i = (i + 1) % n
        } else {
            points.splice(i, 1)
            n = points.length
            i = (n + i - 1) % n
        }
        counter++
    }

    return points
}


