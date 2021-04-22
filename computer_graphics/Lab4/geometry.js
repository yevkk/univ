'use strict'

let points = []
let tree
let queryRect = {
    left_up: null,
    right_bottom: null,
    reset: function () {
        this.left_up = null
        this.right_bottom = null
    }
}


function createTree(points, prevType) {
    if (points.length === 0) {
        return null
    }

    let node = {}
    node.type = prevType == null ? "x" : (prevType === "x" ? "y" : "x")

    let compareFunc = node.type === "x" ? ((p1, p2) => p1.x - p2.x) : ((p1, p2) => p1.y - p2.y)
    points.sort(compareFunc)
    let midIndex = Math.floor(points.length / 2)
    node.point = points[midIndex]

    node.left = createTree(points.filter((point, index) => index < midIndex), node.type)
    node.right = createTree(points.filter((point, index) => index > midIndex), node.type)

    return node
}