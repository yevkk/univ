'use strict'

let mainCanvas
let isFinished = false

let points = []

function Point(x, y) {
    this.x = x
    this.y = y
}

function drawPoint(context, color, point) {
    context.fillStyle = color
    context.beginPath()
    context.arc(point.x, point.y, 3, 0, 2 * Math.PI)
    context.fill()
}

function drawLine(context, color, start, end) {
    context.strokeStyle = color
    context.beginPath()
    context.moveTo(start.x, start.y)
    context.lineTo(end.x, end.y)
    context.stroke()
}

function onCanvasClick(e) {
    let point = new Point(e.pageX - mainCanvas.offsetLeft, e.pageY - mainCanvas.offsetTop)
    let color = '#598add'
    let context = mainCanvas.getContext('2d');

    drawPoint(context, color, point)

    if (points.length > 0) {
       drawLine(context, color, points[points.length - 1], point)
    }

    points.push(point)
}

addEventListener("load", () => {
    mainCanvas = document.getElementById('main-canvas')
    mainCanvas.width = mainCanvas.parentElement.clientWidth
    mainCanvas.height = 500
    mainCanvas.addEventListener("click", onCanvasClick)
})

