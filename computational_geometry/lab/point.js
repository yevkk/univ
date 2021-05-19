'use strict'

class Point {
    #x
    #y

    constructor(x, y) {
        this.#x = x
        this.#y = y
    }

    get x() {
        return this.#x
    }

    get y() {
        return this.#y
    }

    relativeY(point1, point2) {
        let y =  (this.x - point1.x) * (point2.y - point1.y) / (point2.x - point1.x) + point1.y
        return this.y - y
    }

    relativeX(point1, point2) {
        let x =  (this.y - point1.y) * (point2.x - point1.x) / (point2.y - point1.y) + point1.x
        return this.x - x
    }
}

