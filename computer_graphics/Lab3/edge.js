'use strict'

class Edge {
    #start
    #end

    constructor(point1, point2) {
        if (point1.y > point2.y) {
            this.#start = point1
            this.#end = point2
        } else {
            this.#start = point2
            this.#end = point1
        }
        point1.addEdge(this)
        point2.addEdge(this)
    }

    get start() {
        return this.#start
    }

    get end() {
        return this.#end
    }

    cross(edge) {
        function Vector(x, y) {
            this.x = x
            this.y = y
        }

        let zLength = (vec1, vec2) => (vec1.x * vec2.y) - (vec1.y * vec2.x)

        let vecA = new Vector(this.end.x - this.start.x, this.end.y - this.start.y)
        let vecB1 = new Vector(edge.start.x - this.start.x, edge.start.y - this.start.y)
        let vecB2 = new Vector(edge.end.x - this.start.x, edge.end.y - this.start.y)
        let res1 = Math.sign(zLength(vecA, vecB1)) !== Math.sign(zLength(vecA, vecB2))

        vecA = new Vector(edge.end.x - edge.start.x, edge.end.y - edge.start.y)
        vecB1 = new Vector(this.start.x - edge.start.x, this.start.y - edge.start.y)
        vecB2 = new Vector(this.end.x - edge.start.x, this.end.y - edge.start.y)
        let res2 = Math.sign(zLength(vecA, vecB1)) !== Math.sign(zLength(vecA, vecB2))

        return res1 && res2
    }

    pointRelativeX(point) {
        let x = (point.y - this.start.y) * (this.end.x - this.start.x) / (this.end.y - this.start.y) + this.start.x
        return point.x - x
    }
}