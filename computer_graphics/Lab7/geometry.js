'use strict'

let points = []
let convexHull = []

function addPointCH(hull, candidatePoint) {
    drawPoint(mainCanvas.getContext('2d'), "yellow", candidatePoint)
    ;
    if (hull.length < 2) {
        hull.push(candidatePoint)
        return true
    } else if (hull.length === 2) {
        //TODO:
        hull.push(candidatePoint)
        return true
    }

    let n = hull.length
    let distSq = (p1, p2) => ((p1.x - p2.x) ** 2 + (p1.y - p2.y) ** 2)
    let closestI = hull.reduce((current, point, index) => ((distSq(candidatePoint, point) < distSq(candidatePoint, hull[current])) ? index : current), 0)
    let counter = 0;

    drawPoint(mainCanvas.getContext('2d'), "green", hull[closestI])
    ;

    let leftI = closestI;
    while (hull[(n + leftI - 1) % n].relativeY(candidatePoint, hull[leftI]) * hull[(leftI + 1) % n].relativeY(candidatePoint, hull[leftI]) < 0) {
        leftI = (n + leftI - 1) % n
        counter++
        if (leftI === closestI) {
            return false
        }
    }
    drawPoint(mainCanvas.getContext('2d'), "blue", hull[leftI])
    ;


    let rightI = closestI;
    while (hull[(n + rightI - 1) % n].relativeY(candidatePoint, hull[rightI]) * hull[(rightI + 1) % n].relativeY(candidatePoint, hull[rightI]) < 0 || rightI === leftI) {
        rightI = (rightI + 1) % n
        counter++
        if (rightI === closestI) {
            return false
        }
    }
    drawPoint(mainCanvas.getContext('2d'), "blue", hull[rightI])
    ;
    counter--
    let orientation = (p, q, r) => ((q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y))
    if (orientation(hull[leftI], candidatePoint, hull[rightI]) < 0) {
        [leftI, rightI] = [rightI, leftI]
        counter = n - 2 - counter
    }



    if (leftI < rightI) {
        hull.splice(leftI + 1, counter, candidatePoint)
    } else {
        hull.splice(0, rightI)
        hull.splice(leftI + 1, hull.length - leftI - 1, candidatePoint)
    }

    return true
}