'use strict'

function Point(x, y) {
    this.x = x
    this.y = y
}

let points = []

function segmentsCross(startPoint1, endPoint1, startPoint2, endPoint2) {
    function Vector(x, y) {
        this.x = x
        this.y = y
    }
    let zLength = (vec1, vec2) => (vec1.x * vec2.y) - (vec1.y * vec2.x)

    let vecA = new Vector(endPoint1.x - startPoint1.x, endPoint1.y - startPoint1.y)
    let vecB1 = new Vector(startPoint2.x - startPoint1.x, startPoint2.y - startPoint1.y)
    let vecB2 = new Vector(endPoint2.x - startPoint1.x, endPoint2.y - startPoint1.y)
    let res1 =  Math.sign(zLength(vecA, vecB1)) !== Math.sign(zLength(vecA, vecB2))

    vecA = new Vector(endPoint2.x - startPoint2.x, endPoint2.y - startPoint2.y)
    vecB1 = new Vector(startPoint1.x - startPoint2.x, startPoint1.y - startPoint2.y)
    vecB2 = new Vector(endPoint1.x - startPoint2.x, endPoint1.y - startPoint2.y)
    let res2 =  Math.sign(zLength(vecA, vecB1)) !== Math.sign(zLength(vecA, vecB2))

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