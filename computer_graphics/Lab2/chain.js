'use strict'

class Chain {
    edges = []

    constructor() {
    }

    pointRelativeX(point) {
        if (point.y > this.edges[0].start.y || point.y < this.edges[this.edges.length - 1].end.y) {
            return null
        }
        let points = this.edges.map(item => item.start)
        points.push(this.edges[this.edges.length - 1].end)

        let left = 0
        let right = points.length - 1
        while (right - left > 1) {
            let mid = Math.ceil((right + left) / 2)
            if (point.y > points[mid].y) {
                right = mid
            } else {
                left = mid
            }
        }

        return this.edges[right - 1].pointRelativeX(point)
    }
}
