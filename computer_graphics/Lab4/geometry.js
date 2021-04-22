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

function search(tree, queryRect, res) {
    if (tree ==  null) {
        return
    }

    if (queryRect.left_up.x < tree.point.x && tree.point.x < queryRect.right_bottom.x && queryRect.left_up.y < tree.point.y && tree.point.y < queryRect.right_bottom.y) {
        res.push(tree.point)
    }

    let m = (tree.type === "x") ? tree.point.x : tree.point.y;
    let l = (tree.type === "x") ? queryRect.left_up.x : queryRect.left_up.y;
    let r = (tree.type === "x") ? queryRect.right_bottom.x : queryRect.right_bottom.y;

    if (l < m) {
        search(tree.left, queryRect, res)
    }

    if (m < r) {
        search(tree.right, queryRect, res)
    }
}