'use strict'

let pointsA = []
let pointsB = []
let morphSrc = []
let morphDst = []

function segmentsCross(startPoint1, endPoint1, startPoint2, endPoint2) {
    function Vector(x, y) {
        this.x = x
        this.y = y
    }

    let zLength = (vec1, vec2) => (vec1.x * vec2.y) - (vec1.y * vec2.x)

    let vecA = new Vector(endPoint1.x - startPoint1.x, endPoint1.y - startPoint1.y)
    let vecB1 = new Vector(startPoint2.x - startPoint1.x, startPoint2.y - startPoint1.y)
    let vecB2 = new Vector(endPoint2.x - startPoint1.x, endPoint2.y - startPoint1.y)
    let res1 = Math.sign(zLength(vecA, vecB1)) !== Math.sign(zLength(vecA, vecB2))

    vecA = new Vector(endPoint2.x - startPoint2.x, endPoint2.y - startPoint2.y)
    vecB1 = new Vector(startPoint1.x - startPoint2.x, startPoint1.y - startPoint2.y)
    vecB2 = new Vector(endPoint1.x - startPoint2.x, endPoint1.y - startPoint2.y)
    let res2 = Math.sign(zLength(vecA, vecB1)) !== Math.sign(zLength(vecA, vecB2))

    return res1 && res2
}

function isSimple(points) {
    let n = points.length;
    for (let i = 0; i < n; i++) {
        for (let j = 0; j < n; j++) {
            if (j !== i && j !== ((i - 1 >= 0) ? i - 1 : i - 1 + n) && j !== ((i + 1) % n)) {
                if (segmentsCross(points[i], points[(i + 1) % n], points[j], points[(j + 1) % n])) {
                    return false;
                }
            }
        }
    }
    return true;
}

function polarSort(points, center) {
    let tg = (point, center) => ((point.y - center.y) / (point.x - center.x))
    let orientationArray = points.map(point => {
        return {
            point: point,
            tg: tg(point, center)
        }
    })

    let quadIV = orientationArray.filter(item => item.point.x > center.x && item.point.y < center.y).sort((item1, item2) => item2.tg - item1.tg)
    let quadIII = orientationArray.filter(item => item.point.x < center.x && item.point.y < center.y).sort((item1, item2) => item2.tg - item1.tg)
    let quadII = orientationArray.filter(item => item.point.x < center.x && item.point.y > center.y).sort((item1, item2) => item2.tg - item1.tg)
    let quadI = orientationArray.filter(item => item.point.x > center.x && item.point.y > center.y).sort((item1, item2) => item2.tg - item1.tg)

    return [...quadIV.map(item => item.point), ...quadIII.map(item => item.point), ...quadII.map(item => item.point), ...quadI.map(item => item.point)]
}

function buildMorph(pointsA, pointsB) {
    let morphBegin = []
    let morphEnd = []

    let o = new Point(pointsA.reduce((value, point) => value + point.x, 0) / pointsA.length, pointsA.reduce((value, point) => value + point.y, 0) / pointsA.length)

    pointsA = polarSort(pointsA, o)
    pointsB = polarSort(pointsB, o)

    let orientation = (p, q, r) => ((q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y))
    drawPoint(mainCanvas.getContext('2d'), o, 'green')

    let indexB = pointsB.findIndex(point => orientation(point, o, pointsA[0]) > 0);
    let  nA = pointsA.length
    pointsA.forEach((point, index) => {
        let localCounter = 0;
        while(orientation(pointsB[indexB], o, pointsA[(index + 1) % nA]) < 0) {
            morphBegin.push(new Point(point.x, point.y));
            morphEnd.push(new Point(pointsB[indexB].x, pointsB[indexB].y))
            localCounter++
            indexB = (indexB + 1) % pointsB.length
        }
        if (localCounter === 0) {
            morphBegin.push(new Point(point.x, point.y));
            morphEnd.push(new Point(pointsB[indexB].x, pointsB[indexB].y))
        }
    })
;
    return {
        morphBegin,
        morphEnd
    }
}

function mergeCenters(pointsA, pointsB) {
    let oAx = pointsA.reduce((value, point) => value + point.x, 0) / pointsA.length
    let oAy = pointsA.reduce((value, point) => value + point.y, 0) / pointsA.length

    let oBx = pointsB.reduce((value, point) => value + point.x, 0) / pointsB.length
    let oBy = pointsB.reduce((value, point) => value + point.y, 0) / pointsB.length

    let deltaX = oBx - oAx;
    let deltaY = oBy - oAy;

    pointsA.forEach(point => {
        point.x += deltaX
        point.y += deltaY
    })
}

